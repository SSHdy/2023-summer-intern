package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.RepairApply;
import com.ryzw.housemanager.entity.RepairWorkStatistics;
import com.ryzw.housemanager.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 维修申请表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-03-28
 */
public interface RepairApplyMapper extends BaseMapper<RepairApply> {

    /**
     * 维修申请信息列表
     *
     * @param page
     * @param repairListVo
     * @return
     */
    Page<RepairApplyDto> selectAllRepairApply(Page<RepairApplyDto> page, @Param("RepairApply") RepairListVo repairListVo);

    /**
     * 维修审批列表
     *
     * @param page
     * @param repairApprovalVo
     * @return
     */
    Page<RepairApprovalDto> selectRepairApproval(Page<RepairApprovalDto> page, @Param("RepairApply") RepairApprovalVo repairApprovalVo);

    /**
     * 查询维修申请详情
     *
     * @param repairApplyId
     * @return
     */
    RepairApplyDetailDto selectRepairApplyDetail(@Param("repairApplyId") Long repairApplyId);

    /**
     * 维修信息详情集合
     *
     * @param repairApplyId
     * @return
     */
    RepairApprovalDetailDto selectRepairApprovalDetail(@Param("repairApplyId") Long repairApplyId);

    /**
     * 维修信息详情
     *
     * @param repairModuleId
     * @return
     */
    RepairMessageDetailDto selectRepairMessageDetail(@Param("repairModuleId") Long repairModuleId);

    /**
     * 查询相关维修记录
     *
     * @param yardId
     * @param buildId
     * @param houseId
     * @param floorName
     * @return
     */
    List<RepairRecordDetailDto> selectRepairRecord(@Param("yardId") Long yardId, @Param("buildId") Long buildId, @Param("houseId")
            String houseId, @Param("floorName") Integer floorName, @Param("repairPartId") Long repairPartId);

    /**
     * 维修项目详情
     *
     * @param id
     * @return
     */
    List<RepairProjectDto> projectDetail(@Param("id") Long id);

    /**
     * 查询某楼栋相关的维修模块记录
     *
     * @param buildId
     * @return
     */
    RepairBuildRecordDto selectRepairBuildRecord(@Param("buildId") Long buildId);

    /**
     * 查询某个维修申请对应的维修模块总面积
     *
     * @param repairApplyId
     * @return
     */
    Double selectTotalArea(@Param("repairApplyId") Long repairApplyId);

    /**
     * 查询单位集合
     *
     * @param repairApplyId
     * @return
     */
    List<String> selectUnit(@Param("repairApplyId") Long repairApplyId);

    /**
     * 维修类型报表查询
     *
     * @param repairReportVo
     * @return
     */
    List<RepairReportDto> typeReport(@Param("type") RepairReportVo repairReportVo);

    /**
     * 维修查看列表
     *
     * @param page
     * @param repairListVo
     * @return
     */
    Page<RepairApplyDto> leaderReviewList(Page<RepairApplyDto> page, @Param("RepairApply") RepairListVo repairListVo);

    /**
     * 查询维修内容集合
     *
     * @param repairApplyId
     * @return
     */
    List<String> selectTargetList(@Param("repairApplyId") Long repairApplyId);

    /**
     * 查询某个用户所有审批记录
     *
     * @param page
     * @param userId
     * @param repairApplyIdList
     * @return
     */
    Page<RepairApplyDto> selectApprovalRecord(Page<RepairApplyDto> page, @Param("userId") Long userId,
                                              @Param("repairApplyIdList") List<Long> repairApplyIdList);

    /**
     * 查询维修工作统计审批数量、面积、金额
     *
     * @param workStatisticsVo
     * @return
     */
    RepairWorkStatistics selectRepairCount(@Param("WorkStatistics") WorkStatisticsVo workStatisticsVo);

    /**
     * 查询维修工作统计日期范围
     *
     * @param unitIds
     * @return
     */
    DateRangeDto statisticsDateRange(@Param("unitIds") List<Long> unitIds);

    /**
     * 查询某栋楼关联的维修申请
     *
     * @param buildRepairVo
     * @return
     */
    List<BuildRepairApplyDto> buildRepairApplyList(@Param("buildRepairVo") BuildRepairVo buildRepairVo);

    /**
     * 查询维修档案列表
     *
     * @param page
     * @param buildAndYardVo
     * @return
     */
    Page<RepairFilesDto> repairFilesList(Page<RepairFilesDto> page, @Param("buildAndYardVo") BuildAndYardVo buildAndYardVo);
}
