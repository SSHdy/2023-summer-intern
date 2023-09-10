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
 * 合同表
 * </p>
 *
 * @author yz
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Contract implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同id
     */
    @TableId(value = "contract_id", type = IdType.AUTO)
    private Long contractId;

    /**
     * 租赁开始时间
     */
    private Date leaseBeginTime;

    /**
     * 租赁结束时间
     */
    private Date leaseEndTime;

    /**
     * 合同年限
     */
    private Integer contractYear;

    /**
     * 付款时间
     */
    private Date paymentTime;

    /**
     * 首次付款时间
     */
    private Date firstPaymentTime;

    /**
     * 到期提醒: 0未到期未提醒  1已到期已提醒到期提醒
     */
    private Integer isExpire;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 出租单位
     */
    private String rentalUnit;

    /**
     * 付款周期(月)
     */
    private Integer paymentCycle;

    /**
     * 是否已阅(0未读  1已读)
     */
    private Integer isRead;

    /**
     * 租金
     */
    private BigDecimal rentMoney;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 押金
     */
    private BigDecimal deposit;

    /**
     * 出租类型
     */
    private Integer rentType;

    /**
     * 联系方式
     */
    private String contactPhone;

    /**
     * 是否删除(0未删除  1已删除)
     */
    private Integer idDelete;


}
