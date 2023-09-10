package com.ryzw.housemanager.service.impl;

import com.ryzw.housemanager.dto.MenuChildAllDto;
import com.ryzw.housemanager.dto.ProcessHistoryDto;
import com.ryzw.housemanager.dto.ProcessNumDto;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IProcessService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.houseprocess.CommonProcess;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     公共流程 服务实现类
 * </p>
 *
 * @author wj
 * @since 2019-05-29 0029
 */
@Service
@Slf4j
public class ProcessServiceImpl implements IProcessService {

    @Autowired
    private CommonProcess commonProcess;

    @Autowired
    private IAuthorityUnitService authorityUnitService;


    @Override
    public List<ProcessHistoryDto> selectProcessHistory(String processInstanceId) {
        try {
            List<ProcessHistoryDto> repairHistoryList = new ArrayList<>();
            List<HistoricActivityInstance> historicActivityInstanceList = commonProcess.findHistoryActivity(processInstanceId);
            if (CollectionUtils.isNotEmpty(historicActivityInstanceList)) {
                for (HistoricActivityInstance hisInstance : historicActivityInstanceList) {
                    ProcessHistoryDto processHistoryDto = new ProcessHistoryDto();
                    processHistoryDto.setStepName(hisInstance.getActivityName());
                    if (hisInstance.getEndTime() != null) {
                        processHistoryDto.setProcessingTime(hisInstance.getEndTime());
                    }

                    List<HistoricDetail> historicDetailList = commonProcess.findHistoricDetail(hisInstance.getId());
                    if(CollectionUtils.isNotEmpty(historicDetailList)){
                        for(HistoricDetail historicDetail : historicDetailList){
                            HistoricVariableUpdate historicVariableUpdate = (HistoricVariableUpdate)historicDetail;
                            //配置申请人
                            if(historicVariableUpdate.getVariableName().equals("configureApplicant") ||
                                    //机关管理局用房情况审查
                                    historicVariableUpdate.getVariableName().equals("unitReviewUser") ||
                                    //拟定分配方案
                                    historicVariableUpdate.getVariableName().equals("drawPlanUser") ||
                                    //领导审批
                                    historicVariableUpdate.getVariableName().equals("leaderApprovalUser") ||
                                    //完善信息
                                    historicVariableUpdate.getVariableName().equals("improvingDataUser") ||
                                    //处置申请人
                                    historicVariableUpdate.getVariableName().equals("handleApplicant") ||
                                    //申请初审
                                    historicVariableUpdate.getVariableName().equals("initApplyUser") ||
                                    //财政或人民政府审批
                                    historicVariableUpdate.getVariableName().equals("financeApprovalUser") ||
                                    //转换用途
                                    historicVariableUpdate.getVariableName().equals("conPurposesUser") ||
                                    //拍卖
                                    historicVariableUpdate.getVariableName().equals("auctionUser") ||
                                    //拆除
                                    historicVariableUpdate.getVariableName().equals("dismantleUser") ||
                                    //征收
                                    historicVariableUpdate.getVariableName().equals("levyUser") ||
                                    //维修申请人
                                    historicVariableUpdate.getVariableName().equals("repairApplicant") ||
                                    //业务科室审批
                                    historicVariableUpdate.getVariableName().equals("departmentUser") ||
                                    //内控项目事前审批
                                    historicVariableUpdate.getVariableName().equals("ptApprovalUser") ||
                                    //线下党组会审批
                                    historicVariableUpdate.getVariableName().equals("dzhApprovalUser") ||
                                    //自费维修上传维修方案
                                    historicVariableUpdate.getVariableName().equals("repairPlanUser") ||
                                    //审批维修方案
                                    historicVariableUpdate.getVariableName().equals("arpUser") ||
                                    //自费维修上传预算方案
                                    historicVariableUpdate.getVariableName().equals("ysRepairPlanUser") ||
                                    //审批预算方案
                                    historicVariableUpdate.getVariableName().equals("approvalYsPlanUser") ||
                                    //项目方案设计可行性研究
                                    historicVariableUpdate.getVariableName().equals("researchApprovalUser") ||
                                    //报政府或发改委
                                    historicVariableUpdate.getVariableName().equals("governmentApprovalUser") ||
                                    //项目施工设计
                                    historicVariableUpdate.getVariableName().equals("detailDesignUser") ||
                                    //项目预算
                                    historicVariableUpdate.getVariableName().equals("projectBudgetUser") ||
                                    //项目评审
                                    historicVariableUpdate.getVariableName().equals("projectReviewUser") ||
                                    //内控采购
                                    historicVariableUpdate.getVariableName().equals("cgApprovalUser") ||
                                    //指定项目负责人
                                    historicVariableUpdate.getVariableName().equals("leaderUser")){
                                processHistoryDto.setTransactors(String.valueOf(historicVariableUpdate.getValue()));
                            }
                        }
                    }

                    repairHistoryList.add(processHistoryDto);
                }
            }
            return repairHistoryList;
        } catch (Exception ex) {
            log.error("selectProcessHistoryError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 统计待办事项数量
     *
     * @param userId 用户ID
     * @return
     */
    @Override
    public Long todoListNum(Long userId) {
        try {
            //所有流程的待办事项数量
            Long processNum = commonProcess.todoListNum(userId);
            //维修待办事项数量
            Long repairNum = commonProcess.todoListRepairNum(userId);
            //处置待办事项数量
            Long handleNum = commonProcess.todoListHandleNum(userId);
            //配置待办事项数量
            Long configureNum = commonProcess.todoListConfigureNum(userId);
            BasicVo<Long> basicVo = new BasicVo<>();
            basicVo.setId(userId);

            //流程待办事项菜单
            Map<String,Object> processMenu = this.processMenu(basicVo);

            if(MapUtils.isNotEmpty(processMenu)){
                //没有维修相关审批权限
                if(processMenu.get("repairMenu") == null){
                    processNum = processNum - repairNum;
                }
                //没有处置相关审批权限
                if(processMenu.get("handleMenu") == null){
                    processNum = processNum - handleNum;
                }
                //没有配置相关审批权限
                if(processMenu.get("configureMenu") == null){
                    processNum = processNum - configureNum;
                }
            }else{
                //所有流程待办事项菜单都没有
                processNum = 0L;
            }
            return processNum;
        } catch (Exception ex) {
            log.error("todoListNumError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 统计每个待办事项数量
     * @param userId
     * @return
     */
    @Override
    public ProcessNumDto todoListProcessNum(Long userId) {
        try {
            BasicVo<Long> basicVo = new BasicVo<>();
            basicVo.setId(userId);
            //流程待办事项菜单
            Map<String,Object> processMenu = this.processMenu(basicVo);
            //获取维修流程待办事项数量
            Long repairNum = commonProcess.todoListRepairNum(userId);
            //没有维修流程待办事项菜单
            if(processMenu.get("repairMenu") == null){
                repairNum = 0L;
            }
            //获取处置流程待办事项数量
            Long handleNum = commonProcess.todoListHandleNum(userId);
            //没有处置流程待办事项菜单
            if(processMenu.get("handleMenu") == null){
                handleNum = 0L;
            }
            //获取配置流程待办事项数量
            Long configureNum = commonProcess.todoListConfigureNum(userId);
            //没有配置流程待办事项菜单
            if(processMenu.get("configureMenu") == null){
                configureNum = 0L;
            }
            ProcessNumDto processNumDto = new ProcessNumDto();
            processNumDto.setRepairNum(repairNum).setHandleNum(handleNum).setConfigureNum(configureNum);
            return processNumDto;
        }catch (Exception ex){
            log.error("todoListProcessNumError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 流程待办事项菜单
     * @param basicVo
     * @return
     */
    public  Map<String,Object> processMenu(BasicVo<Long> basicVo){
        try {
            Map<String,Object> menuMap = new HashMap<>();
            List<MenuChildAllDto> menuChildAllDtoList = authorityUnitService.getListMenu(basicVo);
            if(CollectionUtils.isNotEmpty(menuChildAllDtoList)){
                for(MenuChildAllDto menuChildAllDto : menuChildAllDtoList){
                    if(CollectionUtils.isNotEmpty(menuChildAllDto.getChildren())){
                        for(MenuChildAllDto menuChildAll :menuChildAllDto.getChildren()){
                            if(StringUtils.isNotBlank(menuChildAll.getKey())){
                                if(menuChildAll.getKey().equals("waittingThing")){
                                    menuMap.put("repairMenu",menuChildAll.getKey());
                                }
                                if(menuChildAll.getKey().equals("disposeBacklog")){
                                    menuMap.put("handleMenu",menuChildAll.getKey());
                                }
                                if(menuChildAll.getKey().equals("configBacklog")){
                                    menuMap.put("configureMenu",menuChildAll.getKey());
                                }
                            }
                        }
                    }
                }
            }
            return menuMap;
        }catch (Exception ex){
            log.error("processMenuError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}
