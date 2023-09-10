package com.ryzw.houseauthority.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.houseauthority.dto.RoleDetailDto;
import com.ryzw.houseauthority.dto.RoleMenuUnitDto;
import com.ryzw.houseauthority.entity.Role;
import com.ryzw.houseauthority.entity.RoleUnit;
import com.ryzw.houseauthority.exception.ServiceException;
import com.ryzw.houseauthority.service.IRoleService;
import com.ryzw.houseauthority.service.IRoleUnitService;
import com.ryzw.houseauthority.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.util.Strings;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yz
 * @since 2018-12-27
 */
@RestController
@RequestMapping("/role")
@Api(value = "角色管理", description = "角色管理")
public class RoleController {
    @Autowired
    IRoleService roleService;

    @Autowired
    DozerBeanMapper dozerBeanMapper;

    @Autowired
    IRoleUnitService roleUnitService;

    /**
     * 查询角色
     *
     * @param roleListVo
     * @return
     */
    @RequestMapping(value = "roleList", method = RequestMethod.POST)
    @ApiOperation(value = "查询角色", notes = "查询角色")
    public IPage<Role> roleList(@RequestBody RoleListVo roleListVo) {
        Page<Role> page = new Page<>(roleListVo.getCurrentPage(), roleListVo.getPageSize());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        if (Strings.isNotBlank(roleListVo.getRoleName())) {
            queryWrapper.like("role_name", roleListVo.getRoleName());
        }
        queryWrapper.orderByDesc("role_id");
        return roleService.page(page, queryWrapper);
    }

    /**
     * 查询角色所有
     *
     * @return
     */
    @RequestMapping(value = "roles", method = RequestMethod.POST)
    @ApiOperation(value = "查询角色", notes = "查询角色")
    public List<Role> roles() {
        return roleService.list(new QueryWrapper<>());
    }

    /**
     * 角色详情
     *
     * @param roleMenuUnitVo
     * @return
     */
    @RequestMapping(value = "selectRoleMenuUnit", method = RequestMethod.POST)
    @ApiOperation(value = "角色详情", notes = "角色详情")
    public RoleMenuUnitDto selectRoleMenuUnit(@RequestBody RoleMenuUnitVo roleMenuUnitVo) {
        return roleService.selectRoleMenuUnit(roleMenuUnitVo);
    }


    /**
     * 用于删除角色
     *
     * @param role
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ApiOperation(value = "用于删除角色", notes = "用于删除角色")
    public boolean delete(@RequestBody Role role) {
        return roleService.deleteNotUser(role.getRoleId());
    }


    /**
     * 用于单独操作冻结或者启用
     *
     * @param roleVo
     * @return
     */
    @RequestMapping(value = "isFreeze", method = RequestMethod.POST)
    @ApiOperation(value = "用于单独操作冻结或者启用", notes = "用于单独操作冻结或者启用")
    public boolean isFreeze(@RequestBody RoleVo roleVo) {
        Role role = dozerBeanMapper.map(roleVo, Role.class);
        return roleService.isFreeze(role);
    }

    /**
     * 用于判断角色名称是否重复
     *
     * @param roleVo
     * @return
     */
    @RequestMapping(value = "unique", method = RequestMethod.POST)
    @ApiOperation(value = "用于判断角色名称是否重复", notes = "用于判断角色名称是否重复")
    public boolean unique(@RequestBody RoleVo roleVo) {
        Integer roleId = roleVo.getRoleId();
        QueryWrapper<Role> qw = new QueryWrapper<>();
        qw = qw.eq("role_name", roleVo.getRoleName());
        int count = (roleId == null) ? roleService.count(qw) : roleService.count(qw.ne("role_id", roleId));
        if (count > 0) {
            throw new ServiceException("角色名称重复");
        }
        return true;
    }

    /**
     * 添加角色，并添加单位和菜单权限
     *
     * @param roleUnitMenuVo
     * @return
     */
    @RequestMapping(value = "addRole", method = RequestMethod.POST)
    @ApiOperation(value = "添加角色，并添加单位和菜单权限", notes = "添加角色，并添加单位和菜单权限")
    public boolean addRole(@RequestBody RoleUnitMenuVo roleUnitMenuVo) {
        return roleService.addRole(roleUnitMenuVo);
    }

    /**
     * 修改角色，并修改单位和菜单权限
     *
     * @param roleUnitMenuVo
     * @return
     */
    @RequestMapping(value = "updateRole", method = RequestMethod.POST)
    @ApiOperation(value = "修改角色，并修改单位和菜单权限", notes = "修改角色，并修改单位和菜单权限")
    public boolean updateRole(@RequestBody RoleUnitMenuVo roleUnitMenuVo) {
        return roleService.updateRole(roleUnitMenuVo);
    }

    /**
     * 角色信息详情查询
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "roleDetail", method = RequestMethod.POST)
    @ApiOperation(value = "角色信息详情查询", notes = "角色信息详情查询")
    public RoleDetailDto roleDetail(@RequestBody BasicVo<Long> basicVo) {
        return roleService.roleDetail(basicVo);
    }


    /**
     * 角色信息详情查询
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "roleUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "角色信息详情查询", notes = "角色信息详情查询")
    public RoleDetailDto roleUpdate(@RequestBody BasicVo<Long> basicVo) {
        return roleService.roleUpdate(basicVo);
    }


    /**
     * 根据单位编号查询使用的角色数目
     *
     * @param unitId
     * @return
     */
    @RequestMapping(value = "selectCountByUnit", method = RequestMethod.POST)
    @ApiOperation(value = "根据单位编号查询使用的角色数目", notes = "根据单位编号查询使用的角色数目")
    public int selectCountByUnit(@RequestBody Long unitId) {
        QueryWrapper<RoleUnit> wrapper = new QueryWrapper<>();
        wrapper.eq("unit_id", unitId);
        return roleUnitService.count(wrapper);
    }
}
