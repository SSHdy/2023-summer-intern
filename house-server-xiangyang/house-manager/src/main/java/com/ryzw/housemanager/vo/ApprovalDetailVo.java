package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 查询某个用户审批记录详情
 * </p>
 *
 * @author zf
 * @since 2019/5/30
 */
@Data
public class ApprovalDetailVo {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 维修申请Id
     */
    @NotNull(message = "维修申请Id不能为空")
    @ApiModelProperty(value = "维修申请Id")
    private Long repairApplyId;
}


