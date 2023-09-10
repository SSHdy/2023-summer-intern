package com.ryzw.housemanager.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 项目付款表
 * </p>
 *
 * @author mfl
 * @since 2019-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RepairPayment{


    /**
     * 项目付款编号
     */
    @TableId(value = "repair_payment_id", type = IdType.AUTO)
    private Long repairPaymentId;

    /**
     * 维修申请编号
     */
    private Long repairApplyId;

    /**
     * 付款进度
     */
    private String repairPaymentProgress;

    /**
     * 付款比例
     */
    private Double repairPaymentRatio;

    /**
     * 付款金额
     */
    private BigDecimal repairPaymentMoney;

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

    /**
     * 付款阶段
     */
    private String stepLabel;

    /**
     * 阶段目标
     */
    private String stepTarget;

    /**
     * 备注说明
     */
    private String stepDescription;


}
