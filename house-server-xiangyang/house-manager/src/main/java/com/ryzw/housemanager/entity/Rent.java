package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 租赁房间信息表
 * </p>
 *
 * @author mfl
 * @since 2019-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Rent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 房屋租赁编号
     */
    @TableId(value = "rent_id", type = IdType.AUTO)
    private Long rentId;

    /**
     * 租赁方式
     */
    private Integer rentMode;

    /**
     * 承租用途
     */
    private String lesseeUse;

    /**
     * 承租年限
     */
    private Double lesseeYears;

    /**
     * 承租方
     */
    private String renter;

    /**
     * 开始时间
     */
    private Date rentBegin;

    /**
     * 结束时间
     */
    private Date rentEnd;

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
     * 租赁类型
     */
    private Integer rentType;

    /**
     * 联系方式
     */
    private String contactPhone;

    /**
     * 建筑面积
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Double coveredArea;

    /**
     * 所属办公楼
     */
    private String officeBuilding;

    /**
     * 所属单位
     */
    private Long affiliatedUnit;

    /**
     * 财政备案
     */
    private String financialCode;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 付款时间
     */
    private Date paymentTime;

    /**
     * 表类型 1出租  2出借
     */
    private Integer type;

    /**
     * 首次付款时间
     */
    private Date firstPaymentTime;

    /**
     * 付款周期(月)
     */
    private Integer paymentCycle;

    /**
     * 是否已阅(0未读  1已读)
     */
    private Integer isRead;

    /**
     * 状态 1院落 2土地
     */
    private Integer state;

    /**
     * 到期提醒: 0未到期未提醒  1已到期已提醒
     */
    private Integer isExpire;

}
