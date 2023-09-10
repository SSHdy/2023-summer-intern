package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *分配单位信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/6/19
 */
@Data
public class AllocationUnitDto {
    /**
     * 房间id
     */
    private  String houseId;

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
     * 配置方案数据编号
     */
    private Long configurePlanDataId;

    /**
     * 配置申请编号
     */
    private Long configureId;

    /**
     * 方案编号
     */
    private Long planId;

    /**
     * 方案类型
     */
    private Integer planType;

    /**
     * 是否闲置(0闲置 1非闲置)
     */
    private Integer isIdle;

    /**
     * 租赁id
     */
    private Long rent;

    /**
     * 添加时的原单位列表
     */
    private List<UnitReportDto> quondamUnitList;

    /**
     * 通过配置查询的原单位列表
     */
    private List<OldUnitDto> oldUnitList;

    /**
     * 新单位列表
     */
    private List<UnitReportDto> newUnitList;

}
