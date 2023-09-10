package com.ryzw.housemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 *流程结点列表信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/4/15
 */
@Data
@AllArgsConstructor
public class StepNodeDto {

    private Integer value;

    private String key;
}
