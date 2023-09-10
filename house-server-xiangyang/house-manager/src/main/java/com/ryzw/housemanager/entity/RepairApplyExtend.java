package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 维修申请信息表
 * </p>
 *
 * @author wj
 * @since 2019-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RepairApplyExtend implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维修申请扩展编号
     */
    @TableId(value = "repair_apply_extend_id", type = IdType.AUTO)
    private Long repairApplyExtendId;

    /**
     * 维修申请编号
     */
    private Long repairApplyId;

    /**
     * 维修申请扩展键
     */
    private String repairApplyExtendKey;

    /**
     * 维修申请扩展值
     */
    private String repairApplyExtendValue;

    /**
     * 维修申请步骤
     */
    private Integer repairApplyExtendStep;


}
