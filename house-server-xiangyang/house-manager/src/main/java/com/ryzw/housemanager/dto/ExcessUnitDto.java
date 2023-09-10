package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 单位超标信息查询输出
 * </p>
 *
 * @author zf
 * @since 2019/12/25
 */
@Data
public class ExcessUnitDto {

    /**
     * 序号
     */
    private String number;

    /**
     * 单位Id
     */
    private Long unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 总建筑面积
     */
    private BigDecimal buildAreaSum;

    /**
     * 总使用面积
     */
    private BigDecimal houseUseAreaSum;

    /**
     * 附属用房建筑面积
     */
    private BigDecimal fuShuAraeSum;

    /**
     * 基本办公用房使用面积
     */
    private BigDecimal jiBenUseArea;

    /**
     * 基本办公用核定面积
     */
    private BigDecimal jiBenStandardArea;

    /**
     * 基本办公用房超标面积
     */
    private BigDecimal jiBenExcessArea;

    /**
     * 基本办公用房超标比例
     */
    private BigDecimal jiBenExcessRatio;

    /**
     * 办公室用房使用面积
     */
    private BigDecimal useAreaByBanGong;

    /**
     * 办公室用房核定面积
     */
    private BigDecimal standardAreaByBanGong;

    /**
     * 办公室用房超标面积
     */
    private BigDecimal excessAreaByBanGong;

    /**
     * 办公室用房超标比列
     */
    private BigDecimal excessRatioByBanGong;

    /**
     * 服务用房使用面积
     */
    private BigDecimal useAreaByFuWu;

    /**
     * 服务用房核定面积
     */
    private BigDecimal standardAreaByFuWu;

    /**
     * 服务用房超标面积
     */
    private BigDecimal excessAreaByFuWu;

    /**
     * 服务用房超标比列
     */
    private BigDecimal excessRatioByFuWu;

    /**
     * 设备用房使用面积
     */
    private BigDecimal useAreaBySheBei;

    /**
     * 设备用房核定面积
     */
    private BigDecimal standardAreaBySheBei;

    /**
     * 设备用房超标面积
     */
    private BigDecimal excessAreaBySheBei;

    /**
     * 设备用房超标比列
     */
    private BigDecimal excessRatioBySheBei;


}
