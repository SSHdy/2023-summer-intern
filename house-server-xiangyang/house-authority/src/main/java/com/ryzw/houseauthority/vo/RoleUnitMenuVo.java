package com.ryzw.houseauthority.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *角色信息的添加修改，前端需要传入的参数
 * </p>
 *
 * @author mfl
 * @since 2019/3/27
 */
@Data
public class RoleUnitMenuVo {
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
     * 标题（0对内 1对外）
     */
    @ApiModelProperty(value = "标题（0对内 1对外）",example = "0")
    private Integer roleBanner;

    /**
     * 菜单编号
     */
    private List<Long> menuId;

    /**
     * 单位编号
     */
    private List<Long> unitId;
}
