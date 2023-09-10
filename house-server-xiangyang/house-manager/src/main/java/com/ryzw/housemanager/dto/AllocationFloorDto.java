package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *按照楼层查询人工分房的房间信息字段输出
 * </p>
 *
 * @author mfl
 * @since 2019/7/9
 */
@Data
public class AllocationFloorDto {
    /**
     * 房间Id
     */
    private String houseId;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 房间使用面积
     */
    private Float houseUseArea;

    /**
     * 房屋类型名称
     */
    private String housetypeName;

    /**
     * 是否使用
     */
    private Integer isUse;

    /**
     * 租赁id
     */
    private Long rent;

    /**
     * 方案编号
     */
    private Long planId;

    /**
     * 是否闲置(0闲置 1非闲置)
     */
    private Integer isIdle;

    /**
     * 申请状态(0未审核，1审核通过，2审核未通过，3审核中)
     */
    private Integer applyStatus;

    /**
     * 处置方式(1转换用途 2出租 3拍卖 4 拆除 5不处置 6出借 7征收)
     */
    private Integer handleWay;

    /**
     * 原单位列表
     */
    private List<OriginalUnitDto> originalUnitList;

}
