package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配置方案原使用单位表
 * </p>
 *
 * @author mfl
 * @since 2019-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigurePlanOldUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置方案数据单位编号
     */
    @TableId(value = "configure_plan_old_unit_id", type = IdType.AUTO)
    private Long configurePlanOldUnitId;

    /**
     * 配置方案数据编号
     */
    private Long configurePlanDataId;

    /**
     * 原使用单位id
     */
    private Long unitId;


}
