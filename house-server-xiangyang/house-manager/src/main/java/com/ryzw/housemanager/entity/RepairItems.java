package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 维修增减项表
 * </p>
 *
 * @author mfl
 * @since 2019-04-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RepairItems implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 增减项编号
     */
    @TableId(value = "repair_items_id", type = IdType.AUTO)
    private Long repairItemsId;

    /**
     * 维修申请编号
     */
    private Long repairApplyId;

    /**
     * 增项说明
     */
    private String repairItemsAddExplain;

    /**
     * 减项说明
     */
    private String repairItemsReduceExplain;

    /**
     * 增减项文件虚拟地址
     */
    private String repairItemsVirtualAddress;

    /**
     * 增减项文件物理地址
     */
    private String repairItemsPhysicalAddress;


}
