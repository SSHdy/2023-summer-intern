package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.entity.Build;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lyx
 * @since 2019/1/14
 */
@Data
public class YardBuildDto {
    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 院落经度
     */
    private BigDecimal yardLongitude;

    /**
     * 院落纬度
     */
    private BigDecimal yardLatitude;


    /**
     * 院落对应的build数组
     */
    private List<Build> buildList;

}
