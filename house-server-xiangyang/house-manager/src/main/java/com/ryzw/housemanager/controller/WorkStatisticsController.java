package com.ryzw.housemanager.controller;

import com.ryzw.housemanager.dto.ConfigureStatisticsDto;
import com.ryzw.housemanager.dto.DateRangeDto;
import com.ryzw.housemanager.entity.HandleWorkStatistics;
import com.ryzw.housemanager.entity.RepairWorkStatistics;
import com.ryzw.housemanager.service.IWorkStatisticsService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.WorkStatisticsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 工作统计  前端控制器
 * </p>
 *
 * @author wj
 * @since 2019-07-23 0023
 */
@RestController
@RequestMapping("/workStatistics")
@Api(value = "工作统计管理", description = "工作统计管理")
public class WorkStatisticsController {

    @Autowired
    IWorkStatisticsService workStatisticsService;

    /**
     * 查询维修工作统计(按月)
     *
     * @param workStatisticsVo
     * @return
     */
    @RequestMapping(value = "selectRepairMothCount", method = RequestMethod.POST)
    @ApiOperation(value = "维修工作统计列表(按月)", notes = "维修工作统计列表(按月)")
    public List<RepairWorkStatistics> selectRepairMothCount(@RequestBody WorkStatisticsVo workStatisticsVo) {
        return workStatisticsService.selectRepairMothCount(workStatisticsVo);
    }

    /**
     * 查询维修工作统计(按年)
     *
     * @param workStatisticsVo
     * @return
     */
    @RequestMapping(value = "selectRepairYearCount", method = RequestMethod.POST)
    @ApiOperation(value = "维修工作统计列表(按年)", notes = "维修工作统计列表(按年)")
    public List<RepairWorkStatistics> selectRepairYearCount(@RequestBody WorkStatisticsVo workStatisticsVo) {
        return workStatisticsService.selectRepairYearCount(workStatisticsVo);
    }

    /**
     * 查询配置工作统计(按月)
     *
     * @param workStatisticsVo
     * @return
     */
    @RequestMapping(value = "/configureStatisticsByMonth", method = RequestMethod.POST)
    @ApiOperation(value = "配置工作量统计(按月)", notes = "配置工作量统计(按月)")
    public List<ConfigureStatisticsDto> configureStatisticsByMonth(@RequestBody WorkStatisticsVo workStatisticsVo) {

        return workStatisticsService.configureStatisticsByMonth(workStatisticsVo);
    }

    /**
     * 查询配置工作统计(按年)
     *
     * @param workStatisticsVo
     * @return
     */
    @RequestMapping(value = "/configureStatisticsByYear", method = RequestMethod.POST)
    @ApiOperation(value = "配置工作量统计(按年)", notes = "配置工作量统计(按年)")
    public List<ConfigureStatisticsDto> configureStatisticsByYear(@RequestBody WorkStatisticsVo workStatisticsVo) {

        return workStatisticsService.configureStatisticsByYear(workStatisticsVo);
    }

    /**
     * 查询处置工作统计(按月)
     *
     * @param workStatisticsVo
     * @return
     */
    @RequestMapping(value = "selectHandleMothCount", method = RequestMethod.POST)
    @ApiOperation(value = "查询处置工作统计(按月)", notes = "查询处置工作统计(按月)")
    public List<HandleWorkStatistics> selectHandleMothCount(@RequestBody WorkStatisticsVo workStatisticsVo) {
        return workStatisticsService.selectHandleMothCount(workStatisticsVo);
    }

    /**
     * 查询处置工作统计(按年)
     *
     * @param workStatisticsVo
     * @return
     */
    @RequestMapping(value = "selectHandleYearCount", method = RequestMethod.POST)
    @ApiOperation(value = "查询处置工作统计(按年)", notes = "查询处置工作统计(按年)")
    public List<HandleWorkStatistics> selectHandleYearCount(@RequestBody WorkStatisticsVo workStatisticsVo) {
        return workStatisticsService.selectHandleYearCount(workStatisticsVo);
    }

    /**
     * 查询工作统计日期范围
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/statisticsDateRange", method = RequestMethod.POST)
    @ApiOperation(value = "查询工作统计日期范围", notes = "查询工作统计日期范围")
    public DateRangeDto statisticsDateRange(@RequestBody @Valid BasicVo<Integer> basicVo) {

        return workStatisticsService.statisticsDateRange(basicVo);
    }


}
