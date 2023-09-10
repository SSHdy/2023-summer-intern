package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 查询出租用房信息列表
 * </p>
 *
 * @author mfl
 * @since 2019-05-017
 */
@Data
public class OutRentDto {

    /**
     * 出租用房信息编号
     */
    private Long outRentId;

    /**
     * 所属办公楼
     */
    private Long buildId;

    /**
     * 办公楼名称
     */
    private String buildName;

    /**
     * 建筑面积
     */
    private Double coveredArea;

    /**
     * 租用单位
     */
    private Long rentUnit;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 租用开始时间
     */
    private Date startTime;

    /**
     * 租用截止时间
     */
    private Date endTime;

    /**
     * 租金标准
     */
    private BigDecimal rentMoney;

    /**
     * 财政备案或批复文号
     */
    private String financeRemark;
}
