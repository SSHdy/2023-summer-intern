package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *院落位置信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/5/6
 */
@Data
public class YardPositionInfoDto {
    /**
     * 院落位置
     */
    private String yardPosition;

    /**
     * 经度
     */
    private BigDecimal yardLongitude;

    /**
     * 纬度
     */
    private BigDecimal yardLatitude;
}
