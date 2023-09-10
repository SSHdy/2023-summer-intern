package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p>
 *资产公共部分输出
 * </p>
 *
 * @author mfl
 * @since 2019/1/10
 */
@Data
@Document("assert")
public class AssetsComnDto {

    /**
     * id
     */
    private  String id;

    /**
     * 申请时间
     */
    private String applicationTime;

    /**
     * 卡片编号
     */
    @ApiModelProperty(value = "卡片编号")
    private String cardId;

    /**
     * 资产名称
     */
    @ApiModelProperty(value = "资产名称")
    private String assetNameComn;

    /**
     * 资产编码
     */
    @ApiModelProperty(value = "资产编码")
    private String categoryCodeComn;
    /**
     * 资产分类名称
     */
    @ApiModelProperty(value = "资产分类名称")
    private String categoryName;

    /**
     * 采购组织形式
     */
    @ApiModelProperty(value = "采购组织形式")
    private String procurementModeComn;

    /**
     * 预算项目编号
     */
    @ApiModelProperty(value = "预算项目编号")
    private String budgetItemNumberComn;

    /**
     * 价值类型
     */
    @ApiModelProperty(value = "价值类型")
    private String valueTypeComn;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer assetNumberComn;


    /**
     * 价值
     */
    @ApiModelProperty(value = "价值")
    private Float assetWorthComn;


    /**
     * 资产类别(1车辆卡片  2房屋卡片  3构筑物卡片   4家具、用具、装具卡片  5通用设备卡片  6专用设备)
     */
    private Integer assetsType;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String assetRemarksComn;

    /**
     * 是否注销
     */
    @ApiModelProperty(value = "是否注销，0有效，1注销")
    private String isCancleComn;

}
