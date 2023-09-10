package com.ryzw.housemanager.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * <p>
 *     返回配置方案房间单位信息数据
 * </p>
 *
 * @author wj
 * @since 2019-06-20 0020
 */
@Data
public class ConfigurePlanUnitDto {

    /**
     * 自用房间ID
     */
    @TableField("useunit_id")
    private Long useunitId;

    /**
     * 单位名称
     */
    private String unitName;

}
