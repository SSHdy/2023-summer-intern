package com.ryzw.housemanager.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *     维修工作统计实体类
 * </p>
 *
 * @author wj
 * @since 2019-07-24 0024
 */
@Data
public class RepairWorkStatistics {


    /**
     * 审批数量
     */
    private Long auditCount;

    /**
     * 维修面积
     */
    private BigDecimal repairModuleArea;

    /**
     * 付款金额
     */
    private BigDecimal repairPaymentMoney;

    /**
     * 审批数量同比
     */
    private Long auditCountTb;

    /**
     * 维修面积同比
     */
    private BigDecimal repairModuleAreaTb;

    /**
     * 付款金额同比
     */
    private BigDecimal repairPaymentMoneyTb;

    /**
     * 审批数量环比
     */
    private Long auditCountHb;

    /**
     * 维修面积环比
     */
    private BigDecimal repairModuleAreaHb;

    /**
     * 付款金额环比
     */
    private BigDecimal repairPaymentMoneyHb;

    /**
     * 维修类型
     */
    private Integer repairType;


}
