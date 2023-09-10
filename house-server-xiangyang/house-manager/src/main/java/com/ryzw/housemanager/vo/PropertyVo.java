package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *资产信息前端传参
 * </p>
 *
 * @author mfl
 * @since 2019/10/17
 */
@Data
public class PropertyVo extends PageVo{
    /**
     * 采购编号
     */
    private String purchasingId;
}
