package com.ryzw.housemanager.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ryzw.housemanager.config.DoubleSerialize;
import lombok.Data;

/**
 * <p>
 *市（州、盟）房屋类型报表查询信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/9/4
 */
@Data
public class YardTypeTwoDto {


    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 房屋类型id
     */
    private Integer houseTypeId;

    /**
     * 房屋类型名称
     */
    private String houseTypeName;

    /**
     * 某类型房屋建筑面积总和
     */
    @JsonSerialize(using = DoubleSerialize.class)
    private Double totalBuildArea;

    /**
     * 某类型房屋套内面积总和
     */
    @JsonSerialize(using = DoubleSerialize.class)
    private Double totalUseArea;
}
