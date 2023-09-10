package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 * 闲置用房树查询输出
 * </p>
 *
 * @author mfl
 * @since 2019/4/17
 */
@Data
public class SpareRoomTreeDto {
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
     * 楼层
     */
    private Integer floorName;

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 是否显示 true不显示 false显示
     */
    private String disabled;

}
