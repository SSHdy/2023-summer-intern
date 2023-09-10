package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *位置集合
 * </p>
 *
 * @author mfl
 * @since 2019/7/23
 */
@Data
public class PositionDto {
    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 楼层
     */
    private Integer floorName;

}
