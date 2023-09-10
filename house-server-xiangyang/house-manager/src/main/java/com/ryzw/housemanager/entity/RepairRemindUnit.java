package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 维修提醒单位表
 * </p>
 *
 * @author mfl
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RepairRemindUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维修提醒单位编号
     */
    @TableId(value = "repair_remind_unit_id", type = IdType.AUTO)
    private Long repairRemindUnitId;

    /**
     * 维修提醒编号
     */
    private Long repairRemindId;

    /**
     * 使用单位编号
     */
    private Long unitId;


}
