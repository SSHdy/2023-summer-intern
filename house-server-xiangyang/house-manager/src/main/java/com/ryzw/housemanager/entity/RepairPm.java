package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 维修项目管理人表
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RepairPm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维修项目管理编号
     */
    @TableId(value = "repair_pm_id", type = IdType.AUTO)
    private Long repairPmId;

    /**
     * 维修申请编号
     */
    private Long repairApplyId;

    /**
     * 维修项目管理人员编号
     */
    private Long repairPmUserid;

    /**
     * 维修项目管理人名称
     */
    private String repairPmName;


}
