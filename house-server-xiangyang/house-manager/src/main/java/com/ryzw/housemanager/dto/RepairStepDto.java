package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 输出给前端的维修类型列表
 * </p>
 *
 * @author zf
 * @since 2019/5/10
 */
@Data
public class RepairStepDto {

    /**
     * 流程与名称对象集合
     */
    private List<RepairTypeDto> repairTypeDtoList;

    /**
     * 发改委审批状态 0不显示  1显示
     */
    private Integer state;
}
