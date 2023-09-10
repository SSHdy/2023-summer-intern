package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 设备管理表
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备编号
     */
    @TableId(value = "equipment_id", type = IdType.AUTO)
    private Long equipmentId;

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备数量
     */
    private Integer equipmentNum;

    /**
     * 负责人
     */
    private String equipmentCharge;

    /**
     * 负责人联系方式
     */
    private String equipmentContact;

    /**
     * 设备购入时间
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Date equipmentTime;

    /**
     * 设备唯一编号
     */
    private String equipmentCode;

    /**
     * 备注
     */
    private String equipmentRemark;

    /**
     * 设备型号
     */
    private String equipmentType;

    /**
     * 设备品牌
     */
    private String equipmentBrand;

}
