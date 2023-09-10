package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *自动分房条件
 * </p>
 *
 * @author mfl
 * @since 2019/6/10
 */
@Data
public class AutoConditionDto {
    /**
     * 区域id
     */
    private List<Integer> regionId;

    /**
     * 电梯数量
     */
    private Integer elevatorsNumber;

    /**
     * 重新分配单位编号
     */
    private Long redistributionUnitId;

    /**
     * 房间数量
     */
    private Integer roomNumber;

    /**
     * 房间面积最小值
     */
    private Float roomAreaMin;

    /**
     * 房间面积最大值
     */
    private Float roomAreaMax;

}
