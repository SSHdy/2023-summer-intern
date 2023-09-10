package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *发起付款信息输入
 * </p>
 *
 * @author mfl
 * @since 2019/5/7
 */
@Data
public class RepairPaymentVo {

    /**
     * 项目付款编号
     */
    private List<Long> repairPaymentId;

    /**
     * 维修申请编号
     */
    private Long repairApplyId;

    /**
     * 操作人
     */
    private String repairPaymentUsername;

    /**
     * 操作人编号
     */
    private Long repairPaymentUserid;

    /**
     * 操作时间
     */
    private Date repairPaymentTime;

    /**
     * 付款状态
     */
    private Integer repairPaymentState;
}
