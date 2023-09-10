package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.ConPlanDataDto;
import com.ryzw.housemanager.dto.ConfigureDto;
import com.ryzw.housemanager.dto.ConfigureStatisticsDto;
import com.ryzw.housemanager.dto.DateRangeDto;
import com.ryzw.housemanager.entity.Configure;
import com.ryzw.housemanager.vo.ConfigureVo;
import com.ryzw.housemanager.vo.HouseListVo;
import com.ryzw.housemanager.vo.PlanDataVo;
import com.ryzw.housemanager.vo.WorkStatisticsVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 配置表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
public interface ConfigureMapper extends BaseMapper<Configure> {

    /**
     * 查询总面积
     *
     * @param unitId
     * @return
     */
    Float selectTotalArea(@Param("unitId") Integer unitId);

    /**
     * 配置申请列表
     *
     * @param page
     * @param configureVo
     * @return
     */
    Page<ConfigureDto> configureList(Page<ConfigureDto> page, @Param("configureVo") ConfigureVo configureVo);

    /**
     * 查询用房情况审查数据
     *
     * @param id
     * @return
     */
    String selectData(@Param("id") Long id);

    /**
     * 查看分配方案数据
     *
     * @param page
     * @param planDataVo
     * @return
     */
    Page<ConPlanDataDto> selectPlanData(Page<ConPlanDataDto> page, @Param("planDataVo") PlanDataVo planDataVo);

    /**
     * 查询区域集合
     *
     * @param id
     * @return
     */
    List<String> selectRegionList(@Param("id") Long id);

    /**
     * 查询某个用户所有审批配置记录
     *
     * @param page
     * @param userId
     * @param repairApplyIdList
     * @return
     */
    Page<ConfigureDto> selectConfigureRecord(Page<ConfigureDto> page, @Param("userId") Long userId, @Param("congigureIdList") List<Long> congigureIdList);

    /**
     * 查看分配方案数据(人工)
     *
     * @param configureId
     * @return
     */
    String planData(@Param("configureId") Long configureId);

    /**
     * 查询配置工作统计(按月)
     *
     * @param workStatisticsVo
     * @return
     */
    ConfigureStatisticsDto configureStatistics(@Param("workStatisticsVo") WorkStatisticsVo workStatisticsVo);

    /**
     * 查询配置Id集合
     *
     * @param workStatisticsVo
     * @return
     */
    List<Long> configureIdList(@Param("workStatisticsVo") WorkStatisticsVo workStatisticsVo);

    /**
     * 查询配置工作统计日期范围
     *
     * @param unitIds
     * @return
     */
    DateRangeDto statisticsDateRange(@Param("unitIds") List<Long> unitIds);

    /**
     * 显示相关联的配置申请
     *
     * @param page
     * @param houseListVo
     * @return
     */
    Page<Configure> configureListByHouseIdOrFloor(Page<Configure> page, @Param("houseListVo") HouseListVo houseListVo);

    /**
     * 获取相关联的配置申请Id集合
     *
     * @param houseListVo
     * @return
     */
    List<Long> selectConfigureIdList(@Param("houseListVo") HouseListVo houseListVo);

    /**
     * 获取本月调剂面积
     *
     * @param workStatisticsVo
     * @return
     */
    BigDecimal getTjArea(@Param("workStatisticsVo") WorkStatisticsVo workStatisticsVo);
}
