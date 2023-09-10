package com.ryzw.housemanager.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 * 单位分组
 * </p>
 *
 * @author yz
 * @since 2019/9/10
 */
@Data
@AllArgsConstructor
public class UnitGroup {
    /**
     * 单位编号
     */
    private Long unitId;
    /**
     * 单位编号
     */
    private String unitName;
    /**
     * 单位类别
     */
    private String unitCategory;
}
