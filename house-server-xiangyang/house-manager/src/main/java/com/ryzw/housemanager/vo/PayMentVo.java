package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *付款状态前端传入的值
 * </p>
 *
 * @author mfl
 * @since 2019/4/9
 */
@Data
public class PayMentVo {
    /**
     * 项目付款编号
     */
    private Long repairPaymentId;

    /**
     * 付款状态
     */
    private Integer repairPaymentState;
}
