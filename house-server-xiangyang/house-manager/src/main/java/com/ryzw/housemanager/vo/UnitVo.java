package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author yz
 * @since 2018/12/14
 */
@Data
public class UnitVo extends PageVo {
    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;

    /**
     * 类型编号
     */
    @ApiModelProperty(value = "类型编号")
    private Integer unitType;
}
