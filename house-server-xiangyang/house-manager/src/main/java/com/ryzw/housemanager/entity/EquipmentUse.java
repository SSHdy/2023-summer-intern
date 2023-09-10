package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 设备关系实体
 * </p>
 *
 * @author mfl
 * @since 2019-01-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("equipmentUse")
@Builder
public class EquipmentUse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "equipmentUse_id", type = IdType.AUTO)
    private Long equipmentuseId;

    /**
     * buildId
     */
    private Long buildId;

    /**
     * 设备id
     */
    private Long equipmentId;
}
