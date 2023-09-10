package com.ryzw.houseauthority.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *  用于接收角色信息
 * </p>
 *
 * @author lyx
 * @since 2018/12/28
 */
@Data
public class RoleVo {
    /**
     * 角色编号
     */
    @ApiModelProperty(value = "角色编号",example = "1")
    private Integer roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称",example = "管理员")
    private String roleName;

    /**
     * 是否冻结
     */
    @ApiModelProperty(value = "是否冻结",example = "1")
    private Integer isFreeze;

    /**
     * 批量roleIds数组
     */
    @ApiModelProperty(value = "批量roleIds数组",example = "[]")
    private List<Integer> roleIdList;
}
