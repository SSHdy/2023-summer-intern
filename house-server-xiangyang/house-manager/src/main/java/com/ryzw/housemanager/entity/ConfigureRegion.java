package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配置区域表
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigureRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置区域编号
     */
    @TableId(value = "configure_region_id", type = IdType.AUTO)
    private Long configureRegionId;

    /**
     * 配置申请编号
     */
    private Long configureId;

    /**
     * 区域id
     */
    private Integer regionId;


}
