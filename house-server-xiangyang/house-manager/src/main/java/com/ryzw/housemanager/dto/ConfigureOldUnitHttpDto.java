package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *     配置原使用单位
 * </p>
 *
 * @author wj
 * @since 2019-06-27 0027
 */
@Data
public class ConfigureOldUnitHttpDto {

    /**
     * 配置方案数据单位编号
     */
    private Long configurePlanOldUnitId;


    /**
     * 原使用单位
     */
    private String oldUseUnitName;

}
