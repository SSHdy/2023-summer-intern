package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2019/7/23
 */
@Data
public class UnitExcessDto {
    /**
     * 单位序号
     */
    @ApiModelProperty(value = "单位序号")
    private Long unitId;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;

    /**
     * 领导办公室
     */
    private LeaderExcessDto LeaderExcessDto;

    /**
     * 普通人员办公室
     */
    private CommonExcessDto CommonExcessDto;

}
