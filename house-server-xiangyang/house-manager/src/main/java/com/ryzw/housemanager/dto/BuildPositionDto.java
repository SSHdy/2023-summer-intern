package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 用户权限下的楼栋位置信息查询
 * </p>
 *
 * @author mfl
 * @since 2019/2/18
 */
@Data
public class BuildPositionDto {
    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 楼座名称或院落名称
     */
    private String buildName;

    /**
     * 楼层集合
     */
    private List<FloorNameDto> floorNameDtoList;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 楼座经度
     */
    private BigDecimal buildLongitude;

    /**
     * 楼座纬度
     */
    private BigDecimal buildLatitude;


    /**
     * 类型
     */
    private Integer type;
}
