package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配置楼座中间表
 * </p>
 *
 * @author mfl
 * @since 2019-07-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigureBuild implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置楼座id
     */
    @TableId(value = "configure_build_id", type = IdType.AUTO)
    private Long configureBuildId;

    /**
     * 配置id
     */
    private Long configureId;

    /**
     * 楼座编号
     */
    private Long buildId;


}
