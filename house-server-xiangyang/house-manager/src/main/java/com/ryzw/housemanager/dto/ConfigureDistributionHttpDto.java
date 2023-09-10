package com.ryzw.housemanager.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 *     返回给内控的配置分配数据
 * </p>
 *
 * @author wj
 * @since 2019-06-27 0027
 */
@Data
@Accessors(chain = true)
public class ConfigureDistributionHttpDto {

    /**
     * 配置方案数据ID
     */
    private Long configurePlanDataId;

    /**
     * 房间位置
     */
    private String roomLocation;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 楼栋名称
     */
    private String buildName;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 房间面积
     */
    private Float roomArea;

    /**
     * 原使用单位
     */
    private String oldUserUnits;

    /**
     * 新使用单位
     */
    private String newUserUnits;

    /**
     * 配置原使用单位
     */
    private List<ConfigureOldUnitHttpDto> configureOldUnitHttpDtoList;

    /**
     * 配置新使用单位
     */
    private List<ConfigureNewUnitHttpDto> configureNewUnitHttpDtoList;
}
