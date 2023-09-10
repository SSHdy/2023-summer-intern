package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 采购表
 * </p>
 *
 * @author mfl
 * @since 2019-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Purchasing implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 采购编号
     */
    @TableId(value = "purchasing_id", type = IdType.AUTO)
    private Long purchasingId;

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
    private String purchaseAmount;

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
     * 采购单价
     */
    private String unitPrice;

    /**
     * 计量单位
     */
    private String unit;

   /**
     * 所属类目
     */
    private String category;

    /**
     * 所属类目编号
     */
    private String categoryCode;

    /**
     * 状态（1：完成 2:未完成）
     */
    private Integer status;


}
