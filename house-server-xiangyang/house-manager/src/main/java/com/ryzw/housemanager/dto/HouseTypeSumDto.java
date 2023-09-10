package com.ryzw.housemanager.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ryzw.housemanager.config.DoubleSerialize;
import lombok.Data;

/**
 * <p>
 *房屋类型报表数量统计相关
 * </p>
 *
 * @author mfl
 * @since 2019/4/18
 */
@Data
public class HouseTypeSumDto {

    /**
     * 房屋类型名称
     */
    private String houseTypeName;

    /**
     * 某类型房间数量
     */
    private Integer houseNum;

    /**
     * 某类型房屋建筑面积总和
     */
    @JsonSerialize(using = DoubleSerialize.class)
    private Float totalBuildArea;

    /**
     * 某类型房屋套内面积总和
     */
    @JsonSerialize(using = DoubleSerialize.class)
    private Float totalUseArea;
}
