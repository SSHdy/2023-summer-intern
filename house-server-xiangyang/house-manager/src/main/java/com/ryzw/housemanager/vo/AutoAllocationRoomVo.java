package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 *自动分配房间，前端传入的参数
 * </p>
 *
 * @author mfl
 * @since 2019/6/10
 */
@Data
public class AutoAllocationRoomVo extends PageVo{
    /**
     *区域id
     */
    @ApiModelProperty(required = true,value = "区域id",example = "[3,4]")
    private List<Integer> regionId;

    /**
     * 电梯数量
     */
    @ApiModelProperty(required = true,value = "电梯数量",example = "3")
    private Integer elevatorsNumber;

    /**
     *申请单位id
     */
    @ApiModelProperty(required = true,value = "申请单位id",example = "236")
    private Long selfUnitId;

    /**
     *重新分配单位id
     */
    @ApiModelProperty(required = true,value = "重新分配单位id",example = "236")
    private Long otherUnitId;


    /**
     *房间最小面积
     */
    @ApiModelProperty(required = true,value = "房间最小面积",example = "20.0")
//    @NotNull(message = "房间最小面积不能为空")
    private Float roomAreaMin;

    /**
     *房间最大面积
     */
//    @NotNull(message = "房间最大面积")
    @ApiModelProperty(required = true,value = "房间最大面积不能为空",example = "40.0")
    private Float roomAreaMax;
}
