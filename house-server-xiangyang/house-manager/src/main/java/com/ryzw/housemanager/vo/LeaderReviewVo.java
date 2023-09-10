package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 领导查看参数
 * </p>
 *
 * @author zf
 * @since 2018/4/17
 */
@Data
public class LeaderReviewVo {

    /**
     * 当前流程编号
     */
    @NotNull(message = "当前流程编号不能为空")
    private Integer step;

    /**
     * 申请Id
     */
    @NotNull(message = "申请Id不能为空")
    private Long repairApplyId;

}
