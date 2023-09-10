package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.Configure;
import com.ryzw.housemanager.entity.Handle;
import com.ryzw.housemanager.entity.RepairApply;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.ConfigureMapper;
import com.ryzw.housemanager.mapper.HandleMapper;
import com.ryzw.housemanager.service.IRepairApplyService;
import com.ryzw.housemanager.upload.FileOperateUtils;
import com.ryzw.housemanager.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>
 * 维修申请表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-03-28
 */
@RestController
@RequestMapping("/repairApply")
@Api(value = "RepairApplyController", description = "资产维修申请列表")
@Slf4j
public class RepairApplyController {

    @Autowired
    IRepairApplyService repairApplyService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ConfigureMapper configureMapper;
    @Autowired
    HandleMapper handleMapper;

    /**
     * 新增维修申请
     *
     * @param repairAddVo
     * @return
     */
    @RequestMapping(value = "addRepair", method = RequestMethod.POST)
    @ApiOperation(value = "新增维修申请", notes = "新增维修申请")
    public boolean addRepairApply(@RequestBody @Valid RepairAddVo repairAddVo) {

        return repairApplyService.addRepairApply(repairAddVo);

    }

    /**
     * 维修申请列表
     *
     * @param repairListVo
     * @return
     */
    @RequestMapping(value = "repairApplyList", method = RequestMethod.POST)
    @ApiOperation(value = "维修申请列表", notes = "维修申请列表")
    public Page<RepairApplyDto> repairApplyList(@RequestBody RepairListVo repairListVo) {
        return repairApplyService.selectAllRepair(repairListVo);
    }

    /**
     * 维修申请详情
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "repairApplyDetail", method = RequestMethod.POST)
    @ApiOperation(value = "维修申请详情", notes = "维修申请详情")
    public RepairApplyDetailDto repairApplyDetail(@RequestBody @Valid BasicVo<Long> basicVo) {
        return repairApplyService.selectRepairApplyDetail(basicVo.getId());
    }

    /**
     * 维修信息详情集合
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "repairApprovalDetail", method = RequestMethod.POST)
    @ApiOperation(value = "维修信息详情集合", notes = "维修信息详情集合")
    public RepairApprovalDetailDto repairApprovalDetail(@RequestBody @Valid BasicVo<Long> basicVo) {
        return repairApplyService.repairApprovalDetail(basicVo.getId());
    }

    /**
     * 维修信息详情（通过repairModuleId）
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "repairMessageDetail", method = RequestMethod.POST)
    @ApiOperation(value = "维修信息详情", notes = "维修信息详情")
    public RepairMessageDetailDto repairMessageDetail(@RequestBody @Valid BasicVo<Long> basicVo) {
        return repairApplyService.repairMessageDetail(basicVo.getId());
    }

    /**
     * 修改维修申请下维修模块的面积
     *
     * @param repairModuleUpdateVo
     * @return
     */
    @RequestMapping(value = "updateRepairModule", method = RequestMethod.POST)
    @ApiOperation(value = "修改维修模块的面积", notes = "修改维修模块的面积")
    public boolean updateRepairModule(@RequestBody @Valid RepairModuleUpdateVo repairModuleUpdateVo) {

        return repairApplyService.updateRepairModule(repairModuleUpdateVo);
    }

    /**
     * 删除维修模块的一条记录
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "deleteRepairModule", method = RequestMethod.POST)
    @ApiOperation(value = "删除维修模块的一条记录", notes = "删除维修模块的一条记录")
    public boolean deleteRepairModule(@RequestBody @Valid BasicVo<Long> basicVo) {

        return repairApplyService.deleteRepairModule(basicVo.getId());
    }

    /**
     * 修改维修申请信息
     *
     * @param repairApplyUpdateVo
     * @return
     */
    @RequestMapping(value = "updateRepairApply", method = RequestMethod.POST)
    @ApiOperation(value = "修改维修申请信息", notes = "修改维修申请信息")
    public boolean updateRepairApply(@RequestBody RepairApplyUpdateVo repairApplyUpdateVo) {

        return repairApplyService.updateRepairApply(repairApplyUpdateVo);
    }

    /**
     * 查询相关维修记录
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "repairRecord", method = RequestMethod.POST)
    @ApiOperation(value = "查询相关维修记录", notes = "查询相关维修记录")
    public List<RepairRecordDetailDto> repairRecord(@RequestBody @Valid BasicVo<Long> basicVo) {

        return repairApplyService.selectRepairRecord(basicVo.getId());
    }

    /**
     * 查询某楼栋相关的维修模块记录
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "repairBuildRecord", method = RequestMethod.POST)
    @ApiOperation(value = "查询某楼栋相关的维修模块记录", notes = "查询某楼栋相关的维修模块记录")
    public RepairBuildRecordDto repairBuildRecord(@RequestBody @Valid BasicVo<Long> basicVo) {

        return repairApplyService.selectRepairBuildRecord(basicVo.getId());
    }


    /**
     * 维修审批列表
     *
     * @param repairApprovalVo
     * @return
     */
    @ApiOperation(value = "查看维修审批列表", notes = "查看维修审批列表")
    @RequestMapping(value = "/repairApprovalList", method = RequestMethod.POST)
    public Page<RepairApprovalDto> repairApprovalList(@RequestBody @Valid RepairApprovalVo repairApprovalVo) {
        return repairApplyService.repairApprovalList(repairApprovalVo);
    }


    /**
     * 审核申请审批
     *
     * @param repairFlowVo
     * @return
     */
    @ApiOperation(value = "审核申请审批", notes = "审核申请审批")
    @RequestMapping(value = "/saveSubmitRepair", method = RequestMethod.POST)
    public boolean saveSubmitRepair(@RequestBody @Valid RepairFlowVo repairFlowVo) {
        return repairApplyService.saveSubmitRepair(repairFlowVo);
    }

    /**
     * 维修项目详情
     *
     * @param basicVo
     * @return
     */
    @ApiOperation(value = "维修项目详情", notes = "维修项目详情")
    @RequestMapping(value = "/projectDetail", method = RequestMethod.POST)
    public List<RepairProjectDto> projectDetail(@RequestBody BasicVo<Long> basicVo) {
        return repairApplyService.projectDetail(basicVo.getId());
    }

    /**
     * 维修项目信息添加
     *
     * @param constructManageVo
     * @return
     */
    @ApiOperation(value = "维修项目信息添加", notes = "维修项目信息添加")
    @RequestMapping(value = "/constructManage", method = RequestMethod.POST)
    public boolean constructManage(@RequestBody @Valid ConstructManageVo constructManageVo) {
        return repairApplyService.constructManage(constructManageVo);
    }

    /**
     * 文件下载
     *
     * @param downLoadFiletVo
     * @param response
     */
    @RequestMapping(value = "/downLoad", method = RequestMethod.POST)
    @ApiOperation(value = "文件下载", notes = "文件下载")
    public Object download(@RequestBody DownLoadFiletVo downLoadFiletVo, HttpServletResponse response) {
        List<AdressAndNameVo> addressList = downLoadFiletVo.getAddressList();
        if (CollectionUtils.isEmpty(addressList)) {
            throw new ServiceException("请选择有效下载文件");
        }
        List<DownLoadDto> addressListDto = repairApplyService.downLoad(downLoadFiletVo);
        //去除重复路径
        HashSet h = new HashSet(addressListDto);
        addressListDto.clear();
        addressListDto.addAll(h);
        String fileName = "";
        if (addressListDto.size() > 1) {
            //配置管理文件的下载名称
            if (downLoadFiletVo.getDownLoadType() != null && downLoadFiletVo.getConfigureId() != null) {
                Integer downLoadType = downLoadFiletVo.getDownLoadType();
                Configure configure = configureMapper.selectById(downLoadFiletVo.getConfigureId());
                if (downLoadType.equals(1)) {
                    fileName = "配置管理-" + configure.getUnitName() + ".zip";
                }
                if (downLoadType.equals(2)) {
                    fileName = "完善资料-" + configure.getUnitName() + ".zip";
                }
            }
            //维修管理文件的下载名称
            if (downLoadFiletVo.getDownLoadType() != null && downLoadFiletVo.getRepairApplyId() != null) {
                Integer downLoadType = downLoadFiletVo.getDownLoadType();
                Long repairApplyId = downLoadFiletVo.getRepairApplyId();
                RepairApply repairApply = repairApplyService.getById(repairApplyId);
                //获取维修申请的申请编号
                String repairApplyNo = repairApply.getRepairApplyNo();
                if (downLoadType.equals(4)) {
                    fileName = "审批原件" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(5)) {
                    fileName = "部门维修上传" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(19)) {
                    fileName = "预算维修上传" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(8)) {
                    fileName = "可行性报告" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(9)) {
                    fileName = "平面图效果图" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(10)) {
                    fileName = "领导审批" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(11)) {
                    fileName = "报政府发改委" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(12)) {
                    fileName = "设计图纸" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(17)) {
                    fileName = "规划许可" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(13)) {
                    fileName = "维修预算" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(14)) {
                    fileName = "预算评审" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(15)) {
                    fileName = "竣工报告" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(16)) {
                    fileName = "竣工图纸" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(20)) {
                    fileName = "维修档案" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(21)) {
                    fileName = "项目事前审批" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(22)) {
                    fileName = "采购审批" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(23)) {
                    fileName = "计划备案" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(24)) {
                    fileName = "采购文件" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(25)) {
                    fileName = "采购结果" + repairApplyNo + ".zip";
                }
                if (downLoadType.equals(26)) {
                    fileName = "合同备案" + repairApplyNo + ".zip";
                }
            }
            //处置管理文件的下载名称
            if (downLoadFiletVo.getHandleId() != null && downLoadFiletVo.getDownLoadType() != null) {
                Integer downLoadType = downLoadFiletVo.getDownLoadType();
                Long handleId = downLoadFiletVo.getHandleId();
                Handle handle = handleMapper.selectById(handleId);
                String handleNo = handle.getHandleNo();
                if (downLoadType.equals(1)) {
                    fileName = "审批资料" + handleNo + ".zip";
                }
                if (downLoadType.equals(2)) {
                    fileName = "转换用途登记资料" + handleNo + ".zip";
                }
                if (downLoadType.equals(3)) {
                    fileName = "拍卖资料" + handleNo + ".zip";
                }
                if (downLoadType.equals(4)) {
                    fileName = "拆除资料" + handleNo + ".zip";
                }
                if (downLoadType.equals(5)) {
                    fileName = "征收资料" + handleNo + ".zip";
                }
            }
        } else {
            fileName = addressListDto.get(0).getDownLoadName();
        }
        FileOperateUtils.fileToZip(response, addressListDto, fileName);
        return addressListDto;
    }


    /**
     * 通过mongodb查询维修申请相关的信息
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/repairDetailDb", method = RequestMethod.POST)
    @ApiOperation(value = "查询维修申请相关的信息", notes = "查询维修申请相关的信息")
    public Document repairDetailDb(@RequestBody @Valid BasicVo<Integer> basicVo) {
        try {
            Document document = mongoTemplate.findById(basicVo.getId(), Document.class, "applyExtend");
            return document;
        } catch (Exception ex) {
            log.error("repairDetailDbError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询项目预算信息
     *
     * @param projectReviewtVo
     * @return
     */
    @RequestMapping(value = "/projectBudgetDetail", method = RequestMethod.POST)
    @ApiOperation(value = "查询项目预算信息", notes = "查询项目预算信息")
    public ProjectBudgetDto projectbudgetDetail(@RequestBody @Valid ProjectReviewtVo projectReviewtVo) {
        return repairApplyService.projectbudgetDetail(projectReviewtVo);
    }

    /**
     * 查询项目评审信息
     *
     * @param projectReviewtVo
     * @return
     */
    @RequestMapping(value = "/projectReviewDetail", method = RequestMethod.POST)
    @ApiOperation(value = "查询项目评审信息", notes = "查询项目评审信息")
    public ProjectReviewtDto projectReviewDetail(@RequestBody ProjectReviewtVo projectReviewtVo) {
        return repairApplyService.projectReviewDetail(projectReviewtVo);
    }

    /**
     * 查询政府或发改委审批
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/goverReviewDetail", method = RequestMethod.POST)
    @ApiOperation(value = "查询政府或发改委审批", notes = "查询政府或发改委审批")
    public List<Document> goverReviewDetail(@RequestBody @Valid BasicVo<Long> basicVo) {
        return repairApplyService.goverReviewDetail(basicVo.getId());
    }

    /**
     * 查询项目施工设计
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/designReviewDetail", method = RequestMethod.POST)
    @ApiOperation(value = "查询项目施工设计", notes = "查询项目施工设计")
    public DesignReviewDto designReviewDetail(@RequestBody @Valid BasicVo<Long> basicVo) {
        return repairApplyService.designReviewDetail(basicVo.getId());
    }


    /**
     * 查询对应流程下的文件路径
     *
     * @param projectFileVo
     * @return
     */
    @RequestMapping(value = "/projectFileAddress", method = RequestMethod.POST)
    @ApiOperation(value = "查询对应流程下的文件路径", notes = "查询对应流程下的文件路径")
    public List<Document> projectFileAddress(@RequestBody @Valid ProjectFileVo projectFileVo) {
        return repairApplyService.projectFileAddress(projectFileVo);
    }


    /**
     * 查询维修类型列表
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/selectRepairType", method = RequestMethod.POST)
    @ApiOperation(value = "查询维修类型列表", notes = "查询维修类型列表")
    public RepairStepDto selectRepairType(@RequestBody @Valid BasicVo<Long> basicVo) {
        return repairApplyService.selectRepairType(basicVo.getId());
    }


    /**
     * 领导审批
     *
     * @param leaderReviewVo
     * @return
     */
    @RequestMapping(value = "/leaderReviewDetail", method = RequestMethod.POST)
    @ApiOperation(value = "领导审批", notes = "领导审批")
    public List<Document> leaderReviewDetail(@RequestBody @Valid LeaderReviewVo leaderReviewVo) {
        return repairApplyService.leaderReviewDetail(leaderReviewVo);
    }

    /**
     * 零星维修 内控采购流程审批
     *
     * @param processHttpResultVo
     * @return
     */
    @RequestMapping(value = "/procurementApproval", method = RequestMethod.POST)
    @ApiOperation(value = "零星维修 内控采购流程审批", notes = "零星维修 内控采购流程审批")
    public boolean procurementApproval(@RequestBody @Valid ProcessHttpResultVo processHttpResultVo) {
        return repairApplyService.procurementApproval(processHttpResultVo);
    }


    /**
     * 机关管理局 内控采购流程
     *
     * @param repairHttpResultVo
     * @return
     */
    @RequestMapping(value = "/cgApproval", method = RequestMethod.POST)
    @ApiOperation(value = "机关管理局 内控采购流程", notes = "机关管理局 内控采购流程")
    public boolean cgApproval(@RequestBody @Valid ProcessPayHttpResultVo repairHttpResultVo) {
        return repairApplyService.cgApproval(repairHttpResultVo);
    }

    /**
     * 查询竣工验收信息
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/completedProject", method = RequestMethod.POST)
    @ApiOperation(value = "查询竣工验收信息", notes = "查询竣工验收信息")
    public CompletedProjectDto completedProject(@RequestBody @Valid BasicVo<Long> basicVo) {
        return repairApplyService.completedProject(basicVo.getId());
    }

    /**
     * 更新用户维修权限
     *
     * @return
     */
    @RequestMapping(value = "/updateRepairAuthority", method = RequestMethod.POST)
    @ApiOperation(value = "更新用户维修权限", notes = "更新用户维修权限")
    public boolean updateRepairAuthority(@RequestBody @Valid ActivityAuthorityVo activityAuthorityVo) {
        return repairApplyService.updateRepairAuthority(activityAuthorityVo);
    }


    /**
     * 查询某个用户所有审批记录
     *
     * @param approvalRecordVo
     * @return
     */
    @RequestMapping(value = "/selectApprovalRecord", method = RequestMethod.POST)
    @ApiOperation(value = "查询某个用户所有审批记录", notes = "查询某个用户所有审批记录")
    public Page<RepairApplyDto> selectApprovalRecord(@RequestBody @Valid ApprovalRecordVo approvalRecordVo) {

        return repairApplyService.selectApprovalRecord(approvalRecordVo);
    }

    /**
     * 查询某个用户审批详情
     *
     * @param approvalDetailVo
     * @return
     */
    @RequestMapping(value = "/selectApprovalDetail", method = RequestMethod.POST)
    @ApiOperation(value = "查询某个用户审批详情", notes = "查询某个用户审批详情")
    public List<StepNodeDto> selectApprovalDetail(@RequestBody @Valid ApprovalDetailVo approvalDetailVo) {

        return repairApplyService.selectApprovalDetail(approvalDetailVo);
    }


    /**
     * 维修查看列表
     *
     * @param repairListVo
     * @return
     */
    @RequestMapping(value = "/leaderReviewList", method = RequestMethod.POST)
    @ApiOperation(value = "维修查看列表", notes = "维修查看列表")
    public Page<RepairApplyDto> leaderReviewList(@RequestBody @Valid RepairListVo repairListVo) {

        return repairApplyService.leaderReviewList(repairListVo);
    }

    /**
     * 查询某栋楼关联的维修申请
     *
     * @param buildRepairVo
     * @return
     */
    @RequestMapping(value = "/buildRepairApplyList", method = RequestMethod.POST)
    @ApiOperation(value = "查询某栋楼关联的维修申请", notes = "查询某栋楼关联的维修申请")
    public List<BuildRepairApplyDto> buildRepairApplyList(@RequestBody @Valid BuildRepairVo buildRepairVo) {

        return repairApplyService.buildRepairApplyList(buildRepairVo);
    }

    /**
     * 查询某条维修申请的所有审批文件
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/repairApplyFiles", method = RequestMethod.POST)
    @ApiOperation(value = "查询某条维修申请的所有审批文件", notes = "查询某条维修申请的所有审批文件")
    public List<RepairApplyFilesDto> repairApplyFiles(@RequestBody @Valid BasicVo<Long> basicVo) {

        return repairApplyService.repairApplyFiles(basicVo.getId());
    }

    /**
     * 查询维修档案列表
     *
     * @param buildAndYardVo
     * @return
     */
    @RequestMapping(value = "/repairFilesList", method = RequestMethod.POST)
    @ApiOperation(value = "查询维修档案列表", notes = "查询维修档案列表")
    public Page<RepairFilesDto> repairFilesList(@RequestBody BuildAndYardVo buildAndYardVo) {

        return repairApplyService.repairFilesList(buildAndYardVo);
    }

    /**
     * 获取内控采购流程完成步骤集合
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/dbStepList", method = RequestMethod.POST)
    @ApiOperation(value = "获取内控采购流程完成步骤集合", notes = "获取内控采购流程完成步骤集合")
    public List<LinkedHashMap<String, Object>> dbStepList(@RequestBody @Valid BasicVo<Long> basicVo) {

        return repairApplyService.getStepList(basicVo.getId());
    }

    /**
     * 获取内控采购流程步骤详情
     *
     * @param dbStepDetailVo
     * @return
     */
    @RequestMapping(value = "/dbStepDetail", method = RequestMethod.POST)
    @ApiOperation(value = "获取内控采购流程步骤详情", notes = "获取内控采购流程步骤详情")
    public Object dbStepDetail(@RequestBody DbStepDetailVo dbStepDetailVo) {

        return repairApplyService.dbStepDetail(dbStepDetailVo);
    }

    /**
     * 获取内控采购流程所有审批文件--关联到一条维修申请上
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/dbAllFiles", method = RequestMethod.POST)
    @ApiOperation(value = "获取内控采购流程所有审批文件", notes = "获取内控采购流程所有审批文件")
    public Object dbAllFiles(@RequestBody @Valid BasicVo<Long> basicVo) {

        return repairApplyService.dbAllFiles(basicVo.getId());
    }

    /**
     * 获取内控预算事项科室预算明细
     *
     * @return
     */
    @RequestMapping(value = "/getSectionList", method = RequestMethod.POST)
    @ApiOperation(value = "获取内控预算事项科室预算明细", notes = "获取内控预算事项科室预算明细")
    public List<Object> getSectionList(@RequestBody @Valid BudgetDetailsVo budgetDetailsVo) {
        return repairApplyService.getSectionList(budgetDetailsVo);
    }


}
