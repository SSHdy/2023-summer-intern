package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 建设项目列表
 * </p>
 *
 * @author zf
 * @since 2019/9/3
 */
@Data
public class ConstructHouseDto {

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 审批部门
     */
    private String approvalDepartment;

    /**
     * 建设单位
     */
    private String unitName;

    /**
     * 主要使用单位
     */
    private List<String> unitList;

    /**
     * 地址坐落
     */
    private String yardPosition;


    /**
     * 建设规模(平方米)
     */
    private Float buildArea;

    /**
     * 估算投资(万元)
     */
    private BigDecimal estimateInvestment;

    /**
     * 备注
     */
    private String remark;




}
