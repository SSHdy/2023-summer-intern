package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 返回给前段的维修审批详情
 * </p>
 *
 * @author zf
 * @since 2019/4/8
 */
@Data
public class RepairApprovalDetailDto {

    /**
     * 维修申请id
     */
    private Long repairApplyId;

    /**
     * 维修申请时间
     */
    private Date repairApplyDate;

    /**
     * 申请人
     */
    private String repairApplicant;

    /**
     * 负责人
     */
    private String responsiblePerson;

    /**
     * 施工单位名称
     */
    private String repairUnitName;

    /**
     * 使用单位集合
     */
    private List<String> unitNameList;

    /**
     * 维修概算
     */
    private BigDecimal budgetEstimate;

    /**
     * 单位面积价格
     */
    private BigDecimal unitAreaPrice;

    /**
     * 预算总价
     */
    private BigDecimal totalBudgetPrice;

    /**
     * 维修面积
     */
    private Double repairModuleArea;

    /**
     * 项目名称
     */
    private String repairProject;

    /**
     * 维修模块总面积
     */
    private Double totalArea;

    /**
     * 维修模块详情集合
     */
    List<RepairModuleDetailDto> repairModuleDetailDtoList;
}
