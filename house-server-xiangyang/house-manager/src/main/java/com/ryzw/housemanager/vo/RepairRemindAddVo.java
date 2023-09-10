package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.RepairRemind;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 新增维修提醒参数
 * </p>
 *
 * @author mfl
 * @since 2019/4/11
 */
@Data
public class RepairRemindAddVo {

    /**
     * 维修提醒对象
     */
    private RepairRemind repairRemind;

    /**
     * 单位集合
     */
    private List<Long> unitList;
}
