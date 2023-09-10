package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author lyx
 * @since 2019/1/21
 */
@Data
public class UnitUsageDto {
    /**
     * 单位序号
     */
    private Long unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 编制人数
     */
    private Integer unitComplement;

    /**
     * 总人数
     */
    private String sumRoomNum;

    /**
     * 核定面积
     */
    private String checkArea;

    /**
     * 实有面积
     */
    private String actualArea;

    /**
     * 差额面积
     */
    private String minusArea;

    /**
     * 房屋类型编号
     */
    private Integer housetypeId;

    /**
     * 房屋类型名称
     */
    private String housetypeName;
}
