package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *     配置新使用单位
 * </p>
 *
 * @author wj
 * @since 2019-06-27 0027
 */
@Data
public class ConfigureNewUnitHttpDto {

    /**
     * 配置方案数据单位编号
     */
    private Long configurePlanNewUnitId;

    /**
     * 新使用单位
     */
    private String newUseUnitName;

}
