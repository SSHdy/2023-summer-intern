package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *     返回给内控的数据
 * </p>
 *
 * @author wj
 * @since 2019-06-27 0027
 */
@Data
public class ConfigureHttpDto {

    /**
     *配置基础数据
     */
    private ConfigureBasicHttpDto configureBasicHttpDto;

    /**
     *配置分配数据
     */
    private List<ConfigureDistributionHttpDto> configureDistributionHttps;
}
