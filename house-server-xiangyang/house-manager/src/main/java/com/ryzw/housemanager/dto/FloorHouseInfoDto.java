package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *查询一层楼的房间信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/7/8
 */
@Data
public class FloorHouseInfoDto {
    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 楼层
     */
    private Integer floorName;

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 是否在使用（1在使用，0未使用）
     */
    private Integer isUse;

    /**
     * 是否闲置(0闲置 1非闲置)
     */
    private Integer isIdle;
}
