package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 配置工作量统计数据
 * </p>
 *
 * @author zf
 * @since 2019-07-25
 */
@Data
public class ConfigureStatisticsDto {

    /**
     * 统计名称:分配方案(1调剂 2置换 3租用 4建设 5汇总)
     */
    private Integer allocationPlan;

    /**
     * 审批数量
     */
    private Integer approvalNum;

    /**
     * 同比审批数量
     */
    private Integer approvalNumTb;

    /**
     * 环比审批数量
     */
    private Integer approvalNumHb;

    /**
     * 审批金额
     */
    private BigDecimal approvalMoney;

    /**
     * 同比审批金额
     */
    private BigDecimal approvalMoneyTb;

    /**
     * 环比审批金额
     */
    private BigDecimal approvalMoneyHb;

    /**
     * 审批面积
     */
    private BigDecimal approvalArea;

    /**
     * 同比审批面积
     */
    private BigDecimal approvalAreaTb;

    /**
     * 环比审批面积
     */
    private BigDecimal approvalAreaHb;

}
