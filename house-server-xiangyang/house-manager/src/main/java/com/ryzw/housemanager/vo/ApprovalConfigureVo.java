package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 查询某个用户审批配置记录详情
 * </p>
 *
 * @author zf
 * @since 2019/6/26
 */
@Data
public class ApprovalConfigureVo {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 配置申请Id
     */
    @NotNull(message = "配置申请Id不能为空")
    @ApiModelProperty(value = "配置申请Id")
    private Long configureId;
}


