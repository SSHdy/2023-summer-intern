package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *维修项目详情
 * </p>
 *
 * @author mfl
 * @since 2019/4/9
 */
@Data
public class RepairProjectDto {
    /**
     * 项目名称
     */
    private String repairProject;

    /**
     * 决算金额
     */
    private BigDecimal finalSum;

    /**
     * 维修面积
     */
    private Double repairModuleArea;

    /**
     * 维修申请人
     */
    private String repairApplicant;

    /**
     * 维修申请时间
     */
    private Date repairApplyDate;

    /**
     * 单位名称
     */
    private List<String> unitName;

    /**
     * 设计单位
     */
    private String designUnit;

    /**
     * 审计单位
     */
    private String auditUnit;

    /**
     * 评审单位
     */
    private String accreditationUnit;

    /**
     * 规划许可证
     */
    private String planningPermit;

}
