package com.ryzw.houseauthority.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用于接收角色查询参数
 * </p>
 *
 * @author lyx
 * @since 2018/12/28
 */
@Data
public class RoleListVo extends PageVo{
    /**
     * 角色名称
     */
    @ApiModelProperty(value = "角色名称",example = "管理员")
    private String roleName;
}
