package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *分配房间信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/6/6
 */
@Data
public class AutoAllocationRoomDto {

    /**
     * 院落id
     */
    private Long yardId;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 楼栋id
     */
    private Long buildId;

    /**
     * 楼栋名称
     */
    private String buildName;

    /**
     * 房间id
     */
    private String houseId;

    /**
     * 楼层名称
     */
    private Integer floorName;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 房间使用面积
     */
    private Float houseUseArea;

    /**
     * 单位列表
     */
    private List<UnitReportDto> unitList;


}
