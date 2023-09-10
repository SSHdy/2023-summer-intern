package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *维修类型报表查询输出
 * </p>
 *
 * @author mfl
 * @since 2019/4/26
 */
@Data
public class RepairReportDto {
    /**
     * 维修类型(0 自费维修  1 零星维修 2机关管理局维修)
     */
    private Integer repairType;

    /**
     * 维修费用
     */
    private BigDecimal totalMoney;

    /**
     * 维修数量
     */
    private Integer num;

    /**
     * 维修面积
     */
    private Double totalArea;
}
