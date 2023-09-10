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
 * 院落物业表
 * </p>
 *
 * @author mfl
 * @since 2019-05-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class YardEstate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 院落物业编号
     */
    @TableId(value = "yard_estate_id", type = IdType.AUTO)
    private Long yardEstateId;

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 物业编号
     */
    private Long estateId;

    /**
     * 开始使用日期
     */
    private Date startTime;

    /**
     * 结束使用日期
     */
    private Date endTime;

    /**
     * 合同金额
     */
    private BigDecimal contractAmount;

    /**
     * 经费来源
     */
    private String moneyFrom;


}
