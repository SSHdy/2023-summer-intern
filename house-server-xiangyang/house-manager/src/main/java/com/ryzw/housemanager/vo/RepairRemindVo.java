package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 维修提醒参数输入
 * </p>
 *
 * @author mfl
 * @since 2019/2/28
 */
@Data
public class RepairRemindVo extends PageVo {
    /**
     * 维修提醒序号
     */
    private Long repairRemind;

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
     * 单位Id
     */
    private Long unitId;

    /**
     * 当前时间
     */
    private Date currDate;

    /**
     * 结束时间
     */
    private Date endDate;

    /**
     * 提醒状态 0不提醒  1提醒
     */
    private Integer remindState;

    /**
     * 提醒天数
     */
    private Integer repairRemindDay;
}
