package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2019/6/24
 */
@Data
public class NewUnitDto {
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
     * 配置方案数据单位编号
     */
    private List<Long> configurePlanNewUnitId;
}
