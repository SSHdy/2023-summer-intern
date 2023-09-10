package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.entity.ContractEnclosure;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *     租赁合同详情
 * </p>
 *
 * @author wj
 * @since 2019-08-28 0028
 */
@Data
public class ContractDetailDto {
    /**
     * 合同id
     */
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

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 楼栋名称
     */
    private String buildName;

    /**
     * 楼层
     */
    private Integer floorName;

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 租赁合同附件
     */
    private List<ContractEnclosure> contractEnclosures;


}
