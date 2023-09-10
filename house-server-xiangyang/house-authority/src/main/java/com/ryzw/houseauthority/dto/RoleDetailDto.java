package com.ryzw.houseauthority.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *菜单详情信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/3/27
 */
@Data
public class RoleDetailDto {
    /**
     * 角色编号
     */
    private Integer roleId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 是否冻结
     */
    private Integer isFreeze;

    /**
     * 是否订阅消息
     */
    private Integer isSubscibe;

    /**
     * roleValue
     */
    private String roleValue;

    /**
     * 菜单信息输出
     */
    private List<MenuDetailDto> menuDetail;

    /**
     * 单位编号
     */
    private List<Long> unitId;


    /**
     * 单位集合
     */
    private List<Map<Object,Object>> unitList;

    /**
     * 标题（0对内 1对外）
     */
    private Integer roleBanner;
}
