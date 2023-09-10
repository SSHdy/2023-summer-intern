package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 * 输出给前端的维修类型列表
 * </p>
 *
 * @author zf
 * @since 2019/4/17
 */
@Data
public class RepairTypeDto {

    /**
     * 流程编号
     */
    private Long step;

    /**
     * 流程名称
     */
    private String stepName;

    /**
     * 原始流程编号
     */
    private Integer stepValue;
}
