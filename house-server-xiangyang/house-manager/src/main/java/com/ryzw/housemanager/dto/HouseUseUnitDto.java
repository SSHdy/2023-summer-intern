package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * </p>
 *
 * @author wj
 * @since 2019-07-24 0024
 */
@Data
public class HouseUseUnitDto {

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号",example = "1")
    private Long useunitId;

    /**
     * 单位序号
     */
    @ApiModelProperty(value = "单位序号",example = "1")
    private Long unitId;
}


