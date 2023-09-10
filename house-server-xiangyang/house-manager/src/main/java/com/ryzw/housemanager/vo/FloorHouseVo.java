package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2018/12/21
 */
@Data
public class FloorHouseVo {
    /**
     * 楼栋id
     */
    @ApiModelProperty(value = "楼栋id",example = "1")
    @NotNull(message = "楼栋id不能为空")
    private Long buildId;

    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层",example = "1")
    @NotNull(message = "楼层不能为空")
    private Integer floorName;
}
