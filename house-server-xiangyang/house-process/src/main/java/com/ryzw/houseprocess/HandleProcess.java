package com.ryzw.houseprocess;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *    处置工作流 服务类
 * </p>
 *
 * @author wj
 * @since 2019-05-24 0024
 */
@Service
public class HandleProcess extends CommonProcess{

    private static final String HANDLE_PROCESS_DEFINE_KEY = "handleProcess";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;



    /**
     * 启动流程实例
     * @param handleId  处置申请ID
     * @param userId  当前登录人ID
     */
    public String startHandleProcess(Long handleId,String handleApplicant, Long userId,List<String> userIds) {
        //使用流程变量设置下一个任务的办理人
        Map<String, Object> variable = new HashMap();
        variable.put("userId",userId);
        variable.put("handleApplicant",handleApplicant);

        //让启动的流程（流程实例）关联业务
        String objId = HANDLE_PROCESS_DEFINE_KEY+"."+handleId;
        variable.put("objId", objId);
        //启动流程实例
        ProcessInstance vacationInstance =  runtimeService.startProcessInstanceByKey(HANDLE_PROCESS_DEFINE_KEY,objId,variable);
        Task task = taskService.createTaskQuery().processInstanceId(vacationInstance.getId()).singleResult();
        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        Map<String, Object> variables = new HashMap<String,Object>();
        variables.put("userIds",userIds);
        variables.put("handleApplicant",handleApplicant);
        taskService.complete(task.getId(),variables);
        return vacationInstance.getId();
    }


    /**
     * 使用当前用户ID正在执行的任务表，获取当前任务的处置ids
     * @param userId
     * @return
     */
    public List<HandleWorkFlow> findTaskListById(Long userId) {
        List<HandleWorkFlow> handleList = new ArrayList<HandleWorkFlow>();
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(HANDLE_PROCESS_DEFINE_KEY)
                .taskCandidateUser(String.valueOf(userId))
                .orderByTaskCreateTime().desc()
                .list();
        if(taskList!=null && taskList.size() > 0){
            for(Task task :taskList ){
                HandleWorkFlow handleWorkFlow = new HandleWorkFlow();
                //使用任务对象Task获取流程实例ID
                String processInstanceId = task.getProcessInstanceId();
                //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
                ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(processInstanceId)
                        .singleResult();
                //使用流程实例对象获取BUSINESS_KEY
                String business_key = pi.getBusinessKey();
                String id = "";
                if(StringUtils.isNotBlank(business_key)){
                    //截取字符串，取buniness_key小数点的第2个值
                    id = business_key.split("\\.")[1];
                    handleWorkFlow.setHandleId(Long.parseLong(id));
                    handleWorkFlow.setTaskId(task.getId());
                    handleList.add(handleWorkFlow);
                }
            }
        }
        return handleList;
    }

    /**
     * 申请初审
     * @param handleWorkFlow
     * @return
     */
    public ProcessInstance saveTwoSubmitTask(HandleWorkFlow handleWorkFlow) {
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(handleWorkFlow.getUserId()));

        //在完成任务之前，设置流程变量，按照连线的名称，去完成任务
        Map<String, Object> variables = new HashMap();
        variables.put("auditState", handleWorkFlow.getAuditState());
        variables.put("initApplyUser", handleWorkFlow.getCurrentUser());

        //财政或人民政府审批
        if(handleWorkFlow.getAuditState().equals(1)){
            variables.put("gtUserIds", handleWorkFlow.getGtUserIds());
        }

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(handleWorkFlow.getTaskId(), variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(handleWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }

    /**
     * 财政或人民政府审批
     * @param handleWorkFlow
     * @return
     */
    public ProcessInstance saveThreeSubmitTask(HandleWorkFlow handleWorkFlow) {
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(handleWorkFlow.getUserId()));

        //在完成任务之前，设置流程变量，按照连线的名称，去完成任务
        Map<String, Object> variables = new HashMap();
        variables.put("auditState", handleWorkFlow.getAuditState());
        variables.put("financeApprovalUser", handleWorkFlow.getCurrentUser());

        if(handleWorkFlow.getAuditState().equals(1)){
            variables.put("handleWay", handleWorkFlow.getHandleWay());
            //转换用途
            if(handleWorkFlow.getHandleWay().equals(1)){
                variables.put("conversionUserIds", handleWorkFlow.getConversionUserIds());
            }
            //拍卖
            if(handleWorkFlow.getHandleWay().equals(3)){
                variables.put("auctionUserIds", handleWorkFlow.getAuctionUserIds());
            }
            //拆除
            if(handleWorkFlow.getHandleWay().equals(4)){
                variables.put("dismantleUserIds", handleWorkFlow.getDismantleUserIds());
            }
            //征收
            if(handleWorkFlow.getHandleWay().equals(7)){
                variables.put("collectionUserIds", handleWorkFlow.getCollectionUserIds());
            }
        }

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(handleWorkFlow.getTaskId(), variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(handleWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }

    /**
     * 转换用途
     * @param handleWorkFlow
     * @return
     */
    public ProcessInstance saveFourSubmitTask(HandleWorkFlow handleWorkFlow) {
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(handleWorkFlow.getUserId()));

        Map<String, Object> variables = new HashMap();
        variables.put("conPurposesUser", handleWorkFlow.getCurrentUser());

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(handleWorkFlow.getTaskId(), variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(handleWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }

    /**
     * 拍卖
     * @param handleWorkFlow
     * @return
     */
    public ProcessInstance saveFiveSubmitTask(HandleWorkFlow handleWorkFlow) {
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(handleWorkFlow.getUserId()));

        Map<String, Object> variables = new HashMap();
        variables.put("auctionUser", handleWorkFlow.getCurrentUser());
        taskService.complete(handleWorkFlow.getTaskId(), variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(handleWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }

    /**
     * 拆除
     * @param handleWorkFlow
     * @return
     */
    public ProcessInstance saveSixSubmitTask(HandleWorkFlow handleWorkFlow) {
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(handleWorkFlow.getUserId()));

        Map<String, Object> variables = new HashMap();
        variables.put("dismantleUser", handleWorkFlow.getCurrentUser());
        taskService.complete(handleWorkFlow.getTaskId(), variables);

        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(handleWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }

    /**
     * 征收
     * @param handleWorkFlow
     * @return
     */
    public ProcessInstance saveSevenSubmitTask(HandleWorkFlow handleWorkFlow) {
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(handleWorkFlow.getUserId()));

        Map<String, Object> variables = new HashMap();
        variables.put("levyUser", handleWorkFlow.getCurrentUser());
        taskService.complete(handleWorkFlow.getTaskId(), variables);

        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(handleWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }
}
