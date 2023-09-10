package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 建筑物保养部位表
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("repairParts")
public class RepairParts implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "repair_parts_id", type = IdType.AUTO)
    private Integer repairPartsId;

    /**
     * 维修部位名称
     */
    private String repairPartName;

    /**
     * 质保期
     */
    private Integer repairPartQuality;

    /**
     * 是否删除（0未删除，1删除）
     */
    private Integer isDelete;


}
