package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *闲置用房楼层信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/3/13
 */
@Data
public class SpareFloorDto {
    /**
     * 楼层
     */
    private Integer floorName;

    /**
     * 房屋信息
     */
    private List<SpareHoDto> houseList;
}
