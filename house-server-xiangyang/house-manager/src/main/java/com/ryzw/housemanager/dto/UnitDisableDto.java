package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *  查询所有单位时，显示单位是否可用
 * </p>
 *
 * @author yz
 * @since 2019/5/13
 */
@Data
public class UnitDisableDto {

    /**
     * 单位序号
     */
    private Long unitId;


    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 是否可用
     */
    private boolean disabled;
}
