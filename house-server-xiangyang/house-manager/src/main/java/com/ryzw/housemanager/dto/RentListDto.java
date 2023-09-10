package com.ryzw.housemanager.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *输出给前端的租赁列表
 * </p>
 *
 * @author zf
 * @since 2019/3/19
 */
@Data
public class RentListDto {

    /**
     * 房屋租赁编号
     */
    private Long rentId;

    /**
     * 房屋使用id
     */
    private Long housinguseId;

    /**
     * 租赁方式
     */
    private Integer rentMode;

    /**
     * 承租用途
     */
    private String lesseeUse;

    /**
     * 承租人数
     */
    private Integer useNum;

    /**
     * 承租面积
     */
    private Double lesseeArea;

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
     * 押金
     */
    private BigDecimal deposit;

    /**
     * 租赁类型
     */
    private Integer rentType;

    /**
     * 租赁状态
     */
    private Integer rentState;

    /**
     * 付款时间
     */
    private Date paymentTime;

    /**
     * 表类型 1出租  2出借
     */
    private Integer type;
}
