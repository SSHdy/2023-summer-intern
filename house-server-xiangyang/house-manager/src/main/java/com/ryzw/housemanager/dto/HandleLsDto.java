package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *查询处置列表（不包含出租出借）
 * </p>
 *
 * @author mfl
 * @since 2019/7/19
 */
@Data
public class HandleLsDto {
    /**
     * 处置申请编号
     */
    private Long handleId;

    /**
     * 申请原因
     */
    private String applyReason;

    /**
     * 申请状态(0未审核，1审核通过，2审核未通过，3审核中)
     */
    private Integer applyStatus;

    /**
     * 处置申请时间
     */
    private Date handleDate;

    /**
     * 处置申请人
     */
    private String handleApplicant;

    /**
     * 处置方式(1转换用途 2出租 3拍卖 4 拆除)
     */
    private Integer handleWay;

    /**
     * 申请单位编号
     */
    private Long unitId;

    /**
     * 申请单位名称
     */
    private String unitName;

    /**
     * 处置位置集合
     */
    private List<PositionDto> PositionDto;


    /**
     * 处置是否到房间 0否 1是
     */
    private Integer isHouse;

}
