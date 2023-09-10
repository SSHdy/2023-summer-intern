package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author mfl
 * @since 2019-10-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资产在内控系统的唯一标识
     */
    @TableId(value = "asset_id", type = IdType.AUTO)
    private Long assetId;

    /**
     * 采购编号
     */
    private Long purchasingId;

    /**
     * 账面资产品牌
     */
    private String brandName;

    /**
     * 账面资产品牌型号
     */
    private String brandMode;

    /**
     * 采购备案
     */
    private String procurementRecord;

    /**
     * 单价
     */
    private String unitPrice;

    /**
     * 数量
     */
    private String number;

    /**
     * 单位
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
     * 采购金额
     */
    private String purchaseAmount;

    /**
     * 资产类型
     */
    private Integer selectAssetType;


}
