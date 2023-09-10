package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *院落输出信息
 * </p>
 *
 * @author mfl
 * @since 2019/1/3
 */
@Data
public class UnitYardDto {
    /**
     * 院落ID
     */
    private Long yardId;

    /**
     * 单位序号
     */
    @ApiModelProperty(value = "单位序号")
    private Long unitId;

    /**
     * 院落名称
     */
    private String yardName;

}
