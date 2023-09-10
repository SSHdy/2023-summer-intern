package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *     房间明细输出
 * </p>
 *
 * @author mfl
 * @since 2019/7/24
 */
@Data
public class RoomDetailDto {
    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 单位名称
     */
    private String unitName;

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
     * 使用面积
     */
    private Float houseUseArea;

    /**
     * 是否为领导(0领导  1一般人员)
     */
    private Integer isLeading;

    /**
     * 房间使用人员
     */
    private String usePerson;

}
