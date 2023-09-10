package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.RepairApply;
import com.ryzw.housemanager.vo.*;
import org.bson.Document;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 维修申请表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-03-28
 */
public interface IRepairApplyService extends IService<RepairApply> {

    /**
     * 查询所有的维修信息
     *
     * @param repairListVo
     * @return
     */
    Page<RepairApplyDto> selectAllRepair(@Valid RepairListVo repairListVo);

    /**
     * 新增维修申请
     *
     * @param repairAddVo
     * @return
     */
    boolean addRepairApply(RepairAddVo repairAddVo);

    /**
     * 维修申请详情
     *
     * @param repairApplyId
     * @return
     */
    RepairApplyDetailDto selectRepairApplyDetail(Long repairApplyId);

    /**
     * 维修审批列表
     *
     * @param repairApprovalVo
     * @return
     */
    Page<RepairApprovalDto> repairApprovalList(@Valid RepairApprovalVo repairApprovalVo);

    /**
     * 审核申请审批
     *
     * @param repairFlowVo
     * @return
     */
    boolean saveSubmitRepair(@Valid RepairFlowVo repairFlowVo);

    /**
     * 修改维修申请下维修模块的面积
     *
     * @param repairModuleUpdateVo
     * @return
     */
    boolean updateRepairModule(RepairModuleUpdateVo repairModuleUpdateVo);

    /**
     * 删除维修模块的一条记录
     *
     * @param repairModuleId
     * @return
     */
    boolean deleteRepairModule(Long repairModuleId);

    /**
     * 修改维修申请信息
     *
     * @param repairApplyUpdateVo
     * @return
     */
    boolean updateRepairApply(RepairApplyUpdateVo repairApplyUpdateVo);

    /**
     * 维修信息详情集合
     *
     * @param repairApplyId
     * @return
     */
    RepairApprovalDetailDto repairApprovalDetail(Long repairApplyId);

    /**
     * 维修信息详情
     *
     * @param repairModuleId
     * @return
     */
    RepairMessageDetailDto repairMessageDetail(Long repairModuleId);

    /**
     * 维修项目详情
     *
     * @param id
     * @return
     */
    List<RepairProjectDto> projectDetail(Long id);

    /**
     * 查询相关维修记录
     *
     * @param repairModuleId
     * @return
     */
    List<RepairRecordDetailDto> selectRepairRecord(Long repairModuleId);

    /**
     * 查询某楼栋相关的维修模块记录
     *
     * @param buildId
     * @return
     */
    RepairBuildRecordDto selectRepairBuildRecord(Long buildId);

    /**
     * 文件下载
     *
     * @param downLoadFiletVo
     * @return
     */
    List<DownLoadDto> downLoad(DownLoadFiletVo downLoadFiletVo);

    /**
     * 查询项目评审信息
     *
     * @param projectReviewtVo
     * @return
     */
    ProjectReviewtDto projectReviewDetail(ProjectReviewtVo projectReviewtVo);

    /**
     * 查询项目预算信息
     *
     * @param repairApplyId
     * @return
     */
    ProjectBudgetDto projectbudgetDetail(ProjectReviewtVo projectReviewtVo);

    /**
     * 查询对应流程下的文件路径
     *
     * @param projectFileVo
     * @return
     */
    List<Document> projectFileAddress(ProjectFileVo projectFileVo);

    /**
     * 查询维修类型列表
     *
     * @param repairApplyId
     * @return
     */
    RepairStepDto selectRepairType(Long repairApplyId);


    /**
     * 零星维修 内控采购流程审批
     *
     * @param processHttpResultVo
     * @return
     */
    boolean procurementApproval(ProcessHttpResultVo processHttpResultVo);


    /**
     * 机关管理局 内控采购流程审批
     *
     * @param repairHttpResultVo
     * @return
     */
    boolean cgApproval(ProcessPayHttpResultVo repairHttpResultVo);

    /**
     * 领导审批
     *
     * @param leaderReviewVo
     * @return
     */
    List<Document> leaderReviewDetail(LeaderReviewVo leaderReviewVo);

    /**
     * 查询政府或发改委审批
     *
     * @param repairApplyId
     * @return
     */
    List<Document> goverReviewDetail(Long repairApplyId);

    /**
     * 查询项目施工设计
     *
     * @param repairApplyId
     * @return
     */
    DesignReviewDto designReviewDetail(Long repairApplyId);

    /**
     * 查询竣工验收信息
     *
     * @param repairApplyId
     * @return
     */
    CompletedProjectDto completedProject(Long repairApplyId);

    /**
     * 更新用户维修权限
     *
     * @param activityAuthorityVo
     * @return
     */
    boolean updateRepairAuthority(ActivityAuthorityVo activityAuthorityVo);

    /**
     * 维修项目信息添加
     *
     * @param constructManageVo
     * @return
     */
    boolean constructManage(ConstructManageVo constructManageVo);

    /**
     * 维修类型报表查询
     *
     * @param repairReportVo
     * @return
     */
    List<RepairReportDto> typeReport(RepairReportVo repairReportVo);

    /**
     * 查询某个用户所有审批记录
     *
     * @param approvalRecordVo
     * @return
     */
    Page<RepairApplyDto> selectApprovalRecord(ApprovalRecordVo approvalRecordVo);

    /**
     * 维修查看列表
     *
     * @param repairListVo
     * @return
     */
    Page<RepairApplyDto> leaderReviewList(RepairListVo repairListVo);

    /**
     * 查询某个用户审批详情
     *
     * @param approvalDetailVo
     * @return
     */
    List<StepNodeDto> selectApprovalDetail(ApprovalDetailVo approvalDetailVo);

    /**
     * 查询某栋楼关联的维修申请
     *
     * @param buildRepairVo
     * @return
     */
    List<BuildRepairApplyDto> buildRepairApplyList(BuildRepairVo buildRepairVo);

    /**
     * 查询某条维修申请的所有审批文件
     *
     * @param repairApplyId
     * @return
     */
    List<RepairApplyFilesDto> repairApplyFiles(Long repairApplyId);

    /**
     * 查询维修档案列表
     *
     * @param buildAndYardVo
     * @return
     */
    Page<RepairFilesDto> repairFilesList(BuildAndYardVo buildAndYardVo);

    /**
     * 获取内控采购流程完成步骤集合
     *
     * @param repairApplyId
     * @return
     */
    List<LinkedHashMap<String, Object>> getStepList(Long repairApplyId);

    /**
     * 获取内控采购流程步骤详情
     *
     * @param dbStepDetailVo
     * @return
     */
    Object dbStepDetail(DbStepDetailVo dbStepDetailVo);

    /**
     * 获取内控采购流程所有审批文件
     *
     * @param repairApplyId
     * @return
     */
    Object dbAllFiles(Long repairApplyId);

    /**
     * 获取内控预算事项科室预算明细
     * @param budgetDetailsVo
     * @return
     */
    List<Object> getSectionList(BudgetDetailsVo budgetDetailsVo);
}
