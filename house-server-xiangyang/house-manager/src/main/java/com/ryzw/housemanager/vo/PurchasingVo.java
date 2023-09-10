package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *采购项目信息管理
 * </p>
 *
 * @author mfl
 * @since 2019/10/17
 */
@Data
public class PurchasingVo extends PageVo{
    /**
     * 采购项目名称
     */
    private String purchasingPname;

    /**
     * 状态（1：完成 2:未完成）
     */
    private Integer status;
}
