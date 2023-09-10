package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 房间明细汇总数据--->面积明细数据
 * </p >
 *
 * @author zf
 * @since 2020.4.9
 */
@Data
public class AreaDetailDto {

    /**
     * 办公地点
     */
    private String officeLocation;

    /**
     * 建筑面积
     */
    private BigDecimal buildArea;

    /**
     * 基本办公用房使用面积
     */
    private BigDecimal basicOfficeArea;

    /**
     * 附属用房
     */
    private Float subsidiaryArea;

    /**
     * 业务用房
     */
    private Float businessArea;

    /**
     * 出租
     */
    private Float rentArea;

    /**
     * 空置
     */
    private Float spareArea;

    /**
     * 出借
     */
    private Float lendArea;





}
