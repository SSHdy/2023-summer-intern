package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *单位信息输出字段
 * </p>
 *
 * @author mfl
 * @since 2018/12/13
 */
@Data
public class UnitDto {
    /**
     * 单位序号
     */
    @ApiModelProperty(value = "单位序号")
    private Long unitId;

    /**
     * 类型编号
     */
    @ApiModelProperty(value = "类型编号")
    private Integer unitType;

    /**
     * 单位编号
     */
    @ApiModelProperty(value = "单位编号")
    private String unitNumber;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;

    /**
     * 总人数
     */
    @ApiModelProperty(value = "总人数")
    private Integer unitTotle;

    /**
     * 单位类型名称
     */
    @ApiModelProperty(value = "单位类型名称")
    private String unitTypeName;
}
