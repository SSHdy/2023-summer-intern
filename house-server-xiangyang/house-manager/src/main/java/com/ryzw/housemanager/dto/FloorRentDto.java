package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *楼层中的房间
 * </p>
 *
 * @author zf
 * @since 2019/3/25
 */
@Data
public class FloorRentDto {

    /**
     * 房间集合
     */
    private List<HouseRentDto> houseNameList;

    /**
     * 楼层
     */
    private Integer floorName;
}
