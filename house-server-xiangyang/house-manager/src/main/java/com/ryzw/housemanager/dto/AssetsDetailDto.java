package com.ryzw.housemanager.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * <p>
 * 资产详情
 * </p>
 *
 * @author mfl
 * @since 2019/1/17
 */
@Data
@Document("assert")
public class AssetsDetailDto {
    /**
     * 自增id
     */
    private String assetsIdComn;

    /**
     * 卡片编号
     */
    private String cardIdComn;

    /**
     * 资产名称
     */
    private String assetNameComn;

    /**
     * 资产编码
     */
    private String categoryCodeComn;


    /**
     * 采购组织形式
     */
    private Integer procurementModeComn;

    /**
     * 预算项目编号
     */
    private String budgetItemNumberComn;

    /**
     * 取得方式
     */
    private Integer acquireWayComn;


    /**
     * 取得日期
     */
    private String acquireDateComn;


    /**
     * 财政拨款
     */
    private Float financialAppropriationComn;

    /**
     * 非财政拨款
     */
    private Float nonFinancialAppropriationComn;

    /**
     * 财务入账状态
     */
    private Integer financialEntryStatusComn;

    /**
     * 价值类型
     */
    private Integer valueTypeComn;

    /**
     * 数量
     */
    private Integer assetNumberComn;

    /**
     * 价值
     */
    private Float assetWorthComn;


    /**
     * 财务入账日期
     */
    private String financialEntryDateComn;

    /**
     * 入账科目
     */
    private Integer accountEntryComn;

    /**
     * 会计凭证号
     */
    private String accountingVoucherNumberComn;

    /**
     * 使用状况
     */
    private Integer usageStatusComn;

    /**
     * 投入使用日期
     */
    private String commissioningDateComn;

    /**
     * 使用/管理部门
     */
    private String useManagementComn;

    /**
     * 折旧状态
     */
    private Integer depreciationStatusComn;


    /**
     * 备注
     */
    private String assetRemarksComn;


    /**
     * 条形码
     */
    private String barCodeComn;


    /**
     * 申请人
     */
    private String assetApplicant;

    /**
     * 申请时间
     */
    private String applicationTime;

    /**
     * 经办人
     */
    private String assetOperator;

    /**
     * 经办科室
     */
    private String managementOffice;

    /**
     * 计量单位
     */
    private String assetUnit;

    /**
     * 内控系统中的资产唯一标识
     */
    private String icassetId;

    /**
     * 财政资产库中的唯一资产编号
     */
    private String assetNo;

    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 资产类别(1车辆卡片  2房屋卡片  3构筑物卡片   4家具、用具、装具卡片  5通用设备卡片  6专用设备)
     */
    private Integer assetsType;

    /**
     * 状态(1入库 2转移 3销毁)
     */
    private Integer status;
}
