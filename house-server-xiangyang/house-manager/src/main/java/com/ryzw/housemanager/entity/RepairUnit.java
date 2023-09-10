package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 维修单位表
 * </p>
 *
 * @author mfl
 * @since 2019-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RepairUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维修单位编号
     */
    @TableId(value = "repair_unit_id", type = IdType.AUTO)
    private Long repairUnitId;

    /**
     * 单位序号
     */
    private Long unitId;

    /**
     * 维修申请编号
     */
    private Long repairApplyId;


}
