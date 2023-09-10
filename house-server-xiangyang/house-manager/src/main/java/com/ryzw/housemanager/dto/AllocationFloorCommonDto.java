package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2019/7/9
 */
@Data
public class AllocationFloorCommonDto {

    /**
     * 房间Id
     */
    private String houseId;

    /**
     * 楼层房屋信息
     */
    AllocationFloorDto allocationUnit;

    /**
     * 通过配置查询的原单位列表
     */
    private List<OldConfUnitDto> oldConfUnitList;

    /**
     * 通过配置查询的新单位列表
     */
    private List<NewConfUnitDto> newConfUnitList;

    /**
     * 配置相关的信息
     */
    private AllocationConfDto allocationConf;
}
