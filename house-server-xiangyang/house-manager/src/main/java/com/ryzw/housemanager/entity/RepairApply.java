package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 维修申请表
 * </p>
 *
 * @author mfl
 * @since 2019-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("repairApply")
public class RepairApply implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维修申请id
     */
    @TableId(value = "repair_apply_id", type = IdType.AUTO)
    private Long repairApplyId;

    /**
     * 申请编号
     */
    private String repairApplyNo;

    /**
     * 维修申请时间
     */
    private Date repairApplyDate;

    /**
     * 维修申请人
     */
    private String repairApplicant;

    /**
     * 维修申请人编号
     */
    private Long repairApplicantId;

    /**
     * 当前流程
     */
    private Integer step;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 维修总体进度
     */
    private Double repairProgress;

    /**
     * 申请状态（0未审核，1审核通过，2审核未通过）
     */
    private Integer applyStatus;

    /**
     * 维修类型(0 自费维修  1 零星维修 2机关管理局维修)
     */
    private Integer repairType;

    /**
     * 项目名称
     */
    private String repairProject;

    /**
     * 决算金额
     */
    private BigDecimal finalSum;

    /**
     * 设计单位
     */
    private String designUnit;

    /**
     * 预算单位
     */
    private String auditUnit;

    /**
     * 预算总价
     */
    private BigDecimal totalBudgetPrice;

    /**
     * 评审单位
     */
    private String accreditationUnit;

    /**
     * 规划许可证
     */
    private String planningPermit;

    /**
     * 维修概算
     */
    private BigDecimal budgetEstimate;

    /**
     * 单位面积价格
     */
    private BigDecimal unitAreaPrice;

    /**
     * 施工单位名称
     */
    private String repairUnitName;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 负责人
     */
    private String responsiblePerson;

    /**
     * 竣工时间
     */
    private Date completionTime;

    /**
     * 申请单位Id
     */
    private Long unitId;

    /**
     * 内控审批单号
     */
    private String sano;


}
