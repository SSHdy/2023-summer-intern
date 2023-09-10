package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 * 类目树类型
 * </p>
 *
 * @author yz
 * @since 2019/2/28
 */
@Data
public class CategoryTreeDto {
    /**
     * 类型编号
     */
    private String categoryCode;
    /**
     * 类型名称
     */
    private String categoryName;
    /**
     * 类目单位
     */
    private String categoryUnit;
    /**
     * 数量
     */
    private int num;
}
