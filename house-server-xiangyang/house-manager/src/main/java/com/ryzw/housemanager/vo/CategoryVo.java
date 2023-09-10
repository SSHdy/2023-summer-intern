package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *资产类别管理输入
 * </p>
 *
 * @author mfl
 * @since 2019/1/6
 */
@Data
public class CategoryVo extends PageVo{
    /**
     * 自增id
     */
    private Long categoryId;
    /**
     * 设备编码
     */
    private String categoryCode;

    /**
     * 设备名称
     */
    private String categoryName;

    /**
     * 计量单位
     */
    private String categoryUnit;

    /**
     * 父级id
     */
    private Integer categoryPid;

    /**
     * 备注
     */
    private String categoryRemark;
}
