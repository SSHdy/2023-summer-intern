package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 查询维修档案列表
 * </p >
 *
 * @author zf
 * @since 2019/8/7
 */
@Data
public class RepairFilesDto {

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 院落Id
     */
    private Long yardId;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 楼座Id
     */
    private Long buildId;

    /**
     * 申请(维修)次数
     */
    private Integer repairCount;

    /**
     * 维修总面积
     */
    private BigDecimal repairArea;


}
