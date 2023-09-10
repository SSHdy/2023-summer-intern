package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 * 闲置用房信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/3/13
 */
@Data
public class SpareRoomDto {
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
     * 套内面积
     */
    private Float houseUseArea;

    /**
     * 建筑面积
     */
    private Float houseBuildArea;

    /**
     * 房间类型
     */
    private String housetypeName;

    /**
     * 是否用建筑面积   0否  1是
     */
    private Integer useBuildArea;

    /**
     * 楼栋系数
     */
    private Double buildCoe;

    /**
     * 单位名称
     */
    private String unitName;
}
