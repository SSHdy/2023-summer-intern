package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 维修项目列表信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/4/3
 */
@Data
public class RepairProjectListDto {
    /**
     * 维修申请id
     */
    private Long repairApplyId;

    /**
     * 项目名称
     */
    private String repairProject;

    /**
     * 单位名称
     */
    private List<String> unitName;

    /**
     * 决算金额
     */
    private BigDecimal finalSum;

    /**
     * 设计单位
     */
    private String designUnit;

    /**
     * 维修申请时间
     */
    private Date repairApplyDate;

    /**
     * 维修总体进度
     */
    private Double repairProgress;

    /**
     * 维修类型(0 自费维修  1 零星维修 2机关管理局维修)
     */
    private Integer repairType;

    /**
     * 竣工时间
     */
    private Date completionTime;

}
