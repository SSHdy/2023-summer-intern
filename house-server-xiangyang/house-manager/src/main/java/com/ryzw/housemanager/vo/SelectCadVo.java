package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *查询cad需要的入参
 * </p>
 *
 * @author mfl
 * @since 2018/12/18
 */
@Data
public class SelectCadVo {

    /**
     * 楼座编号
     */
    @ApiModelProperty(value = "楼座编号")
    @NotNull(message = "楼栋id不能为空")
    private Long buildId;

    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层")
    private Integer floor;
}
