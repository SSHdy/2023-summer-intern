package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 付款节点
 * </p>
 *
 * @author yz
 * @since 2019/4/23
 */
@Data
public class RepairPaymentNodeVo {
    /**
     * 付款进度
     */
    private String repairPaymentProgress;

    /**
     * 付款比例
     */
    private Double repairPaymentRatio;

    /**
     * 付款金额
     */
    @NotNull(message = "付款金额不能为空")
    private BigDecimal repairPaymentMoney;

    /**
     * 付款阶段
     */
    @NotNull(message = "付款阶段不能为空")
    private String stepLabel;

    /**
     * 阶段目标
     */
    private String stepTarget;

    /**
     * 备注说明
     */
    private String stepDescription;
}
