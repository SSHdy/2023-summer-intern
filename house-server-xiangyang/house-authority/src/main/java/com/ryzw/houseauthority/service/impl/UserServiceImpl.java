package com.ryzw.houseauthority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.ryzw.houseauthority.dto.UserDetailDto;
import com.ryzw.houseauthority.dto.UserRoleDto;
import com.ryzw.houseauthority.entity.*;
import com.ryzw.houseauthority.exception.ServiceException;
import com.ryzw.houseauthority.mapper.*;
import com.ryzw.houseauthority.service.*;
import com.ryzw.houseauthority.vo.NewPassWordVo;
import com.ryzw.houseauthority.vo.UserRoleVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yz
 * @since 2018-12-27
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements IUserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    IUserRoleService userRoleService;
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    private UserRepairMapper userRepairMapper;
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    IUserService userService;

    @Autowired
    DozerBeanMapper dozerBeanMapper;

    @Autowired
    SetOperations<String, Object> setOperations;

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private IUserRepairService userRepairService;

    @Autowired
    IHouseAuthorityService houseAuthorityService;

    @Autowired
    UserHandleMapper userHandleMapper;

    @Autowired
    private IUserHandleService userHandleService;

    @Autowired
    private IUserConfigureService userConfigureService;

    @Autowired
    private UserConfigureMapper userConfigureMapper;

    /**
     * 新增用户的相关信息
     *
     * @param userRoleVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(UserRoleVo userRoleVo) {
        try {
            QueryWrapper<UserEntity> qw = new QueryWrapper<>();
            qw.eq("username", userRoleVo.getUsername());
            int count = userService.count(qw);
            if (count > 0) {
                throw new ServiceException("登录名重复");
            }

            if (StringUtils.isNotBlank(userRoleVo.getPhoneNum())) {
                int countPhoneNum = userService.count(new QueryWrapper<UserEntity>().eq("phone_num", userRoleVo.getPhoneNum()));
                if (countPhoneNum > 0) {
                    throw new ServiceException("手机号重复");
                }
            }

            //获取新增用户的相关信息
            UserEntity user = dozerBeanMapper.map(userRoleVo, UserEntity.class);
            if (StringUtils.isNotEmpty(user.getPassword())) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String encode = encoder.encode(user.getPassword().trim());
                user.setPassword(encode);
            }
            //1.保存用户信息到用户表
            this.save(user);
            Long userId = user.getUserId();

//            2.把角色id集合循环保存到用户角色表
            List<Integer> roleIdList = userRoleVo.getRoleIdList();
            List<UserRole> userRoleList = new ArrayList<>();
            for (Integer roleId : roleIdList) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId).setRoleId(roleId);
                userRoleList.add(userRole);
            }
            userRoleService.saveBatch(userRoleList);

//            3.把维修id集合循环插入到用户维修表
            List<Long> repairIdList = userRoleVo.getRepairIdList();
            List<UserRepair> userRepairList = new ArrayList<>();
            for (Long repairId : repairIdList) {
                UserRepair userRepair = new UserRepair();
                userRepair.setUserId(userId).setRepairId(repairId);
                userRepairList.add(userRepair);
            }
            userRepairService.saveBatch(userRepairList);

//            把维修权限集合缓存到readis
            Set<Object> adds = new HashSet<>();
            if (CollectionUtils.isNotEmpty(userRepairList)) {
                for (UserRepair userRepair : userRepairList) {
                    //获取权限id
                    Long repairId = userRepair.getRepairId();
                    adds.add(repairId);
                }

                //删除redis中拥有该权限中的用户
                this.updateStorage(userId, adds, Collections.EMPTY_SET, "repair");
            }
//
//            4.把处置id集合，保存到用户处置表
            List<Long> handleIdList = userRoleVo.getHandleIdList();
            List<UserHandle> userHandleList = new ArrayList<>();
            for (Long handleId : handleIdList) {
                UserHandle userHandle = new UserHandle();
                userHandle.setUserId(userId).setHandleStep(handleId);
                userHandleList.add(userHandle);
            }
            userHandleService.saveBatch(userHandleList);

            this.updateUserActivity(userRoleVo.getAddRepairRight(), Collections.EMPTY_LIST, userRoleVo.getAddHandleRight(), Collections.EMPTY_LIST, userRoleVo.getAddConfigRight(), Collections.EMPTY_LIST, userId);

//            把处置管理集合缓存到readis
            Set<Object> addsHandle = new HashSet<>();
            if (CollectionUtils.isNotEmpty(userHandleList)) {
                for (UserHandle userHandle : userHandleList) {
                    //获取权限id
                    Long handleId = userHandle.getHandleStep();
                    addsHandle.add(handleId);
                }
                //删除redis中拥有该权限中的用户
                this.updateStorage(userId, addsHandle, Collections.EMPTY_SET, "handle");
            }

//              5.把配置id集合，保存到用户配置表
            List<Long> configIdList = userRoleVo.getConfigIdList();
            List<UserConfigure> userCongigList = new ArrayList<>();
            for (Long configId : configIdList) {
                UserConfigure userConfigure = new UserConfigure();
                userConfigure.setUserId(userId).setConfigureStep(configId);
                userCongigList.add(userConfigure);
            }
            userConfigureService.saveBatch(userCongigList);

            this.updateUserActivity(userRoleVo.getAddRepairRight(), Collections.EMPTY_LIST, userRoleVo.getAddHandleRight(), Collections.EMPTY_LIST, userRoleVo.getAddConfigRight(), Collections.EMPTY_LIST, userId);

//            把配置管理集合缓存到readis
            Set<Object> addsConfig = new HashSet<>();
            if (CollectionUtils.isNotEmpty(userCongigList)) {
                for (UserConfigure userConfigure : userCongigList) {
                    //获取权限id
                    Long configId = userConfigure.getConfigureStep();
                    addsConfig.add(configId);
                }
                //删除redis中拥有该权限中的用户
                this.updateStorage(userId, addsConfig, Collections.EMPTY_SET, "config");
            }
            return true;
        } catch (Exception ex) {
            log.error("user/add error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 更新用户的相关信息
     *
     * @param userRoleVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(UserRoleVo userRoleVo) {
        try {
            QueryWrapper<UserEntity> qw = new QueryWrapper<>();
            qw.eq("username", userRoleVo.getUsername());
            qw.ne("user_id", userRoleVo.getUserId());
            int count = userService.count(qw);
            if (count > 0) {
                throw new ServiceException("登录名重复");
            }

            if (StringUtils.isNotBlank(userRoleVo.getPhoneNum())) {
                int countPhoneNum = userService.count(new QueryWrapper<UserEntity>().eq("phone_num", userRoleVo.getPhoneNum())
                        .ne("user_id", userRoleVo.getUserId()));
                if (countPhoneNum > 0) {
                    throw new ServiceException("手机号重复");
                }
            }

            //获取修改用户的相关信息
            UserEntity user = dozerBeanMapper.map(userRoleVo, UserEntity.class);
            if (StringUtils.isNotEmpty(user.getPassword())) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String encode = encoder.encode(user.getPassword().trim());
                user.setPassword(encode);
            }
            //修改用户信息
            this.updateById(user);
            Long userId = userRoleVo.getUserId();

            //删除reids中用户信息
            String userKey = "user:" + String.valueOf(userRoleVo.getUserId());
            if (redisTemplate.hasKey(userKey)) {
                redisTemplate.delete(userKey);
            }

            //删除redis中token信息
//            String tokenKey = "access:*";
//            Set<String> tokenkeys = redisTemplate.keys(tokenKey);
//            if (CollectionUtils.isNotEmpty(tokenkeys)) {
//                redisTemplate.delete(tokenkeys);
//            }

            //1、删除redis中拥有维修权限的用户
            this.updateStorage(userId,
                    userRoleVo.getAddRepairRight().stream().collect(Collectors.toSet()),
                    userRoleVo.getDeleteRepairRight().stream().collect(Collectors.toSet()), "repair");

            //2、删除redis中拥有处置权限的用户
            this.updateStorage(userId,
                    userRoleVo.getAddHandleRight().stream().collect(Collectors.toSet()),
                    userRoleVo.getDeleteHandleRight().stream().collect(Collectors.toSet()), "handle");

            //3、删除redis中拥有配置权限的用户
            this.updateStorage(userId,
                    userRoleVo.getAddConfigRight().stream().collect(Collectors.toSet()),
                    userRoleVo.getDeleteConfigRight().stream().collect(Collectors.toSet()), "config");

//            删除角色
            deleteRoleRight(userRoleVo, userId);

//            新增角色
            saveRoleRight(userRoleVo, userId);

//            删除权限
            deleteRepairRight(userRoleVo.getDeleteRepairRight(), userId);

//            新增权限
            saveRepairRight(userRoleVo, userId);

//            删除处置管理信息
            deleteHandleRight(userRoleVo.getDeleteHandleRight(), userId);

//            新增处置管理信息
            saveHandleRight(userRoleVo, userId);

//            删除配置管理信息
            deleteConfigRight(userRoleVo.getDeleteConfigRight(), userId);

//            新增配置管理信息
            saveConfigRight(userRoleVo, userId);

            //远程调用修改工作流信息
            updateUserActivity(userRoleVo.getAddRepairRight(), userRoleVo.getDeleteRepairRight(), userRoleVo.getAddHandleRight(), userRoleVo.getDeleteHandleRight(), userRoleVo.getAddConfigRight(), userRoleVo.getDeleteConfigRight(), userId);

            return true;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 远程调用修改工作流信息
     *
     * @param addRepairRight
     * @param deleteRepairRight
     * @param userId
     */
    private void updateUserActivity(List<Long> addRepairRight, List<Long> deleteRepairRight, List<Long> addHandleRight, List<Long> deleteHandleRight, List<Long> addConfigRight, List<Long> deleteConfigRight, @NotNull(message = "用户ID不能为空") Long userId) throws Exception {
        if (CollectionUtils.isNotEmpty(addRepairRight)
                || CollectionUtils.isNotEmpty(deleteRepairRight)) {
            Map<String, Object> repairMap = new HashMap<>();
            repairMap.put("userId", userId);
            repairMap.put("steps", addRepairRight);
            repairMap.put("removeSteps", deleteRepairRight);
            houseAuthorityService.updateRepairAuthority(repairMap);
        }

        if (CollectionUtils.isNotEmpty(addHandleRight)
                || CollectionUtils.isNotEmpty(deleteHandleRight)) {
            Map<String, Object> handleMap = new HashMap<>();
            handleMap.put("userId", userId);
            handleMap.put("steps", addHandleRight);
            handleMap.put("removeSteps", deleteHandleRight);
            houseAuthorityService.updateHandleAuthority(handleMap);
        }

        if (CollectionUtils.isNotEmpty(addConfigRight)
                || CollectionUtils.isNotEmpty(deleteConfigRight)) {
            Map<String, Object> configMap = new HashMap<>();
            configMap.put("userId", userId);
            configMap.put("steps", addConfigRight);
            configMap.put("removeSteps", deleteConfigRight);
            houseAuthorityService.updateConfigureAuthority(configMap);
        }
    }

    /**
     * 新增角色
     *
     * @param userRoleVo
     * @param userId
     */
    private void saveRoleRight(UserRoleVo userRoleVo, Long userId) {
        List<Integer> roleAdds = userRoleVo.getAddRoleRight();
        if (CollectionUtils.isNotEmpty(roleAdds)) {
            List<UserRole> userRoleLists = new ArrayList<>();
            for (Integer roleId : roleAdds) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId).setRoleId(roleId);
                userRoleLists.add(userRole);
            }
            userRoleService.saveBatch(userRoleLists);
        }
    }

    /**
     * 删除角色
     *
     * @param userRoleVo
     * @param userId
     */
    private void deleteRoleRight(UserRoleVo userRoleVo, Long userId) {

        List<Integer> deletRoleIdList = userRoleVo.getDeleteRoleRight();
        if (CollectionUtils.isNotEmpty(deletRoleIdList)) {
            for (Integer roleId : deletRoleIdList) {
                Map<String, Object> columnMap = new HashMap<>();
                columnMap.put("user_id", userId);
                columnMap.put("role_id", roleId);
                userRoleMapper.deleteByMap(columnMap);
            }
        }
    }

    /**
     * 添加需要添加的用户维修权限
     *
     * @param userRoleVo
     * @param userId
     */
    private void saveRepairRight(UserRoleVo userRoleVo, Long userId) {
        List<Long> repairAdds = userRoleVo.getAddRepairRight();
        if (CollectionUtils.isNotEmpty(repairAdds)) {
            List<UserRepair> userRepairLists = new ArrayList<>();
            for (Long repairId : repairAdds) {
                UserRepair userRepair = new UserRepair();
                userRepair.setUserId(userId).setRepairId(repairId);
                userRepairLists.add(userRepair);
            }
            userRepairService.saveBatch(userRepairLists);
        }
    }

    /**
     * 添加需要添加的用户处置权限
     *
     * @param userRoleVo
     * @param userId
     */
    private void saveHandleRight(UserRoleVo userRoleVo, Long userId) {
        List<Long> handleAdds = userRoleVo.getAddHandleRight();
        if (CollectionUtils.isNotEmpty(handleAdds)) {
            List<UserHandle> userHandleLists = new ArrayList<>();
            for (Long handleId : handleAdds) {
                UserHandle userHandle = new UserHandle();
                userHandle.setUserId(userId).setHandleStep(handleId);
                userHandleLists.add(userHandle);
            }
            userHandleService.saveBatch(userHandleLists);
        }
    }

    /**
     * 添加用户配置权限
     *
     * @param userRoleVo
     * @param userId
     */
    private void saveConfigRight(UserRoleVo userRoleVo, Long userId) {
        List<Long> configAdds = userRoleVo.getAddConfigRight();
        if (CollectionUtils.isNotEmpty(configAdds)) {
            List<UserConfigure> userConfigureLists = new ArrayList<>();
            for (Long configId : configAdds) {
                UserConfigure userConfigure = new UserConfigure();
                userConfigure.setUserId(userId).setConfigureStep(configId);
                userConfigureLists.add(userConfigure);
            }
            userConfigureService.saveBatch(userConfigureLists);
        }
    }

    /**
     * 删除用户维修权限
     *
     * @param deletRepairIdList
     * @param userId
     */
    private void deleteRepairRight(List<Long> deletRepairIdList, Long userId) {
        if (CollectionUtils.isNotEmpty(deletRepairIdList)) {
            for (Long repairId : deletRepairIdList) {
                Map<String, Object> columnMap = new HashMap<>();
                columnMap.put("user_id", userId);
                columnMap.put("repair_id", repairId);
                userRepairMapper.deleteByMap(columnMap);

            }
        }
    }

    /**
     * 删除用户处置权限
     *
     * @param deletHandleIdList
     * @param userId
     */
    private void deleteHandleRight(List<Long> deletHandleIdList, Long userId) {
        if (CollectionUtils.isNotEmpty(deletHandleIdList)) {
            for (Long handleId : deletHandleIdList) {
                Map<String, Object> columnMap = new HashMap<>();
                columnMap.put("user_id", userId);
                columnMap.put("handle_step", handleId);
                userHandleMapper.deleteByMap(columnMap);

            }
        }
    }

    /**
     * 删除用户配置权限
     *
     * @param deletConfigIdList
     * @param userId
     */
    private void deleteConfigRight(List<Long> deletConfigIdList, Long userId) {
        if (CollectionUtils.isNotEmpty(deletConfigIdList)) {
            for (Long configId : deletConfigIdList) {
                Map<String, Object> columnMap = new HashMap<>();
                columnMap.put("user_id", userId);
                columnMap.put("configure_step", configId);
                userConfigureMapper.deleteByMap(columnMap);

            }
        }
    }


    /**
     * 查询用户和勾选中的角色详情
     *
     * @param userRoleVo
     * @return
     */
    @Override
    public UserRoleDto selectUserAndRole(UserRoleVo userRoleVo) {
        //获取userid
        Long userId = userRoleVo.getUserId();
        //找到对应的user
        UserEntity user = userMapper.selectById(userId);
        //查询关联表，找到用户对应的角色权限
        List<UserRole> userRoleList = userRoleMapper.selectList(new QueryWrapper<UserRole>().eq("user_id", userId));
        List<Role> roleList = new ArrayList<>();
        List<Long> userRoleIdList = new ArrayList<>();

        for (UserRole userRole : userRoleList) {
            Integer roleId = userRole.getRoleId();
            Role role = roleMapper.selectById(roleId);
            roleList.add(role);
            userRoleIdList.add(userRole.getUserRoleId());
        }


        List<UserRepair> userRepairList = userRepairMapper.selectList(new QueryWrapper<UserRepair>().eq("user_id", userId));
        List<Long> userRepairIdList = new ArrayList<>();
        for (UserRepair userRepair : userRepairList) {
            Long repairId = userRepair.getRepairId();
            userRepairIdList.add(repairId);
        }

        List<UserHandle> userHandleList = userHandleMapper.selectList(new QueryWrapper<UserHandle>().eq("user_id", userId));
        List<Long> handleIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userHandleList)) {
            for (UserHandle userHandle : userHandleList) {
                handleIdList.add(userHandle.getHandleStep());
            }
        }

        List<UserConfigure> userConfigList = userConfigureMapper.selectList(new QueryWrapper<UserConfigure>().eq("user_id", userId));
        List<Long> configIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userConfigList)) {
            for (UserConfigure userConfigure : userConfigList) {
                configIdList.add(userConfigure.getConfigureStep());
            }
        }

        UserRoleDto userRoleDto = new UserRoleDto();
        //把对应的信息存放到dto
        userRoleDto.setUser(user);
        userRoleDto.setRoleList(roleList);
        userRoleDto.setUserRoleIdList(userRoleIdList);
        userRoleDto.setRepairIdList(userRepairIdList);
        userRoleDto.setHandleIdList(handleIdList);
        userRoleDto.setConfigIdList(configIdList);
        return userRoleDto;
    }

    /**
     * 修改密码
     *
     * @param newPassWordVo
     * @return
     */
    @Override
    public boolean updatePassWord(NewPassWordVo newPassWordVo) {
        try {
            //根据id获取对应的user实体
            UserEntity userEntity = userMapper.selectOne(new QueryWrapper<UserEntity>().eq("username", newPassWordVo.getUsername()));
            //密码加密
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (userEntity == null) {
                throw new ServiceException("该用户名不存在！");
            }
            if (!encoder.matches(newPassWordVo.getPassword(), userEntity.getPassword())) {
                //校验失败
                throw new ServiceException("密码校验错误！");
            } else {
                //校验成功
                UserEntity newUserEntity = userEntity.setPassword(encoder.encode(newPassWordVo.getNewPassword()));
                this.updateById(newUserEntity);
                return true;
            }
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 根据用户编号查询权限可以查看的单位编号
     *
     * @param userid
     * @return
     */
    @Override
    public List<Object> getUnitIdByUser(Long userid) {
        String redisKey = "user:" + userid;
        if (redisTemplate.hasKey(redisKey)) {
            return Lists.newArrayList(setOperations.members(redisKey));
        } else {
            List<Object> units = userMapper.getUnitIdByUser(userid);
            setOperations.add("user:" + userid, units.toArray(new Long[0]));
            redisTemplate.expire("user:" + userid, 7, TimeUnit.DAYS);
            return units;
        }
    }


    /**
     * 删除用户信息
     *
     * @param user
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(UserEntity user) {
        try {
            Long userId = user.getUserId();
//            删除用户维修信息
            QueryWrapper<UserRepair> wrapper = new QueryWrapper<>();
            wrapper.select("repair_id");
            wrapper.eq("user_id", userId);
            List<Long> repairIdList = userRepairService.list(wrapper).stream()
                    .map(UserRepair::getRepairId).collect(Collectors.toList());

//            删除用户处置信息
            QueryWrapper<UserHandle> handleWrapper = new QueryWrapper<>();
            handleWrapper.select("handle_step");
            handleWrapper.eq("user_id", userId);
            List<Long> handleIdList = userHandleService.list(handleWrapper).stream()
                    .map(UserHandle::getHandleStep).collect(Collectors.toList());

//            删除用户配置信息
            QueryWrapper<UserConfigure> configWrapper = new QueryWrapper<>();
            configWrapper.select("configure_step");
            configWrapper.eq("user_id", userId);
            List<Long> configIdList = userConfigureService.list(configWrapper).stream()
                    .map(UserConfigure::getConfigureStep).collect(Collectors.toList());

            userMapper.deleteById(userId);
            userRoleMapper.delete(new QueryWrapper<UserRole>().eq("user_id", userId));

            this.deleteHandleRight(handleIdList, userId);
            this.deleteConfigRight(configIdList, userId);
            this.deleteRepairRight(repairIdList, userId);
            redisTemplate.delete("user:" + userId);

            this.updateUserActivity(Collections.EMPTY_LIST, repairIdList, Collections.EMPTY_LIST, handleIdList, Collections.EMPTY_LIST, configIdList, userId);


            //删除redis中拥有处置权限的信息
            this.updateStorage(userId, Collections.EMPTY_SET,
                    handleIdList.stream().collect(Collectors.toSet()), "handle");

            //删除redis中拥有配置权限的信息
            this.updateStorage(userId, Collections.EMPTY_SET,
                    configIdList.stream().collect(Collectors.toSet()), "config");

            //删除redis中拥有维修权限的信息
            this.updateStorage(userId, Collections.EMPTY_SET,
                    repairIdList.stream().collect(Collectors.toSet()), "repair");


            return true;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteIds(List<Long> userIds) {
        try {
            userMapper.deleteBatchIds(userIds);
            userRoleMapper.delete(new QueryWrapper<UserRole>().in("user_id", userIds));
            redisTemplate.delete(userIds.stream().map(p -> "user:" + p).collect(Collectors.toList()));
            return true;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 根据单位编号查询用户编号
     *
     * @param unitId
     * @return
     */
    @Override
    public List<Long> getUserIdByUnit(Long unitId) {
        return userMapper.getUserIdByUnit(unitId);
    }

    /**
     * 根据权限Id查询对应的用户集合
     *
     * @param repairId
     * @return
     */
    @Override
    public Set selectRepairList(Integer repairId) {
        try {
            if (redisTemplate.hasKey("repair:right:" + repairId)) {
                Set members = redisTemplate.opsForSet().members("repair:right:" + repairId);
                return members;
            } else {
                Set<Object> repairSet = new HashSet<>();
                List<UserRepair> userRepairList = userRepairMapper.selectList(new QueryWrapper<UserRepair>().eq("repair_id", repairId));
                if (CollectionUtils.isNotEmpty(userRepairList)) {
                    repairSet = userRepairList.stream().map(UserRepair::getUserId).collect(Collectors.toSet());
                    redisTemplate.opsForSet().add("repair:right:" + repairId, repairSet.toArray(new Long[0]));
                }
                return repairSet;
            }
        } catch (Exception ex) {
            log.error("selectRepairListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除用户（删除该用户所有权限）
     *
     * @param userId
     * @return
     */
    @Override
    public boolean deleteRepairById(Long userId) {
        try {
//            第一步：删除redis用户维修权限
            List<UserRepair> userRepairList = userRepairMapper.selectList(new QueryWrapper<UserRepair>().eq("user_id", userId));
            Set<Object> deletes = new HashSet<>();
            if (CollectionUtils.isNotEmpty(userRepairList)) {
                for (UserRepair userRepair : userRepairList) {
                    //获取维修权限id
                    Long repairId = userRepair.getRepairId();
                    deletes.add(repairId);
                }
                //删除redis中拥有该维修权限的用户
                updateStorage(userId, null, deletes, "repair");
            }
//            第二步：删除redis用户处置权限
            List<UserHandle> userHandleList = userHandleMapper.selectList(new QueryWrapper<UserHandle>().eq("user_id", userId));
            Set<Object> deleteHandle = new HashSet<>();
            if (CollectionUtils.isNotEmpty(userHandleList)) {
                for (UserHandle userHandle : userHandleList) {
                    //获取处置权限id
                    Long handleStep = userHandle.getHandleStep();
                    deleteHandle.add(handleStep);
                }
                //删除redis中拥有该处置权限的用户
                updateStorage(userId, null, deleteHandle, "handle");
            }

//            第三步：删除redis用户配置权限
            List<UserConfigure> userConfigureList = userConfigureMapper.selectList(new QueryWrapper<UserConfigure>().eq("user_id", userId));
            Set<Object> deleteConfig = new HashSet<>();
            if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(userConfigureList)) {
                for (UserConfigure userConfigure : userConfigureList) {
                    //获取配置权限id
                    Long configureStep = userConfigure.getConfigureStep();
                    deleteConfig.add(configureStep);
                }
                //删除redis中拥有该配置权限的用户
                updateStorage(userId, null, deleteConfig, "configure");
            }

            //删除数据库中的用户维修权限表
            userRepairMapper.delete(new QueryWrapper<UserRepair>().eq("user_id", userId));
            //删除数据库中的用户处置权限表
            userHandleMapper.delete(new QueryWrapper<UserHandle>().eq("user_id", userId));
            //删除数据库中的用户角色表
            userRoleMapper.delete(new QueryWrapper<UserRole>().eq("user_id", userId));
            //删除数据库中的用户配置权限表
            userConfigureMapper.delete(new QueryWrapper<UserConfigure>().eq("user_id", userId));
            //删除数据库中的用户表
            userMapper.deleteById(userId);
            return true;
        } catch (Exception ex) {
            log.error("deleteRepairByIdError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 根据处置权限Id查询对应的用户集合
     *
     * @param step
     * @return
     */
    @Override
    public Set selectHandleByStep(Integer step) {
        try {
            if (redisTemplate.hasKey("handle:right:" + step)) {
                Set members = redisTemplate.opsForSet().members("handle:right:" + step);
                return members;
            } else {
                Set<Object> handleSet = new HashSet<>();
                List<UserHandle> userHandleList = userHandleMapper.selectList(new QueryWrapper<UserHandle>().eq("handle_step", step));
                if (CollectionUtils.isNotEmpty(userHandleList)) {
                    handleSet = userHandleList.stream().map(UserHandle::getUserId).collect(Collectors.toSet());
                    redisTemplate.opsForSet().add("handle:right:" + step, handleSet.toArray(new Long[0]));
                }
                return handleSet;
            }
        } catch (Exception ex) {
            log.error("selectHandleByStepError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 根据配置权限Id查询对应的用户集合
     *
     * @param step
     * @return
     */
    @Override
    public Set selectConfigByStep(Integer step) {
        try {
            if (redisTemplate.hasKey("config:right:" + step)) {
                Set members = redisTemplate.opsForSet().members("config:right:" + step);
                return members;
            } else {
                Set<Object> configSet = new HashSet<>();
                List<UserConfigure> userConfigureList = userConfigureMapper.selectList(new QueryWrapper<UserConfigure>().eq("configure_step", step));
                if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isNotEmpty(userConfigureList)) {
                    configSet = userConfigureList.stream().map(UserConfigure::getUserId).collect(Collectors.toSet());
                    redisTemplate.opsForSet().add("config:right:" + step, configSet.toArray(new Long[0]));
                }
                return configSet;
            }
        } catch (Exception ex) {
            log.error("selectConfigByStepError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 操作Redis
     *
     * @param userId
     * @param adds
     * @param deletes
     * @return
     */
    private Object updateStorage(Long userId, Set<Object> adds, Set<Object> deletes, String type) {
        return redisTemplate.opsForSet().getOperations().execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                if (CollectionUtils.isNotEmpty(deletes)) {
                    for (Object deleteObj : deletes) {
                        redisConnection.sRem((type + ":right:" + deleteObj).getBytes(), String.valueOf(userId).getBytes());
                    }
                }
                if (CollectionUtils.isNotEmpty(adds)) {
                    for (Object addObj : adds) {
                        if (redisTemplate.hasKey(type + ":right:" + addObj)) {
                            redisConnection.sAdd((type + ":right:" + addObj).getBytes(), String.valueOf(userId).getBytes());
                        }
                    }
                }
                return null;
            }
        });
    }


    /**
     * 查询用户详情
     *
     * @param userName
     * @return
     */
    @Override
    public UserDetailDto userDetail(String userName) {
        List<UserDetailDto> userDetailDtoList = userMapper.userDetail(userName);
        UserDetailDto resultDto = null;
        if (userDetailDtoList.size() == 1) {
            resultDto = userDetailDtoList.get(0);
        } else {
            for (UserDetailDto userDetailDto : userDetailDtoList) {
                if (userDetailDto.getRoleBanner() == 0) {
                    resultDto = userDetailDto;
                }
            }
        }
        return resultDto;
    }
}
