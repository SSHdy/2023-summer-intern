package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author lyx
 * @since 2019/1/18
 */
@Data
public class NoOfficeDto {
    /**
     * 类型编号
     */
    @ApiModelProperty(value = "类型编号")
    private Integer unitType;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;

    /**
     * 院落编号
     */
    @ApiModelProperty(value ="院落编号",example = "1")
    private Long yardId;

    /**
     * 院落名称
     */
    @ApiModelProperty(value ="院落名称",example = "a座")
    private String yardName;

    /**
     * 楼座编号
     */
    @ApiModelProperty(value = "楼座编号",example = "1")
    private Long buildId;

    /**
     * 楼座名称
     */
    @ApiModelProperty(value = "楼座名称",example = "11号楼")
    private String buildName;

    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层",example = "1")
    private Integer floorName;

    /**
     * 房间号
     */
    @ApiModelProperty(value = "房间号",example = "1")
    private String houseNumber;

    /**
     * 使用面积
     */
    @ApiModelProperty(value = "使用面积",example = "70")
    private Float houseUseArea;

    /**
     * 建筑面积
     */
    @ApiModelProperty(value = "建筑面积",example = "150")
    private Float houseBuildArea;

    /**
     * 房屋类型编号
     */
    @ApiModelProperty(value = "房屋类型编号")
    private Integer housetypeId;

    /**
     * 房屋类型名称
     */
    @ApiModelProperty(value = "房屋类型名称")
    private String housetypeName;
}
