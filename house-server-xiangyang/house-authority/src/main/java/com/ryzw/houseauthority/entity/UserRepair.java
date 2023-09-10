package com.ryzw.houseauthority.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户维修权限表
 * </p>
 *
 * @author mfl
 * @since 2019-04-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserRepair implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户维修权限编号
     */
    @TableId(value = "user_repair_id", type = IdType.AUTO)
    private Long userRepairId;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 维修权限编号
     */
    private Long repairId;


}
