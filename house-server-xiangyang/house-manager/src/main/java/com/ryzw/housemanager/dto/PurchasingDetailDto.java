package com.ryzw.housemanager.dto;

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
public class PurchasingDetailDto {

    /**
     * 采购信息
     */
    private Purchasing baseInfo;

    /**
     * 资产信息
     */
    private List<Property> assetInfoList;
}
