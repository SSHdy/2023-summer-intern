package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配置表
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Configure implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置申请编号
     */
    @TableId(value = "configure_id", type = IdType.AUTO)
    private Long configureId;

    /**
     * 配置编号
     */
    private String configureNo;

    /**
     * 配置申请时间
     */
    private Date configureDate;

    /**
     * 配置申请人
     */
    private String configureApplicant;

    /**
     * 配置申请人编号
     */
    private Long configureApplicantId;

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
     * 申请状态(0未审核,1审核通过,2审核未通过,3审核中)
     */
    private Integer applyStatus;

    /**
     * 申请单位名称
     */
    private String unitName;

    /**
     * 申请原因
     */
    private String applyReason;

    /**
     * 分配方案(1调剂 2置换 3租用 4建设)
     */
    private Integer allocationPlan;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 单位性质
     */
    private String unitProperty;

    /**
     * 单位地址
     */
    private String unitAddress;

    /**
     * 选择方案(1智能分配 2人工分配)
     */
    private Integer selectionPlan;

    /**
     * 重新分配单位编号
     */
    private Long redistributionUnitId;

    /**
     * 电梯台数
     */
    private Integer elevatorsNumber;

    /**
     * 单位编号
     */
    private Integer unitId;

    /**
     * 方案编号
     */
    private Long planId;


}
