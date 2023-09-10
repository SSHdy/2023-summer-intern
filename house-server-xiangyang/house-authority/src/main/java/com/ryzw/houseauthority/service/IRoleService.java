package com.ryzw.houseauthority.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.houseauthority.dto.RoleDetailDto;
import com.ryzw.houseauthority.dto.RoleMenuUnitDto;
import com.ryzw.houseauthority.entity.Role;
import com.ryzw.houseauthority.vo.BasicVo;
import com.ryzw.houseauthority.vo.RoleMenuUnitVo;
import com.ryzw.houseauthority.vo.RoleUnitMenuVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yz
 * @since 2018-12-27
 */
public interface IRoleService extends IService<Role> {
    /**
     * 如果该角色是否被用户使用，有就无法进行删除，没有被使用，才可删除
     * @param roleId
     * @return
     */
    boolean deleteNotUser(Integer roleId);


    /**
     * 查询角色以及勾选的菜单和单位权限的详情
     * @param roleMenuUnitVo
     * @return
     */
    RoleMenuUnitDto selectRoleMenuUnit(RoleMenuUnitVo roleMenuUnitVo);

    /**
     * 按用户编号查询用户角色信息
     * @param userId
     * @return
     */
    List<Role> getRoleByUserId(Long userId);

    /**
     * 添加角色，以及勾选的菜单和单位权限
     * @param roleUnitMenuVo
     * @return
     */
    boolean addRole(RoleUnitMenuVo roleUnitMenuVo);

    /**
     * 修改角色，并修改单位和菜单权限
     * @param roleUnitMenuVo
     * @return
     */
    boolean updateRole(RoleUnitMenuVo roleUnitMenuVo);

    /**
     * 角色信息详情查询
     * @param basicVo
     * @return
     */
    RoleDetailDto roleDetail(BasicVo<Long> basicVo);


    /**
     * 角色信息更新查询
     * @param basicVo
     * @return
     */
    RoleDetailDto roleUpdate(BasicVo<Long> basicVo);

    /**
     * 冻结用户
     * @param role
     * @return
     */
    boolean isFreeze(Role role) ;
}
