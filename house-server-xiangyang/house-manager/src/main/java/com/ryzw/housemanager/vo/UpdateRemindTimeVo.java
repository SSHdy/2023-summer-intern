package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 * 修改维修提醒时间参数
 * </p>
 *
 * @author zf
 * @since 2019/4/15
 */
@Data
public class UpdateRemindTimeVo {

    /**
     * 维修提醒编号
     */
    @NotNull(message = "维修提醒编号不能为空")
    private Long repairRemind;

    /**
     * 维修提醒时间
     */
    @NotNull(message = "维修提醒时间不能为空")
    private Date remaindTime;
}
