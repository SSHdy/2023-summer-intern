package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *用户权限下的院落位置信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/2/18
 */
@Data
public class YardPositionDto {
    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 院落经度
     */
    private BigDecimal yardLongitude;

    /**
     * 院落纬度
     */
    private BigDecimal yardLatitude;
}
