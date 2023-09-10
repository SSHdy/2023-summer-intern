package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配置方案数据表
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigurePlanData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置方案数据编号
     */
    @TableId(value = "configure_plan_data_id", type = IdType.AUTO)
    private Long configurePlanDataId;

    /**
     * 方案编号
     */
    private Long planId;

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
     * 方案类型
     */
    private Integer planType;

    /**
     * 配置申请编号
     */
    private Long configureId;

    /**
     * 是否闲置(0闲置 1非闲置)
     */
    private Integer isIdle;

    /**
     * 原使用单位集合
     */
    @TableField(exist = false)
    private List<ConfigurePlanOldUnit> configurePlanOldUnits;

    /**
     * 新使用单位集合
     */
    @TableField(exist = false)
    private List<ConfigurePlanNewUnit> configurePlanNewUnits;

    /**
     * 删除的新使用单位id集合
     */
    @TableField(exist = false)
    private List<Long> deleteCPNUnitIds;

    /**
     * 删除原使用单位id集合
     */
    @TableField(exist = false)
    private List<Long> deleteCPOUnitIds;




}
