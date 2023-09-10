package com.ryzw.housemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 * 维修类型列表
 * </p>
 *
 * @author zf
 * @since 2019/4/17
 */
@Data
@AllArgsConstructor
public class StepNodeTypeDto {

    /**
     * 枚举值
     */
    private Integer value;

    /**
     * 枚举键
     */
    private String key;

    /**
     * 维修类型
     */
    private Integer type;

    /**
     * 标记
     */
    private Integer flag;

}
