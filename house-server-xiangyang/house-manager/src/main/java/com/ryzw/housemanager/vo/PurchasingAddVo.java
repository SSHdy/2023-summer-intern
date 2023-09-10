package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.Property;
import com.ryzw.housemanager.entity.Purchasing;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2019/10/21
 */
@Data
public class PurchasingAddVo {
    /**
     * 采购信息
     */
    private Purchasing baseInfo;

    /**
     * 资产信息
     */
    private List<Property> assetInfoList;

}
