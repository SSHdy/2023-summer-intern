package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 专项巡检表
 * </p>
 *
 * @author mfl
 * @since 2019-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SpecialInspection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 专项巡检编号
     */
    @TableId(value = "special_inspection_id", type = IdType.AUTO)
    private Long specialInspectionId;

    /**
     * 被检查单位id
     */
    private Long specialInspectionUnitId;

    /**
     * 被检查院落id
     */
    private Long specialInspectionYardId;

    /**
     * 被检查楼栋id
     */
    private Long specialInspectionBuildId;

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
