package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *房屋面积超标信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/1/18
 */
@Data
public class HouseProofDto {
    /**
     * 房间编号
     */
    private String houseId;

    /**
     *
     * 院落名称
     */
    private String yardName;

    /**
     * 院落位置
     */
    private String yardPosition;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 楼层
     */
    private Integer floorName;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 房间使用人数
     */
    private Integer roomNum;

    /**
     * 规定面积
     */
    private Float specifiedArea;

    /**
     * 建筑面积
     */
    private Float houseBuildArea;

    /**
     * 使用面积
     */
    private Float houseUseArea;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private List<UnitReportDto> unitNames;

    /**
     * 房屋类型名称
     */
    @ApiModelProperty(value = "房屋类型名称")
    private String housetypeName;

    /**
     * 楼栋系数
     */
    private Double buildCoe;

    /**
     * 使用人姓名
     */
    private String leadingOfficeName;

    /**
     * 级别
     */
    private String professionalName;
}
