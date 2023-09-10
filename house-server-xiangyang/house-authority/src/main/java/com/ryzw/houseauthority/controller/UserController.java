package com.ryzw.houseauthority.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.houseauthority.dto.UserDetailDto;
import com.ryzw.houseauthority.dto.UserRoleDto;
import com.ryzw.houseauthority.entity.UserEntity;
import com.ryzw.houseauthority.exception.ServiceException;
import com.ryzw.houseauthority.service.IUserService;
import com.ryzw.houseauthority.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yz
 * @since 2018-12-27
 */
@RestController
@RequestMapping("/user")
@Api(value = "用户管理", description = "用户管理")
public class UserController {
    @Autowired
    IUserService userService;


    @Autowired
    DozerBeanMapper dozerBeanMapper;

    /**
     * 查询用户
     *
     * @param userListVo
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ApiOperation(value = "查询用户", notes = "查询用户")
    public IPage<UserEntity> userList(@RequestBody UserListVo userListVo) {
        Page<UserEntity> page = new Page<>(userListVo.getCurrentPage(), userListVo.getPageSize());
        QueryWrapper<UserEntity> queryWrapper = new QueryWrapper<>();

        if (Strings.isNotBlank(userListVo.getName())) {
            queryWrapper.like("name", userListVo.getName());
        }

        if (Strings.isNotBlank(userListVo.getUsername())) {
            queryWrapper.like("username", userListVo.getUsername());
        }

        if (userListVo.getIsFreeze() != null) {
            queryWrapper.eq("is_freeze", userListVo.getIsFreeze());
        }
        queryWrapper.orderByDesc("user_id");

        return userService.page(page, queryWrapper);
    }

    /**
     * 修改用户和角色
     *
     * @param userRoleVo
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ApiOperation(value = "修改用户和角色", notes = "修改用户和角色")
    public boolean update(@RequestBody UserRoleVo userRoleVo) {
        return userService.update(userRoleVo);
    }

    /**
     * 新增用户,角色,维修，处置，配置信息
     *
     * @param userRoleVo
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ApiOperation(value = "新增用户,角色,维修，处置，配置信息", notes = "新增用户,角色,维修，处置，配置信息")
    public boolean add(@RequestBody UserRoleVo userRoleVo) {
        return userService.add(userRoleVo);
    }


    /**
     * 用户详情
     *
     * @param userRoleVo
     * @return
     */
    @RequestMapping(value = "selectUserAndRole", method = RequestMethod.POST)
    @ApiOperation(value = "用户详情", notes = "用户详情")
    public UserRoleDto selectUserAndRole(@RequestBody UserRoleVo userRoleVo) {
        return userService.selectUserAndRole(userRoleVo);
    }

    /**
     * 用于删除用户
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ApiOperation(value = "用于删除用户", notes = "用于删除用户")
    public boolean delete(@RequestBody UserEntity user) {
        return userService.delete(user);
    }

    /**
     * 用于批量删除用户
     *
     * @param userVo
     * @return
     */
    @RequestMapping(value = "deleteIds", method = RequestMethod.POST)
    @ApiOperation(value = "用于批量删除用户", notes = "用于批量删除用户")
    public boolean deleteIds(@RequestBody UserVo userVo) {
        return userService.deleteIds(userVo.getUserIdList());
    }

    /**
     * 用于单独操作冻结或者启用
     *
     * @param userVo
     * @return
     */
    @RequestMapping(value = "isFreeze", method = RequestMethod.POST)
    @ApiOperation(value = "用于单独操作冻结或者启用", notes = "用于单独操作冻结或者启用")
    public boolean isFreeze(@RequestBody UserVo userVo) {
        UserEntity user = dozerBeanMapper.map(userVo, UserEntity.class);
        return userService.updateById(user);
    }

    /**
     * 用于判断登录名是否重复
     *
     * @param userVo
     * @return
     */
    @RequestMapping(value = "unique", method = RequestMethod.POST)
    @ApiOperation(value = "用于判断登录名是否重复", notes = "用于判断登录名是否重复")
    public boolean unique(@RequestBody UserVo userVo) {
        Long userId = userVo.getUserId();
        QueryWrapper<UserEntity> qw = new QueryWrapper<>();
        qw = qw.eq("username", userVo.getUsername());
        int count = (userId == null) ? userService.count(qw) : userService.count(qw.ne("user_id", userId));
        if (count > 0) {
            throw new ServiceException("登录名重复");
        }
        return true;
    }

    /**
     * 用于判断手机号是否重复
     *
     * @param userVo
     * @return
     */
    @RequestMapping(value = "uniquePhoneName", method = RequestMethod.POST)
    @ApiOperation(value = "用于判断手机号是否重复", notes = "用于判断手机号是否重复")
    public boolean uniquePhoneName(@RequestBody UserVo userVo) {
        Long userId = userVo.getUserId();
        QueryWrapper<UserEntity> qw = new QueryWrapper<>();
        if(StringUtils.isNotBlank(userVo.getPhoneNum())){
            qw = qw.eq("phone_num", userVo.getPhoneNum());
        }else{
            throw new ServiceException("手机号不能为空！");
        }
        int count = (userId == null) ? userService.count(qw) : userService.count(qw.ne("user_id", userId));
        if (count > 0) {
            throw new ServiceException("手机号重复");
        }
        return true;
    }

    @RequestMapping("/info")
    @ApiOperation(value = "登录", notes = "登录")
    public Principal user(Principal user) {
        return user;
    }

    /**
     * 修改密码
     *
     * @param newPassWordVo
     * @return
     */
    @RequestMapping(value = "/updatePassWord", method = RequestMethod.POST)
    @ApiOperation(value = "登录", notes = "登录")
    public boolean updatePassWord(@RequestBody NewPassWordVo newPassWordVo) {
        return userService.updatePassWord(newPassWordVo);
    }

    /**
     * 查询用户详情
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation(value = "查询用户详情", notes = "查询用户详情")
    public UserDetailDto userDetail(Principal user) {
        String userName = user.getName();
        return userService.userDetail(userName);
    }

    /**
     * 根据用户编号查询权限可以查看的单位编号
     *
     * @return
     */
    @RequestMapping(value = "/getIdByToken", method = RequestMethod.POST)
    @ApiOperation(value = "根据用户编号查询权限可以查看的单位编号", notes = "根据用户编号查询权限可以查看的单位编号")
    public List<Object> getIdByToken(Principal user) {
        String userName = user.getName();
        QueryWrapper<UserEntity> qw = new QueryWrapper<>();
        qw = qw.eq("username", userName).select("user_id");
        UserEntity entity = userService.getOne(qw);
        return userService.getUnitIdByUser(entity.getUserId());
    }

    /**
     * 按单位编号查询用户编号
     *
     * @return
     */
    @RequestMapping(value = "/getUserIdByUnit", method = RequestMethod.POST)
    @ApiOperation(value = "按单位编号查询用户编号", notes = "按单位编号查询用户编号")
    public List<Long> getUserIdByUnit(@RequestBody Long unitId) {
        return userService.getUserIdByUnit(unitId);
    }

    /**
     * 用户列表
     *
     * @return
     */
    @RequestMapping(value = "/userList", method = RequestMethod.POST)
    @ApiOperation(value = "用户列表", notes = "用户列表")
    public List<Map<String, Object>> userList(@RequestBody List<Long> userIds) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.select("user_id", "username");
        wrapper.eq("is_freeze", 0);
        if (CollectionUtils.isNotEmpty(userIds)) {
            wrapper.notIn("user_id", userIds);
        }
        return userService.listMaps(wrapper);
    }


    /**
     * 查询用户详情
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/getUserByToken", method = RequestMethod.POST)
    @ApiOperation(value = "查询用户详情", notes = "查询用户详情")
    public Map<String, Object> getUserByToken(Principal user) {
        String userName = user.getName();
        QueryWrapper<UserEntity> qw = new QueryWrapper<>();
        qw = qw.eq("username", userName)
                .eq("is_freeze", 0)
                .select("user_id", "username","unit_id");
        return userService.getMap(qw);
    }

    /**
     * 根据权限Id查询对应的用户集合
     *
     * @param step
     * @return
     */
    @RequestMapping(value = "selectRepairByStep", method = RequestMethod.POST)
    @ApiOperation(value = "根据权限Id查询对应的用户集合", notes = "根据权限Id查询对应的用户集合")
    public Set selectRepairByStep(@RequestBody @NotNull(message = "维修步骤不能为空") Integer step) {
        return userService.selectRepairList(step);
    }

    /**
     * 删除用户（删除该用户所有权限）
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "deleteRepairById", method = RequestMethod.POST)
    @ApiOperation(value = "删除该用户所有权限", notes = "删除该用户所有权限")
    public boolean deleteRepairById(@RequestBody @Valid BasicVo<Long> basicVo) {
        return userService.deleteRepairById(basicVo.getId());
    }

    /**
     * 根据处置权限Id查询对应的用户集合
     *
     * @param step
     * @return
     */
    @RequestMapping(value = "selectHandleByStep", method = RequestMethod.POST)
    @ApiOperation(value = "根据处置权限Id查询对应的用户集合", notes = "根据处置权限Id查询对应的用户集合")
    public Set selectHandleByStep(@RequestBody @NotNull(message = "处置步骤不能为空") Integer step) {
        return userService.selectHandleByStep(step);
    }

    /**
     * 根据配置权限Id查询对应的用户集合
     *
     * @param step
     * @return
     */
    @RequestMapping(value = "selectConfigByStep", method = RequestMethod.POST)
    @ApiOperation(value = "根据配置权限Id查询对应的用户集合", notes = "根据配置权限Id查询对应的用户集合")
    public Set selectConfigByStep(@RequestBody @NotNull(message = "配置步骤不能为空") Integer step) {
        return userService.selectConfigByStep(step);
    }
}
