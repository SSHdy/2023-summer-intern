package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 维修提醒管理
 * </p>
 *
 * @author mfl
 * @since 2019-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RepairRemind implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维修提醒序号
     */
    @TableId(value = "repair_remind", type = IdType.AUTO)
    private Long repairRemind;

    /**
     * 负责人
     */
    private String responsiblePerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 楼层编号
     */
    private Integer floorName;

    /**
     * 维修部位
     */
    private Integer repairPart;

    /**
     * 维修内容
     */
    private String repairGoods;

    /**
     * 提醒时间
     */
    private Date remaindTime;

    /**
     * 是否已处理(1未提醒，2忽略，3处理)
     */
    private Integer remaindHandle;

    /**
     * 备注
     */
    private String remaindRemark;

    /**
     * 地址字符串
     */
    private String position;

}
