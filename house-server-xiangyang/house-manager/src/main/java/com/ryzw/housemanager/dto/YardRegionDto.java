package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 *  院落区域列表
 * </p>
 *
 * @author mfl
 * @since 2019/6/4
 */
@Data
public class YardRegionDto {

    /**
     * 院落编号
     */
    private  Long yardId;


    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 院落位置
     */
    private String yardPosition;

    /**
     * 经度
     */
    private BigDecimal yardLongitude;

    /**
     * 纬度
     */
    private BigDecimal yardLatitude;

    /**
     * 区域名称
     */
    private Integer regionId;

    /**
     * 类型 1院落  2土地
     */
    private Integer type;

}
