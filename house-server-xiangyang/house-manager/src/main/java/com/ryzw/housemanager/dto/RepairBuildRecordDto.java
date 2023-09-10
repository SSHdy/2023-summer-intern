package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 返回给前段的某楼栋相关的维修模块记录
 * </p>
 *
 * @author zf
 * @since 2019/4/11
 */
@Data
public class RepairBuildRecordDto {

    /**
     * 楼座Id
     */
    private Long buildId;

    /**
     * 维修模块详情集合
     */
    private List<RepairModuleDetailDto> repairModuleDetailDtoList;
}
