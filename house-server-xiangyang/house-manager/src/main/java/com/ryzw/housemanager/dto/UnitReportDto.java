package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *单位报表输出
 * </p>
 *
 * @author mfl
 * @since 2019/1/18
 */
@Data
public class UnitReportDto {
    /**
     * 单位序号
     */
    @ApiModelProperty(value = "单位序号")
    private Long unitId;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;

    /**
     * 自用单位序号
     */
    @ApiModelProperty(value = "单位序号")
    private Long useunitId;

    /**
     * 配置方案数据单位编号
     */
    private Long configurePlanOldUnitId;

    /**
     * 配置方案数据单位编号
     */
    private Long configurePlanNewUnitId;
}
