package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2019/1/10
 */
@Data
public class AssetsComnVo extends PageVo{

    /**
     * 资产名称
     */
    @ApiModelProperty(value = "资产名称",example = "电动车")
    private String assetNameComn;

    /**
     * 资产编码
     */
    @ApiModelProperty(value = "资产编码",example = "2030900")
    private String categoryCodeComn;

    /**
     * 资产类别(1车辆卡片  2房屋卡片  3构筑物卡片   4家具、用具、装具卡片  5通用设备卡片  6专用设备)
     */
    private Integer assetsType;
}
