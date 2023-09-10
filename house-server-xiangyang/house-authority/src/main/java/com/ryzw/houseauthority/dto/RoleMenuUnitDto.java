package com.ryzw.houseauthority.dto;

import com.ryzw.houseauthority.entity.Menu;
import com.ryzw.houseauthority.entity.Role;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色菜单单位信息
 * </p>
 *
 * @author lyx
 * @since 2018/12/29
 */
@Data
public class RoleMenuUnitDto {
    /**
     * 角色编号
     */
    private Role role;

    /**
     *  角色对应的菜单权限
     */
    private List<Menu> menuList;

    /**
     * 角色对应的单位权限
     */
    private List<Map<Object,Object>> unitList;

    /**
     * 菜单编号
     */
    private List<Long> menuIdList;

    /**
     * 菜单角色编号
     */
    private List<Long> roleMenuIdList;

    /**
     * 单位序号
     */
    private List<Long> unitIdList;

    /**
     * 角色单位编号
     */
    private List<Long> roleUnitIdList;
}
