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
 * 项目管理人变更日志表
 * </p>
 *
 * @author mfl
 * @since 2019-04-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PmChangeLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 变更日志编号
     */
    @TableId(value = "repair_change_id", type = IdType.AUTO)
    private Long repairChangeId;

    /**
     * 维修申请编号
     */
    private Long repairApplyId;

    /**
     * 原始负责人
     */
    private String repairUsername;

    /**
     * 原始负责人编号
     */
    private Long repairUserid;

    /**
     * 新项目负责人
     */
    private String repairNewUsername;

    /**
     * 新项目负责人编号
     */
    private Long repairNewUserid;

    /**
     * 变更原因
     */
    private String repairChangeReason;

    /**
     * 变更时间
     */
    private Date repairChangeTime;


}
