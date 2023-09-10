package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *     维修内控审批流程传输的参数
 * </p>
 *
 * @author wj
 * @since 2019-09-23 0023
 */
@Data
public class RepairHttpResultVo {

    /**
     * 	经办人
     */
    private String name;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     *简述（零星可以填写维修内容或项目名称）
     */
    private String title;

    /**
     * 预算事项（预算事项 ID）
     */
    private String detailid;

    /**
     * 预算事项类型（A:科室预算明细）
     */
    private String etype;

    /**
     * 事项说明（维修地点，维修部位，维修内容）或者项目简介
     */
    private String description;

    /**
     * 采购名称
     */
    private String 	common_name;

    /**
     * 采购单价
     */
    private String common_price;

    /**
     * 采购数量 平方米
     */
    private String common_quantity;

    /**
     * 单位（台，袋，平方 等等）
     */
    private String common_unit;

    /**
     * 总金额 单价*数量
     */
    private String 	common_total;
}
