package com.ryzw.houseauthority.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.houseauthority.dto.RoleDetailDto;
import com.ryzw.houseauthority.dto.RoleMenuUnitDto;
import com.ryzw.houseauthority.entity.*;
import com.ryzw.houseauthority.exception.ServiceException;
import com.ryzw.houseauthority.mapper.*;
import com.ryzw.houseauthority.service.IRoleMenuService;
import com.ryzw.houseauthority.service.IRoleService;
import com.ryzw.houseauthority.service.IRoleUnitService;
import com.ryzw.houseauthority.service.IUnitService;
import com.ryzw.houseauthority.vo.BasicVo;
import com.ryzw.houseauthority.vo.RoleMenuUnitVo;
import com.ryzw.houseauthority.vo.RoleUnitMenuVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    UserRoleMapper userRoleMapper;
    @Autowired
    IRoleMenuService roleMenuService;
    @Autowired
    IRoleUnitService roleUnitService;
    @Autowired
    IUnitService unitService;

    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RoleMenuMapper roleMenuMapper;
    @Autowired
    RoleUnitMapper roleUnitMapper;

    @Autowired
    MenuMapper menuMapper;

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 如果该角色是否被用户使用，有就无法进行删除，没有被使用，才可删除
     *
     * @param roleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteNotUser(Integer roleId) {
        try {
            Integer count = userRoleMapper.selectCount(new QueryWrapper<UserRole>().eq("role_id", roleId));
            if (count > 0) {
                throw new ServiceException("该角色被用户使用，无法进行删除");
            }
            roleMenuMapper.delete(new QueryWrapper<RoleMenu>().eq("role_id", roleId));
            roleUnitMapper.delete(new QueryWrapper<RoleUnit>().eq("role_id", roleId));
            roleMapper.deleteById(roleId);
            deleteUserCache(roleId);
            return true;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询角色以及勾选的菜单和单位权限的详情
     *
     * @param roleMenuUnitVo
     * @return
     */
    @Override
    public RoleMenuUnitDto selectRoleMenuUnit(RoleMenuUnitVo roleMenuUnitVo) {
        //接收roleid
        Integer roleId = roleMenuUnitVo.getRoleId();
        //查询id对应的角色
        Role role = roleMapper.selectById(roleId);
        //查询菜单关联表里面的数据
        List<RoleMenu> roleMenuList = roleMenuMapper.selectList(new QueryWrapper<RoleMenu>().eq("role_id", roleId));
        List<Menu> menuList = new ArrayList<>();
        List<Long> roleMenuIdList = new ArrayList<>();
        List<Long> roleUnitIdList = new ArrayList<>();


        List<Long> menuIds = roleMenuList.stream().map(RoleMenu::getMenuId).collect(Collectors.toList());
        roleMenuIdList.addAll(menuIds);
        Optional.ofNullable(menuMapper.selectBatchIds(menuIds)).ifPresent(menuList::addAll);


//        //找到对应的菜单
//        for (RoleMenu roleMenu : roleMenuList) {
//            Menu menu = menuMapper.selectById(roleMenu.getMenuId());
//            roleMenuIdList.add(roleMenu.getRoleMenuId());
//            Optional.ofNullable(menu).ifPresent(menuList::add);
//        }
        //查询单位关联表里面的数据
        List<RoleUnit> roleUnitList = roleUnitMapper.selectList(new QueryWrapper<RoleUnit>().eq("role_id", roleId));
        List<Long> unitIdList = new ArrayList<>();
        //找到对应的单位

        if (CollectionUtils.isNotEmpty(roleUnitList)) {
            for (RoleUnit roleUnit : roleUnitList) {
                roleUnitIdList.add(roleUnit.getRoleUnitId());
                unitIdList.add(roleUnit.getUnitId());
            }
        }


        //调用接口查询单位名称
        List<Map<Object, Object>> roleUnit = unitService.getRoleUnit(unitIdList);
        RoleMenuUnitDto roleMenuUnitDto = new RoleMenuUnitDto();
        roleMenuUnitDto.setUnitList(roleUnit);
        roleMenuUnitDto.setMenuList(menuList);
        //把角色的详情添加到dto里面
        roleMenuUnitDto.setRole(role);
        //查询并把roleMenuIdList给Dto
        roleMenuUnitDto.setRoleMenuIdList(roleMenuIdList);
        //查询并把roleUnitIdList给Dto
        roleMenuUnitDto.setRoleUnitIdList(roleUnitIdList);
        //查询并把unitIdList给Dto
        roleMenuUnitDto.setUnitIdList(unitIdList);
        return roleMenuUnitDto;
    }

    /**
     * 按用户查询角色
     *
     * @param userId
     * @return
     */
    @Override
    public List<Role> getRoleByUserId(Long userId) {
        return roleMapper.getRoleByUserId(userId);
    }


    /**
     * 添加角色，并添加单位和菜单权限
     *
     * @param roleUnitMenuVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRole(RoleUnitMenuVo roleUnitMenuVo) {
        try {
            Role role = dozerBeanMapper.map(roleUnitMenuVo, Role.class);
//            生成随机的UUID，存储到role_value
            String roleValue = UUID.randomUUID().toString();
            role.setRoleValue(roleValue);
            this.save(role);
            Integer roleId = role.getRoleId();
//            把菜单id循环插入到角色菜单表
            List<Long> menuIds = roleUnitMenuVo.getMenuId();
            List<RoleMenu> roleMenuList = new ArrayList<>();
            for (Long menuId : menuIds) {
                roleMenuList.add(RoleMenu.builder().menuId(menuId).roleId(roleId).build());
            }
            roleMenuService.saveBatch(roleMenuList);

//            把单位id循环插入到角色单位表
            List<Long> unitIds = roleUnitMenuVo.getUnitId();
            List<RoleUnit> roleUnitList = new ArrayList<>();
            for (Long unitId : unitIds) {
                roleUnitList.add(RoleUnit.builder().unitId(unitId).roleId(roleId).build());
            }
            roleUnitService.saveBatch(roleUnitList);

            return true;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改角色，并修改单位和菜单权限
     *
     * @param roleUnitMenuVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRole(RoleUnitMenuVo roleUnitMenuVo) {
        try {

            if (roleUnitMenuVo.getRoleId() != null) {
                deleteUserCache(roleUnitMenuVo.getRoleId());

                Role role = dozerBeanMapper.map(roleUnitMenuVo, Role.class);
                this.updateById(role);

                Integer roleId = roleUnitMenuVo.getRoleId();

//               第一步，查询旧的roleMenu关系数组,并删除旧的角色菜单信息
                List<Long> oldRoleMenuIds = roleMenuService.list(new QueryWrapper<RoleMenu>()
                        .eq("role_id", roleUnitMenuVo.getRoleId())).stream().map(RoleMenu::getRoleMenuId)
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(oldRoleMenuIds)) {
                    roleMenuMapper.deleteBatchIds(oldRoleMenuIds);
                }

//                第二步，查询旧的roleUnit关系数组，并删除旧的角色单位信息
                List<Long> oldRoleUnitIds = roleUnitService.list(new QueryWrapper<RoleUnit>()
                        .eq("role_id", roleUnitMenuVo.getRoleId())).stream().map(RoleUnit::getRoleUnitId)
                        .collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(oldRoleUnitIds)) {
                    roleUnitMapper.deleteBatchIds(oldRoleUnitIds);
                }

//                第三步，新增新的角色菜单信息
                List<Long> menuIds = roleUnitMenuVo.getMenuId();
//                用于存储新的关系表信息
                List<RoleMenu> roleMenuList = new ArrayList<>();
                for (Long menuId : menuIds) {
                    roleMenuList.add(RoleMenu.builder().menuId(menuId).roleId(roleId).build());
                }
                roleMenuService.saveBatch(roleMenuList);

//                第四步，新增新的角色单位信息
                List<Long> unitIds = roleUnitMenuVo.getUnitId();
//                用于存储新的关系表信息
                List<RoleUnit> roleUnitList = new ArrayList<>();
                for (Long unitId : unitIds) {
                    roleUnitList.add(RoleUnit.builder().unitId(unitId).roleId(roleId).build());
                }
                roleUnitService.saveBatch(roleUnitList);
            }
            return true;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 角色信息详情查询
     *
     * @param basicVo
     * @return
     */
    @Override
    public RoleDetailDto roleDetail(BasicVo<Long> basicVo) {
        RoleDetailDto detailDto = roleMapper.roleDetail(basicVo.getId());
        if (CollectionUtils.isNotEmpty(detailDto.getUnitId())) {
            List<Map<Object, Object>> unitList = unitService.getRoleUnit(detailDto.getUnitId());
            detailDto.setUnitList(unitList);
        }
        return detailDto;
    }


    /**
     * 角色信息更新查询
     *
     * @param basicVo
     * @return
     */
    @Override
    public RoleDetailDto roleUpdate(BasicVo<Long> basicVo) {
        RoleDetailDto detailDto = roleMapper.roleUpdate(basicVo.getId());
        Optional.ofNullable(detailDto).orElseThrow(() -> new ServiceException("查询不到此角色！"));
        if (CollectionUtils.isNotEmpty(detailDto.getUnitId())) {
            List<Map<Object, Object>> unitList = unitService.getRoleUnit(detailDto.getUnitId());
            detailDto.setUnitList(unitList);
        }
        return detailDto;
    }

    /**
     * 删除用户缓存
     *
     * @param roleIds
     */
    private void deleteUserCache(Integer... roleIds) {
        if (ArrayUtils.isNotEmpty(roleIds)) {
            QueryWrapper<UserRole> wrapper = new QueryWrapper<>();
            wrapper.in("role_id", roleIds);
            wrapper.select("user_id");
            List<UserRole> userRoleList = userRoleMapper.selectList(wrapper);
            List<String> keys = userRoleList.stream().map(p -> "user:" + p.getUserId())
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(keys)) {
                redisTemplate.delete(keys);
            }
        }
    }


    /**
     * 冻结用户
     *
     * @param role
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean isFreeze(Role role) {
        try {
            roleMapper.updateById(role);
            deleteUserCache(role.getRoleId());
            return true;
        } catch (Exception ex) {
            log.error("freeze role error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}
