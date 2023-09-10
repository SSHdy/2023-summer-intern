package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 出租用房信息表
 * </p>
 *
 * @author mfl
 * @since 2019-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OutRent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 出租用房信息编号
     */
    @TableId(value = "out_rent_id", type = IdType.AUTO)
    private Long outRentId;

    /**
     * 所属办公楼
     */
    private Long buildId;

    /**
     * 建筑面积
     */
    private Double coveredArea;

    /**
     * 租用单位
     */
    private Long rentUnit;

    /**
     * 租用开始时间
     */
    private Date startTime;

    /**
     * 租用截止时间
     */
    private Date endTime;

    /**
     * 租金标准
     */
    private BigDecimal rentMoney;

    /**
     * 是否租赁 0否  1是
     */
    private Integer isRent;

    /**
     * 财政备案或批复文号
     */
    private String financeRemark;


}
