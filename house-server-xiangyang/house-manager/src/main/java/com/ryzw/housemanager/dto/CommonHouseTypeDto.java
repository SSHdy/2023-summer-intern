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
public class CommonHouseTypeDto {

    /**
     * 楼栋Id
     */
    private Long buildId;

    /**
     * 该楼栋下某类型的房间使用面积总和
     */
    private BigDecimal houseUseAreaSum;

    /**
     * 某类型房间数量
     */
    private Integer houseNum;


}
