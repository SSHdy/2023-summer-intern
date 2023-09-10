package com.ryzw.housemanager.mapper;

import com.ryzw.housemanager.dto.ConfigureDistributionHttpDto;
import com.ryzw.housemanager.dto.ConfigurePlanUnitDto;
import com.ryzw.housemanager.entity.ConfigurePlanData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 配置方案数据表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
public interface ConfigurePlanDataMapper extends BaseMapper<ConfigurePlanData> {

    /**
     * 按配置方案数据ID批量删除所有配置方案数据关联关系
     * @param configurePlanDataIds
     * @return
     */
    int deleteByCPDataIds(@Param("configurePlanDataIds") List<Long> configurePlanDataIds);

    /**
     * 根据配置id删除配置方案以及关联数据
     * @param configureId
     * @return
     */
    int deleteByConfigureId(@Param("configureId")Long configureId);

    /**
     * 根据房间id删除配置方案以及关联数据
     * @param houseIds
     * @return
     */
    int deleteByHouseId(@Param("houseIds")List<String> houseIds);

    /**
     * 查询配置方案房间单位信息
     * @param houseId
     * @return
     */
    List<ConfigurePlanUnitDto> selectConfigurePlanUnit(@Param("houseId") String  houseId);

    /**
     * 查询配置方案数据以及相关联的数据
     * @param configureId
     * @return
     */
    List<ConfigureDistributionHttpDto> selectConfigureDbo(@Param("configureId")Long configureId);

}
