package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配置方案数据改变
 * </p>
 *
 * @author mfl
 * @since 2019-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigurePlanChangeData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置方案数据改变id
     */
    @TableId(value = "configure_plan_change_data_id", type = IdType.AUTO)
    private Long configurePlanChangeDataId;

    /**
     * 方案id
     */
    private Long planId;

    /**
     * 新单位名称
     */
    private String unitNames;

    /**
     * 使用面积
     */
    private Float useArea;

    /**
     * 房间数量
     */
    private Integer houseNumber;


}
