package com.ryzw.housemanager.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *      处置工作统计实体类
 * </p>
 *
 * @author wj
 * @since 2019-07-26 0026
 */
@Data
public class HandleWorkStatistics {

    /**
     * 审批数量
     */
    private Long auditCount;

    /**
     * 处置面积
     */
    private Float handleArea;

    /**
     * 处置金额
     */
    private BigDecimal handleMoney;

    /**
     * 审批数量同比
     */
    private Long auditCountTb;

    /**
     * 处置面积同比
     */
    private Float handleAreaTb;

    /**
     * 处置金额同比
     */
    private BigDecimal handleMoneyTb;

    /**
     * 审批数量环比
     */
    private Long auditCountHb;

    /**
     * 处置面积环比
     */
    private Float handleAreaHb;

    /**
     * 处置金额环比
     */
    private BigDecimal handleMoneyHb;

    /**
     * 处置类型
     */
    private Integer handleType;

}
