package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * <p>
 *维修项目列表前端传入的值
 * </p>
 *
 * @author mfl
 * @since 2019/4/3
 */
@Data
public class RepairProjectListVo extends PageVo {
    /**
     * 维修申请id
     */
    private Long repairApplyId;

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 维修申请时间
     */
    private Date beginDate;

    /**
     * 维修申请时间
     */
    private Date endDate;


    /**
     * 用户编号
     */
    @NotNull(message = "用户编号不能为空")
    private Long userId;
}
