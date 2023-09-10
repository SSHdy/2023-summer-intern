package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2019/10/17
 */
@Data
public class PurchasingInfoVo {
    private Long id;

    /**
     * 采购编号
     */
    private String purchasingId;

    /**
     * 采购项目名称
     */
    private String purchasingPname;

    /**
     * 采购分类
     */
    private String procurementType;

    /**
     * 组织形式
     */
    private String organizationForm;

    /**
     * 采购方式
     */
    private String procurementMode;

    /**
     * 采购金额
     */
    private Integer purchaseAmount;

    /**
     * 经办科室
     */
    private String handleDepartment;

    /**
     * 申请人
     */
    private String applicant;

    /**
     * 申请时间
     */
    private String applicationTime;

    /**
     * 经办人
     */
    private String operator;

    /**
     * 代理机构
     */
    private String agency;

    /**
     * 账面资产品牌型号
     */
    private String brandMode;

    /**
     * 账面资产品牌
     */
    private String brandName;

    /**
     * 采购备案
     */
    private String procurementRecord;

    /**
     * 单价
     */
    private Integer unitPrice;

    /**
     * 数量
     */
    private Integer number;

    /**
     * 单位
     */
    private String unit;

    /**
     * 资产在内控系统的唯一标识
     */
    private String assetId;

    /**
     * 所属类目
     */
    private String category;

    /**
     * 所属类目编号
     */
    private String categoryCode;
}
