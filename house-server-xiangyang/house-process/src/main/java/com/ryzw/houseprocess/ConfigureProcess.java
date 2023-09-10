package com.ryzw.houseprocess;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.Execution;
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
 *     配置工作流 服务类
 * </p>
 *
 * @author wj
 * @since 2019-06-04 0004
 */
@Service
public class ConfigureProcess extends CommonProcess {

    private static final String CONFIGURE_PROCESS_DEFINE_KEY = "configureProcess";


    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;


    /**
     * 启动流程实例
     * @param configureId  配置申请ID
     * @param userId  当前登录人ID
     */
    public String startConfigureProcess(Long configureId,String configureApplicant, Long userId,List<String> userIds) {
        //使用流程变量设置下一个任务的办理人
        Map<String, Object> variable = new HashMap();
        variable.put("userId",userId);
        variable.put("configureApplicant",configureApplicant);

        //让启动的流程（流程实例）关联业务
        String objId = CONFIGURE_PROCESS_DEFINE_KEY+"."+configureId;
        variable.put("objId", objId);
        //启动流程实例
        ProcessInstance vacationInstance =  runtimeService.startProcessInstanceByKey(CONFIGURE_PROCESS_DEFINE_KEY,objId,variable);
        Task task = taskService.createTaskQuery().processInstanceId(vacationInstance.getId()).singleResult();
        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        Map<String, Object> variables = new HashMap<String,Object>();
        variables.put("gtUserIds",userIds);
        variables.put("configureApplicant",configureApplicant);
        taskService.complete(task.getId(),variables);
        return vacationInstance.getId();
    }


    /**
     * 使用当前用户ID正在执行的任务表，获取当前任务的处置ids
     * @param userId
     * @return
     */
    public List<ConfigureWorkFlow> findTaskListById(Long userId) {
        List<ConfigureWorkFlow> configureList = new ArrayList<ConfigureWorkFlow>();
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(CONFIGURE_PROCESS_DEFINE_KEY)
                .taskCandidateUser(String.valueOf(userId))
                .orderByTaskCreateTime().desc()
                .list();
        if(taskList!=null && taskList.size() > 0){
            for(Task task :taskList ){
                ConfigureWorkFlow configureWorkFlow = new ConfigureWorkFlow();
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
                    configureWorkFlow.setConfigureId(Long.parseLong(id));
                    configureWorkFlow.setTaskId(task.getId());
                    configureList.add(configureWorkFlow);
                }
            }
        }
        return configureList;
    }

    /**
     *机关管理局用房情况审查
     * @param configureWorkFlow
     * @return
     */
    public ProcessInstance saveTwoSubmitTask(ConfigureWorkFlow configureWorkFlow) {
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(configureWorkFlow.getUserId()));

        //在完成任务之前，设置流程变量，按照连线的名称，去完成任务
        Map<String, Object> variables = new HashMap();
        variables.put("auditState", configureWorkFlow.getAuditState());
        variables.put("unitReviewUser", configureWorkFlow.getCurrentUser());

        //拟定分配方案
        if(configureWorkFlow.getAuditState().equals(1)){
            variables.put("duPlanUserIds", configureWorkFlow.getDuPlanUserIds());
        }

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(configureWorkFlow.getTaskId(), variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(configureWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }

    /**
     * 拟定分配方案
     * @param configureWorkFlow
     * @return
     */
    public ProcessInstance saveThreeSubmitTask(ConfigureWorkFlow configureWorkFlow) {
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(configureWorkFlow.getUserId()));
        Map<String, Object> variables = new HashMap();

        variables.put("drawPlanUser", configureWorkFlow.getCurrentUser());

        //领导审批
        variables.put("leaderUserIds", configureWorkFlow.getLeaderUserIds());

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(configureWorkFlow.getTaskId(),variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(configureWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }

    /**
     * 领导审批
     * @param configureWorkFlow
     * @return
     */
    public ProcessInstance saveFourSubmitTasks(ConfigureWorkFlow configureWorkFlow) {
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(configureWorkFlow.getUserId()));
        Map<String, Object> variables = new HashMap();

        variables.put("auditState", configureWorkFlow.getAuditState());
        if(configureWorkFlow.getAllocationPlan()!=null){
            variables.put("allocationPlan", configureWorkFlow.getAllocationPlan());
        }

        variables.put("leaderApprovalUser", configureWorkFlow.getCurrentUser());
        if(configureWorkFlow.getAuditState().equals(1) && configureWorkFlow.getAllocationPlan().equals(5)){
            //完善信息
            variables.put("inGdataUserIds", configureWorkFlow.getInGdataUserIds());
        }

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(configureWorkFlow.getTaskId(),variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(configureWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }

    /**
     * 内控 领导审批
     * @param configureWorkFlow
     * @return
     */
    public ProcessInstance saveFourSubmitTask(ConfigureWorkFlow configureWorkFlow) {
        //获取流程实例ID
        String processInstanceId = configureWorkFlow.getProcessInstanceId();

        //查询执行对象ID
        Execution execution = runtimeService.createExecutionQuery()
                //当前活动的id，对应receiveTask.bpmn文件中的活动节点id的属性值
                .processInstanceId(processInstanceId).activityId("receiveTask").singleResult();

        //向后执行一步，如果流程处于等待状态，使得流程继续执行
        Map<String, Object> receiveVariable = new HashMap();
        receiveVariable.put("auditState", configureWorkFlow.getAuditState());
        receiveVariable.put("leaderApprovalUser", configureWorkFlow.getCurrentUser());

        runtimeService.trigger(execution.getId(),receiveVariable);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        return pi;
    }


    /**
     * 完善信息
     * @param configureWorkFlow
     * @return
     */
    public ProcessInstance saveFiveSubmitTask(ConfigureWorkFlow configureWorkFlow) {
        //添加当前任务的审核人
        Authentication.setAuthenticatedUserId(String.valueOf(configureWorkFlow.getUserId()));
        Map<String, Object> variables = new HashMap();

        variables.put("improvingDataUser", configureWorkFlow.getCurrentUser());

        //使用任务ID，完成当前人的个人任务，同时设置流程变量
        taskService.complete(configureWorkFlow.getTaskId(),variables);

        //在完成任务之后，判断流程是否结束
        ProcessInstance pi = runtimeService.createProcessInstanceQuery()
                .processInstanceId(configureWorkFlow.getProcessInstanceId()).singleResult();
        return pi;
    }
}
