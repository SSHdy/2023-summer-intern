package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 房屋类型报表信息输出
 * </p>
 *
 * @author zf
 * @since 2019/12/10
 */
@Data
public class HouseTypeDto {

    /**
     * 房屋类型名称
     */
    private String houseTypeName;

    /**
     * 是否使用建筑面积 0否 ,1是
     */
    private Integer useBuildArea;

    /**
     * 某类型房间数量
     */
    private Integer houseNum;

    /**
     * 某类型房屋建筑面积总和
     */
    private BigDecimal totalBuildArea;

    /**
     * 某类型房屋套内面积总和
     */
    private BigDecimal totalUseArea;

}
