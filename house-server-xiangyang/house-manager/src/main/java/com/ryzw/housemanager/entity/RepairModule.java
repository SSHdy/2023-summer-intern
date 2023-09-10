package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 维修模块表
 * </p>
 *
 * @author mfl
 * @since 2019-03-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RepairModule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维修模块编号
     */
    @TableId(value = "repair_module_id", type = IdType.AUTO)
    private Long repairModuleId;

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 维修申请编号
     */
    private Long repairApplyId;

    /**
     * 维修部位编号
     */
    private Long repairPartId;

    /**
     * 维修模块位置
     */
    private String repairModulePosition;

    /**
     * 维修目标
     */
    private String repairModuleTarget;

    /**
     * 维修面积
     */
    private BigDecimal repairModuleArea;

    /**
     * 是否维修(0申请  1 删除  2维修完成)
     */
    private Integer isRepair;

    /**
     * 楼层
     */
    private Integer floorName;

    /**
     * 质保日期
     */
    private Date repairQuality;

    /**
     * 操作人
     */
    private String repairOperator;

    /**
     * 维修位置字符串
     */
    private String positionStr;


}
