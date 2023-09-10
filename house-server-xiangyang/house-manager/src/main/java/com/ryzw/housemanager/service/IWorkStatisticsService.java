package com.ryzw.housemanager.service;

import com.ryzw.housemanager.dto.ConfigureStatisticsDto;
import com.ryzw.housemanager.dto.DateRangeDto;
import com.ryzw.housemanager.entity.HandleWorkStatistics;
import com.ryzw.housemanager.entity.RepairWorkStatistics;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.WorkStatisticsVo;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author wj
 * @since 2019-07-24 0024
 */
public interface IWorkStatisticsService {

    /**
     * 查询维修工作统计(按月)
     *
     * @param workStatisticsVo
     * @return
     */
    List<RepairWorkStatistics> selectRepairMothCount(WorkStatisticsVo workStatisticsVo);

    /**
     * 查询维修工作统计(按年)
     *
     * @param workStatisticsVo
     * @return
     */
    List<RepairWorkStatistics> selectRepairYearCount(WorkStatisticsVo workStatisticsVo);

    /**
     * 查询配置工作统计(按月)
     *
     * @param workStatisticsVo
     * @return
     */
    List<ConfigureStatisticsDto> configureStatisticsByMonth(WorkStatisticsVo workStatisticsVo);

    /**
     * 查询配置工作统计(按年)
     *
     * @param workStatisticsVo
     * @return
     */
    List<ConfigureStatisticsDto> configureStatisticsByYear(WorkStatisticsVo workStatisticsVo);

    /**
     * 查询处置工作统计(按月)
     *
     * @param workStatisticsVo
     * @return
     */
    List<HandleWorkStatistics> selectHandleMothCount(WorkStatisticsVo workStatisticsVo);

    /**
     * 查询处置工作统计(按年)
     *
     * @param workStatisticsVo
     * @return
     */
    List<HandleWorkStatistics> selectHandleYearCount(WorkStatisticsVo workStatisticsVo);

    /**
     * 查询工作统计日期范围
     *
     * @param basicVo
     * @return
     */
    DateRangeDto statisticsDateRange(BasicVo<Integer> basicVo);
}
