package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 返回配置申请数据
 * </p>
 *
 * @author zf
 * @since 2019/6/4
 */
@Data
public class ConfigureDto {

    /**
     * 申请单位名称
     */
    private String unitName;

    /**
     * 配置编号
     */
    private String configureNo;

    /**
     * 单位负责人
     */
    private String unitCharger;

    /**
     * 编制人数
     */
    private Integer unitComplement;

    /**
     * 实有人数
     */
    private Integer actualNumber;

    /**
     * 现有面积(总面积)
     */
    private Float totalArea;

    /**
     * 人均面积
     */
    private Float perArea;

    /**
     * 配置申请编号
     */
    private Long configureId;

    /**
     * 申请状态(0未审核,1审核通过,2审核未通过,3审核中)
     */
    private Integer applyStatus;

    /**
     * 配置申请时间
     */
    private Date configureDate;

    /**
     * 配置申请人
     */
    private String configureApplicant;

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
     * 分配方案(1调剂 2置换 3租用 4建设)
     */
    private Integer allocationPlan;

}
