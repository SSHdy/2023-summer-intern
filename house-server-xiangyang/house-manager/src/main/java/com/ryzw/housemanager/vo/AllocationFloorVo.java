package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *按照楼层查询人工分房的房间信息需要的参数
 * </p>
 *
 * @author mfl
 * @since 2019/7/9
 */
@Data
public class AllocationFloorVo {
    /**
     * 楼座编号
     */
    @ApiModelProperty(required = true,value = "楼座编号",example = "111")
    @NotNull(message = "楼座编号不能为空")
    private Long buildId;

    /**
     * 楼层
     */
    @ApiModelProperty(required = true,value = "楼层编号",example = "-3")
    @NotNull(message = "楼层编号不能为空")
    private Integer floorName;

    /**
     * 方案id
     */
    @ApiModelProperty(required = true,value = "方案id",example = "30")
    @NotNull(message = "方案id不能为空")
    private Long planId;
}
