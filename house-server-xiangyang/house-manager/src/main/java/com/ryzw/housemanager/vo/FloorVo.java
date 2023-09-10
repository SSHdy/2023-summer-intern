package com.ryzw.housemanager.vo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FloorVo {
    /**
     * 院落
     */
    @ApiModelProperty(value = "院落",example = "1")
    private Long yardId;

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

    /**
     * 备案人
     */
    @ApiModelProperty(value = "备案人",example = "张三")
    @NotNull(message = "备案人不能为空")
    private String recordHolder;

    /**
     * 备案人id
     */
    @ApiModelProperty(value = "备案人id",example = "13")
    @NotNull(message = "备案人id不能为空")
    private Long recordHolderId;

    /**
     * 用于新增接收前台传过来的 UUID 数组
     */
    @ApiModelProperty(value = "UUID 数组",example = "[]")
    private List<String> addHouseIdList;

    /**
     * 接收修改房屋list
     */
    @ApiModelProperty(value = "修改房屋list",example = "[]")
    private List<HouseVo> houseVoList;

    /**
     *  用于删除接收前台传过来的 UUID 数组
     */
    @ApiModelProperty(value = "UUID 数组",example = "[]")
    private List<String> delHouseIdList;

    /**
     * 用于接收json图片的对象
     */
    @ApiModelProperty(value = "json图片",example = "****")
    private JsonPlanVo jsonPlanVo;

    /**
     * 选择回退的配置申请ID集合
     */
    private List<Long> configureIds;

    /**
     * 出租转自用房的房间ID集合
     */
    private List<String> rentHouseIds;
}
