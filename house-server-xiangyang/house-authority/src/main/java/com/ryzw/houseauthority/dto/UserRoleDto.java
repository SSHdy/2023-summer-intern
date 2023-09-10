package com.ryzw.houseauthority.dto;

import com.ryzw.houseauthority.entity.Role;
import com.ryzw.houseauthority.entity.UserEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lyx
 * @since 2018/12/29
 */
@Data
public class UserRoleDto {
    /**
     * 用户编号
     */
    private UserEntity user;

    /**
     * 角色数组
     */
    private List<Role> roleList;

    /**
     * 用户角色编号
     */
    @ApiModelProperty(value = "用户角色编号",example = "[1,2]")
    private List<Long> userRoleIdList;

    /**
     * 维修编号
     */
    @ApiModelProperty(value = "维修编号",example = "[1,2]")
    private List<Long> repairIdList;

    /**
     * 用户角色编号
     */
    @ApiModelProperty(value = "用户角色编号",example = "[1,2]")
    private List<Long> userRepairIdList;

    /**
     * 处置流程编号
     */
    private List<Long> handleIdList;

    /**
     * 配置流程编号
     */
    private List<Long> configIdList;
}
