package com.ryzw.housemanager.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 输出给前端的维修提醒列表信息
 * </p>
 *
 * @author zf
 * @since 2019/4/11
 */
@Data
public class RepairRemindListDto extends Page {

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
     * 提醒时间
     */
    private Date remaindTime;

    /**
     * 地址字符串
     */
    private String position;

    /**
     * 维修部位编号
     */
    private Long repairPart;

    /**
     * 上次维修时间
     */
    private Date lastRepairTime;

    /**
     * 联系人
     */
    private String responsiblePerson;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 维修位置字符串
     */
    private String positionStr;

    /**
     *提醒状态 0为不提醒  1为提醒
     */
    private Integer remindState;

    /**
     * 单位Id集合
     */
    private List<Long> unitIdList;

}
