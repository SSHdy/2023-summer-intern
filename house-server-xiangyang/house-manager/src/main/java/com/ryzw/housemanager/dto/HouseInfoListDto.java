package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *基础模块房屋信息详情输出
 * </p>
 *
 * @author mfl
 * @since 2019/1/17
 */
@Data
public class HouseInfoListDto {
    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 楼层
     */
    private Integer floorName;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 房屋类型名称
     */
    @ApiModelProperty(value = "房屋类型名称")
    private String housetypeName;

    /**
     * 建筑面积
     */
    private Float houseBuildArea;

    /**
     * 使用面积
     */
    private Float houseUseArea;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 院落位置
     */
    @ApiModelProperty(value ="院落位置")
    private String yardPosition;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;
}
