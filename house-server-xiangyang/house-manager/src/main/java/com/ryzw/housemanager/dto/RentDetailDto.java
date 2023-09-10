package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.entity.Enclosure;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *输出给前端的租赁详情
 * </p>
 *
 * @author zf
 * @since 2019/3/13
 */
@Data
public class RentDetailDto {

    /**
     * 房屋租赁编号
     */
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
    private Double coveredArea;

    /**
     * 付款时间
     */
    private Date paymentTime;

    /**
     * 付款周期
     */
    private Integer paymentCycle;

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
     * 状态 1院落 2土地
     */
    private Integer state;

    /**
     * 附件集合
     */
    private List<Enclosure> enclosureList;


    /**
     *院落集合
     */
   private List<YardRentDto> yardList;

    /**
     * 租赁关系编号集合
     */
    private List<Long> rentRelationshipIdList;

}
