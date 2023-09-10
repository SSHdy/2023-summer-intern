package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *人工分房，新单位信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/6/28
 */
@Data
public class NewUnitListDto {
    /**
     * 单位序号
     */
    @ApiModelProperty(value = "单位序号")
    private Long newUnitId;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String newUnitName;
}
