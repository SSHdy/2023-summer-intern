package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.Handle;
import com.ryzw.housemanager.entity.HandleWorkStatistics;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.HandleLsVo;
import com.ryzw.housemanager.vo.HandleVo;
import com.ryzw.housemanager.vo.WorkStatisticsVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 处置表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-05-24
 */
public interface HandleMapper extends BaseMapper<Handle> {

    /**
     * 处置申请详情(到房间)
     *
     * @param handleId
     * @return
     */
    HandleDetailDto handleDetailToHouse(@Param("handleId") Long handleId);

    /**
     * 处置申请详情(到楼栋)
     *
     * @param handleId
     * @return
     */
    HandleDetailDto handleDetailToBuild(@Param("handleId") Long handleId);

    /**
     * 查询处置总面积
     *
     * @param handleId
     * @return
     */
    Float selectAreaSum(@Param("handleId") Long handleId);

    /**
     * 处置申请列表
     *
     * @param page
     * @param handleVo
     * @return
     */
    Page<HandleDetailDto> handleList(Page<HandleDetailDto> page, @Param("handleVo") HandleVo handleVo);

    /**
     * 查询处置位置
     *
     * @param handleId
     * @return
     */
    List<BuildPositionDto> selectHandlePosition(@Param("handleId") Long handleId);

    /**
     * 查询可以处置的位置
     *
     * @param unitId
     * @return
     */
    List<HandlePositionDto> selectHandlePo(@Param("unitId") Integer unitId);

    /**
     * 修改时查询可以处置的位置
     *
     * @param handleId
     * @param unitId
     * @return
     */
    List<HandlePositionDto> selectHandlePoUp(@Param("handleId") Long handleId, @Param("unitId") Integer unitId);

    /**
     * 查询所有院落楼栋树
     *
     * @return
     */
    List<HandlePositionDto> selectAllHandlePo();

    /**
     * 查询处置列表（不包含出租出借）
     *
     * @param page
     * @param handleLsVo
     * @return
     */
    Page<HandleDetailDto> handleLs(Page page, @Param("hL") HandleLsVo handleLsVo);

    /**
     * 查询处置详情（不包含出租出借）
     *
     * @param basicVo
     * @return
     */
    HandleLsDto handleLsDetail(@Param("hL") BasicVo<Long> basicVo);

    /**
     * 查询处置工作统计审批数量、面积
     *
     * @param workStatisticsVo
     * @return
     */
    HandleWorkStatistics selectHandleCount(@Param("WorkStatistics") WorkStatisticsVo workStatisticsVo);

    /**
     * 查询符合条件的处置列表
     *
     * @param workStatisticsVo
     * @return
     */
    List<Handle> selectHandleIds(@Param("WorkStatistics") WorkStatisticsVo workStatisticsVo);

    /**
     * 查询处置工作统计日期范围
     *
     * @param unitIds
     * @return
     */
    DateRangeDto statisticsDateRange(@Param("unitIds") List<Long> unitIds);

    /**
     * 查询房间处置关系数量
     *
     * @param delHouseIdList
     * @return
     */
    int handleCount(@Param("delHouseIdList") List<String> delHouseIdList);

}
