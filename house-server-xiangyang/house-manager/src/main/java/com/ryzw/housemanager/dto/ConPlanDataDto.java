package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 查询配置方案数据列表
 * </p>
 *
 * @author zf
 * @since 2019/6/11
 */
@Data
public class ConPlanDataDto {

    /**
     * 配置Id
     */
    private Long configureId;

    /**
     * 配置方案数据集合
     */
    private List<PlanDataDto> planDataDtoList;
}
