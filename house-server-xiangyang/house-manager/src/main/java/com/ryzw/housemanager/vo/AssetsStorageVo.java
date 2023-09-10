package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 资产入库查询条件
 * </p>
 *
 * @author yz
 * @since 2019/2/27
 */
@Data
public class AssetsStorageVo extends PageVo{
    /**
     * 资产类别(1车辆卡片  2房屋卡片  3构筑物卡片   4家具、用具、装具卡片  5通用设备卡片  6专用设备)
     */
    @ApiModelProperty(value = "资产类型",example = "1",required = true)
    @NotNull(message = "资产类别不能为空")
    private Integer assetsType;
}
