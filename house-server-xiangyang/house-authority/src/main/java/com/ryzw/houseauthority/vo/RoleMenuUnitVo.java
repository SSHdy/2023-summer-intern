package com.ryzw.houseauthority.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *  用于接收角色以及菜单和单位的信息
 * </p>
 *
 * @author lyx
 * @since 2018/12/29
 */
@Data
public class RoleMenuUnitVo {
    /**
     * 角色编号
     */
    @ApiModelProperty(value = "角色编号",example = "1")
    private Integer roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称",example = "超级管理员")
    private String roleName;

    /**
     * 是否冻结
     */
    @ApiModelProperty(value = "是否冻结",example = "0代表非冻结，1代表冻结")
    private Integer isFreeze;

    /**
     * 是否订阅消息
     */
    @ApiModelProperty(value = "是否订阅消息",example = "0,1")
    private Integer isSubscibe;

    /**
     * roleValue
     */
    @ApiModelProperty(value = "roleValue",example = "admin")
    private String roleValue;

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
