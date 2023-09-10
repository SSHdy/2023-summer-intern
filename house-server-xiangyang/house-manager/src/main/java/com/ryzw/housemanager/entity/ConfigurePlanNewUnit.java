package com.ryzw.housemanager.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配置方案新使用单位表
 * </p>
 *
 * @author mfl
 * @since 2019-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigurePlanNewUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置方案数据单位编号
     */
    @TableId(value = "configure_plan_new_unit_id", type = IdType.AUTO)
    private Long configurePlanNewUnitId;

    /**
     * 配置方案数据编号
     */
    private Long configurePlanDataId;

    /**
     * 新使用单位id
     */
    private Long unitId;


}
