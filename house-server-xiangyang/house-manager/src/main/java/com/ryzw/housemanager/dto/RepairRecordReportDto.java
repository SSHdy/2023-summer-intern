package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 维修报表信息
 * </p>
 *
 * @author zf
 * @since 2019/5/9
 */
@Data
public class RepairRecordReportDto {

    /**
     * 维修模块Id
     */
    private Long repairModuleId;

    /**
     * 维修申请Id
     */
    private Long repairApplyId;

    /**
     * 产权证号
     */
    private String warrantNum;

    /**
     * 开工时间
     */
    private Date startTime;

    /**
     * 竣工时间
     */
    private Date completionTime;

    /**
     * 地点字符串
     */
    private String positionStr;

    /**
     * 施工单位名称
     */
    private String repairUnitName;

    /**
     * 项目金额
     */
    private BigDecimal finalSum;

    /**
     * 维修部位名称
     */
    private String repairPartName;

    /**
     * 维修具体内容
     */
    private String repairModuleTarget;

    /**
     * 楼座
     */
    private String buildName;

    /**
     * 项目名称
     */
    private String repairProject;
}
