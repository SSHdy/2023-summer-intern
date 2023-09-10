package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *专项巡查信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/10/8
 */
@Data
public class SpecialDetailDto {
    /**
     * 专项巡检编号
     */
    private Long specialInspectionId;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 被检查单位id
     */
    private String unitName;

    /**
     * 院落id
     */
    private Long yardId;

    /**
     * 被检查院落id
     */
    private String yardName;

    /**
     * 楼栋id
     */
    private Long buildId;

    /**
     * 被检查楼栋
     */
    private String buildName;

    /**
     * 检查人id
     */
    private Long specialInspectionUserId;

    /**
     * 检查人姓名
     */
    private String specialInspectionUserName;

    /**
     * 检查时间
     */
    private String specialInspectionTime;

    /**
     * 检查结果
     */
    private String specialInspectionResult;

    /**
     * 是否发现问题（0未发现 1发现）
     */
    private Integer specialInspectionProblem;

    /**
     * 整改意见
     */
    private String specialInspectionRectifyComments;

    /**
     * 整改时间
     */
    private String specialInspectionRectifyTime;

    /**
     * 所扣分值
     */
    private Float specialInspectionPoints;

    /**
     * 整改状态（0未完成 1完成）
     */
    private Integer specialInspectionRectifyState;

    /**
     * 整改结果
     */
    private String specialInspectionRectifyResult;
}
