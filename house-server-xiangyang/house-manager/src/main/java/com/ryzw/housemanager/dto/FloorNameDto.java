package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * 楼层集合
 * </p >
 *
 * @author zf
 * @since 2018/7/4
 */
@Data
public class FloorNameDto {

    /**
     * 楼层
     */
    private Integer floorName;

    /**
     * 房间集合
     */
    private List<HouseNumDto> houseNumber;
}
