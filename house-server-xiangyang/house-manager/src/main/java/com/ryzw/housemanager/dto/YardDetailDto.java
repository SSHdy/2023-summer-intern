package com.ryzw.housemanager.dto;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;
import org.dozer.Mapping;

import java.math.BigDecimal;

/**
 * <p>
 *院落信息详情输出
 * </p>
 *
 * @author mfl
 * @since 2019/7/26
 */
@Data
public class YardDetailDto {
    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 院落名称
     */
    private String yardName;

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
     * 区域id
     */
    private Integer regionId;

    /**
     * 行政区划名称
     */
    private String yardAdministrativeRegion;

    /**
     * 类型 1院落  2土地
     */
    private Integer type;

//    /**
//     * 区域名称
//     */
//    private String regionName;
}
