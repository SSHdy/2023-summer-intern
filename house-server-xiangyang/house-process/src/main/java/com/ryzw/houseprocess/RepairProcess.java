package com.ryzw.houseprocess;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 *     维修申请工作流  服务类
 * </p>
 *
 * @author wj
 * @since 2019/3/6 0006
 */
@Service
public class RepairProcess extends CommonProcess {

    private static final String REPAIR_PROCESS_DEFINE_KEY = "repairProcess";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;


    /**
     * 启动流程实例
     * @param repairId  维修申请ID
     * @param userId  当前登录人ID
     */
    public String startRepairProcess(Long repairId,String repairApplicant, Long userId,List<String> userIds) {
        //使用流程变量设置下一个任务的办理人
        Map<String, Object> variable = new HashMap();
        variable.put("userId",userId);
        variable.put("repairApplicant",repairApplicant);

        //让启动的流程（流程实例）关联业务
        String objId = REPAIR_PROCESS_DEFINE_KEY+"."+repairId;
        variable.put("objId", objId);
        //启动流程实例
        ProcessInstance vacationInstance =  runtimeService.startProcessInstanceByKey(REPAIR_PROCESS_DEFINE_KEY,objId,variable);
        Task task = taskService.createTaskQuery().processInstanceId(vacationInstance.getId()).singleResult();
        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        Map<String, Object> variables = new HashMap<String,Object>();
        variables.put("userIds",userIds);
        variables.put("repairApplicant",repairApplicant);
        taskService.complete(task.getId(),variables);
        return vacationInstance.getId();
    }


    /**
     * 使用当前用户ID正在执行的任务表，获取当前任务的维修ids
     * @param userId
     * @return
     */
    public List<RepairWorkFlow> findTaskListById(Long userId) {
        List<RepairWorkFlow> repairList = new ArrayList<RepairWorkFlow>();
        List<Task> taskList = taskService.createTaskQuery()
                    .processDefinitionKey(REPAIR_PROCESS_DEFINE_KEY)
                    .taskCandidateUser(String.valueOf(userId))
                    .orderByTaskCreateTime().desc()
                    .list();
        if(taskList!=null && taskList.size() > 0){
            for(Task task :taskList ){
                RepairWorkFlow repair = new RepairWorkFlow();
                //使用任务对象Task获取流程实例ID
                String processInstanceId = task.getProcessInstanceId();
                //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
                ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult();
                //使用流程实例对象获取BUSINESS_KEY
                String buniness_key = pi.getBusinessKey();
                //获取BUSINESS_KEY对应的主键ID，使用主键ID，查询维修申请对象（repairProcess.1）
                String id = "";
                if(StringUtils.isNotBlank(buniness_key)){
                    //截取字符串，取buniness_key小数点的第2个值
                    id = buniness_key.split("\\.")[1];
                    repair.setRepairId(Long.parseLong(id));
                    repair.setTaskId(task.getId());
                    repairList.add(repair);
                }
            }
        }
        return repairList;
    }


    /**
     * 使用维修ID，查询历史批注信息
     * @param repairId
     * @return
     */
    public List<Comment> findCommentByRepairId(Long repairId){
        //组织流程表中的字段中的值
        String objId = REPAIR_PROCESS_DEFINE_KEY+"."+repairId;
        //使用历史的流程变量查询，返回历史的流程变量的对象，获取流程实例ID
        HistoricVariableInstance hvi = historyService.createHistoricVariableInstanceQuery()
                .variableValueEquals("objId", objId).singleResult();
        //流程实例ID
        String processInstanceId = hvi.getProcessInstanceId();
        List<Comment> list = taskService.getProcessInstanceComments(processInstanceId);
        return list;
    }


    /**
     * 业务科室审批
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveThreeSubmitTask(RepairWorkFlow repairWorkFlow){
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(repairWorkFlow.getUserId()));
        if(StringUtils.isNotBlank(repairWorkFlow.getComment())){
            taskService.addComment(repairWorkFlow.getTaskId(), repairWorkFlow.getProcessInstanceId(), repairWorkFlow.getComment());
        }

        //在完成任务之前，设置流程变量，按照连线的名称，去完成任务
        Map<String, Object> variables = new HashMap();
        variables.put("auditState", repairWorkFlow.getAuditState());
        variables.put("departmentUser", repairWorkFlow.getCurrentUser());

        //实施专项维修
        if(repairWorkFlow.getAuditState().equals(1)){
            variables.put("pwUserIds", repairWorkFlow.getPwUserIds());
        }

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(repairWorkFlow.getTaskId(), variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(repairWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }

    /**
     * 零星维修 内控采购流程审批
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveEighteenSubmitTask(RepairWorkFlow repairWorkFlow){
        //查询执行对象ID
        Execution execution = runtimeService.createExecutionQuery()
                //当前活动的id，对应receiveTask.bpmn文件中的活动节点id的属性值
                .processInstanceId(repairWorkFlow.getProcessInstanceId()).activityId("receiveTask1").singleResult();

        //向后执行一步，如果流程处于等待状态，使得流程继续执行
        Map<String, Object> receiveVariable = new HashMap();
        receiveVariable.put("auditState", repairWorkFlow.getAuditState());
        receiveVariable.put("ptApprovalUser", repairWorkFlow.getCurrentUser());
        runtimeService.trigger(execution.getId(),receiveVariable);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(repairWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }


    /**
     * 党组会审批
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveFourSubmitTask(RepairWorkFlow repairWorkFlow){
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(repairWorkFlow.getUserId()));
        if(StringUtils.isNotBlank(repairWorkFlow.getComment())){
            taskService.addComment(repairWorkFlow.getTaskId(), repairWorkFlow.getProcessInstanceId(), repairWorkFlow.getComment());
        }

        //在完成任务之前，设置流程变量，按照连线的名称，去完成任务
        Map<String, Object> variables = new HashMap();
        variables.put("auditState", repairWorkFlow.getAuditState());
        variables.put("dzhApprovalUser", repairWorkFlow.getCurrentUser());
        //自费维修上传维修方案
        if(repairWorkFlow.getAuditState().equals(1)){
            variables.put("repairPlanUserIds", repairWorkFlow.getRepairPlanUserIds());
        }

        if(repairWorkFlow.getAuditState().equals(3)){
            variables.put("researchUserIds", repairWorkFlow.getResearchUserIds());
        }

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(repairWorkFlow.getTaskId(), variables);


        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(repairWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }


    /**
     * 自费维修上传
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveFiveSubmitTask(RepairWorkFlow repairWorkFlow){
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(repairWorkFlow.getUserId()));

        //在完成任务之前，设置流程变量，去完成任务
        Map<String, Object> variables = new HashMap();
        variables.put("repairPlanUser", repairWorkFlow.getCurrentUser());
        variables.put("approvalRepairUserIds", repairWorkFlow.getApprovalRepairUserIds());

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(repairWorkFlow.getTaskId(), variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(repairWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }

    /**
     * 自费维修审批
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveSixSubmitTask(RepairWorkFlow repairWorkFlow){
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(repairWorkFlow.getUserId()));
        if(StringUtils.isNotBlank(repairWorkFlow.getComment())){
            taskService.addComment(repairWorkFlow.getTaskId(), repairWorkFlow.getProcessInstanceId(), repairWorkFlow.getComment());
        }

        //在完成任务之前，设置流程变量，按照连线的名称，去完成任务
        Map<String, Object> variables = new HashMap();
        variables.put("auditState", repairWorkFlow.getAuditState());
        variables.put("arpUser", repairWorkFlow.getCurrentUser());
        //同意维修上传方案
        if(repairWorkFlow.getAuditState().equals(1)){
            variables.put("budgetPlanUserIds", repairWorkFlow.getBudgetPlanUserIds());
        }

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(repairWorkFlow.getTaskId(), variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(repairWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }


    /**
     * 自费维修上传预算方案
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveNineteenSubmitTask(RepairWorkFlow repairWorkFlow){
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(repairWorkFlow.getUserId()));

        //在完成任务之前，设置流程变量，去完成任务
        Map<String, Object> variables = new HashMap();
        variables.put("ysRepairPlanUser", repairWorkFlow.getCurrentUser());
        variables.put("approvalBudgetUserIds", repairWorkFlow.getApprovalBudgetUserIds());

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(repairWorkFlow.getTaskId(), variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(repairWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }


    /**
     * 审批预算方案
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveTwentySubmitTask(RepairWorkFlow repairWorkFlow){
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(repairWorkFlow.getUserId()));

        //在完成任务之前，设置流程变量，按照连线的名称，去完成任务
        Map<String, Object> variables = new HashMap();
        variables.put("auditState", repairWorkFlow.getAuditState());
        variables.put("approvalYsPlanUser", repairWorkFlow.getCurrentUser());
        //同意维修上传预算方案,流程结束

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(repairWorkFlow.getTaskId(), variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(repairWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }

    /**
     * 可行性研究审批
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveEightSubmitTask(RepairWorkFlow repairWorkFlow){
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(repairWorkFlow.getUserId()));
        Map<String, Object> variables = new HashMap();
        variables.put("researchApprovalUser", repairWorkFlow.getCurrentUser());
        variables.put("leaderApprovalUserIds", repairWorkFlow.getLeaderApprovalUserIds());

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(repairWorkFlow.getTaskId(),variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(repairWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }

    /**
     * 领导审批
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveNineSubmitTask(RepairWorkFlow repairWorkFlow){
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(repairWorkFlow.getUserId()));

        //向后执行一步，如果流程处于等待状态，使得流程继续执行
        Map<String, Object> variables = new HashMap();
        variables.put("auditState", repairWorkFlow.getAuditState());
        variables.put("leaderApprovalUser", repairWorkFlow.getCurrentUser());
        if(repairWorkFlow.getAuditState() .equals(1)){
            variables.put("gtUserIds", repairWorkFlow.getGtUserIds());
        }

        if(repairWorkFlow.getAuditState().equals(4)){
            variables.put("pcdUserIds", repairWorkFlow.getPcdUserIds());
        }

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(repairWorkFlow.getTaskId(),variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(repairWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }


    /**
     * 政府或发改委审批
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveTwentyOneSubmitTask(RepairWorkFlow repairWorkFlow){
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(repairWorkFlow.getUserId()));

        //在完成任务之前，设置流程变量，按照连线的名称，去完成任务
        Map<String, Object> variables = new HashMap();
        variables.put("auditState", repairWorkFlow.getAuditState());
        variables.put("governmentApprovalUser", repairWorkFlow.getCurrentUser());
        //政府或发改委同意
        if(repairWorkFlow.getAuditState() .equals(1)){
            variables.put("pcdUserIds", repairWorkFlow.getPcdUserIds());
        }
        //不同意,流程结束

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(repairWorkFlow.getTaskId(), variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(repairWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }

    /**
     * 项目施工设计
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveTwentyTwoSubmitTask(RepairWorkFlow repairWorkFlow){
        //获取流程实例ID
        String processInstanceId = repairWorkFlow.getProcessInstanceId();
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(repairWorkFlow.getUserId()));

        //在完成任务之前，设置流程变量，按照连线的名称，去完成任务
        Map<String, Object> variables = new HashMap();
        variables.put("detailDesignUser", repairWorkFlow.getCurrentUser());
        variables.put("pdUserIds", repairWorkFlow.getPdUserIds());

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(repairWorkFlow.getTaskId(), variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        return pi;
    }

    /**
     * 项目预算
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveTwentyThreeSubmitTask(RepairWorkFlow repairWorkFlow){
        //获取流程实例ID
        String processInstanceId = repairWorkFlow.getProcessInstanceId();
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(repairWorkFlow.getUserId()));

        //在完成任务之前，设置流程变量，按照连线的名称，去完成任务
        Map<String, Object> variables = new HashMap();
        variables.put("projectBudgetUser", repairWorkFlow.getCurrentUser());
        variables.put("prUserIds", repairWorkFlow.getPrUserIds());

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(repairWorkFlow.getTaskId(), variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        return pi;
    }

    /**
     * 项目评审
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveTwentyFourSubmitTask(RepairWorkFlow repairWorkFlow){
        //获取流程实例ID
        String processInstanceId = repairWorkFlow.getProcessInstanceId();
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(repairWorkFlow.getUserId()));

        Map<String, Object> variables = new HashMap();
        variables.put("projectReviewUser", repairWorkFlow.getCurrentUser());

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(repairWorkFlow.getTaskId(),variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        return pi;
    }

    /**
     * 机关管理局 内控采购流程审批
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveElevenSubmitTask(RepairWorkFlow repairWorkFlow){
        //获取流程实例ID
        String processInstanceId = repairWorkFlow.getProcessInstanceId();

        Map<String, Object> variables = new HashMap();
        variables.put("cgApprovalUser", repairWorkFlow.getCurrentUser());
        variables.put("leaderUserIds", repairWorkFlow.getLeaderUserIds());

        //查询执行对象ID
        Execution execution = runtimeService.createExecutionQuery()
                //当前活动的id，对应receiveTask.bpmn文件中的活动节点id的属性值
                .processInstanceId(processInstanceId).activityId("receiveTask3").singleResult();
        runtimeService.trigger(execution.getId(),variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        return pi;
    }

    /**
     * 指定项目负责人
     * @param repairWorkFlow
     * @return
     */
    public ProcessInstance saveTwentyFiveSubmitTask(RepairWorkFlow repairWorkFlow){
        //获取流程实例ID
        String processInstanceId = repairWorkFlow.getProcessInstanceId();
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(repairWorkFlow.getUserId()));

        Map<String, Object> variables = new HashMap();
        variables.put("leaderUser", repairWorkFlow.getCurrentUser());

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(repairWorkFlow.getTaskId(),variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        return pi;
    }


}
