package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 维修提醒详情
 * </p>
 *
 * @author zf
 * @since 2019/4/15
 */
@Data
public class RepairRemindDto {

    /**
     * 维修提醒序号
     */
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
     * 地址字符串
     */
    private String position;

    /**
     * 使用单位名称
     */
    private List<String> unitNameList;

    /**
     * 维修部位名称
     */
    private String repairPartName;

    /**
     * 提醒时间
     */
    private Date remaindTime;

    /**
     * 维修内容
     */
    private String repairGoods;

    /**
     * 是否已处理
     */
    private Integer remaindHandle;

    /**
     * 备注
     */
    private String remaind_remark;

}
