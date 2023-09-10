package com.ryzw.houseprocess;

import org.activiti.engine.HistoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *     公共工作流 服务类
 * </p>
 *
 * @author wj
 * @since 2019-05-29 0029
 */
@Service
public class CommonProcess {

    private static final String REPAIR_PROCESS_DEFINE_KEY = "repairProcess";

    private static final String HANDLE_PROCESS_DEFINE_KEY = "handleProcess";

    private static final String CONFIGURE_PROCESS_DEFINE_KEY = "configureProcess";

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 统计待办事项数量
     * @param userId
     * @return
     */
    public Long todoListNum(Long userId){
        Long taskNum = taskService.createTaskQuery()
                .taskCandidateUser(String.valueOf(userId))
                .count();
        return taskNum;
    }

    /**
     * 统计维修待办事项数量
     * @param userId
     * @return
     */
    public Long todoListRepairNum(Long userId){
        Long taskNum = taskService.createTaskQuery()
                .processDefinitionKey(REPAIR_PROCESS_DEFINE_KEY)
                .taskCandidateUser(String.valueOf(userId))
                .count();
        return taskNum;
    }

    /**
     * 统计处置待办事项数量
     * @param userId
     * @return
     */
    public Long todoListHandleNum(Long userId){
        Long taskNum = taskService.createTaskQuery()
                .processDefinitionKey(HANDLE_PROCESS_DEFINE_KEY)
                .taskCandidateUser(String.valueOf(userId))
                .count();
        return taskNum;
    }

    /**
     * 统计配置待办事项数量
     * @param userId
     * @return
     */
    public Long todoListConfigureNum(Long userId){
        Long taskNum = taskService.createTaskQuery()
                .processDefinitionKey(CONFIGURE_PROCESS_DEFINE_KEY)
                .taskCandidateUser(String.valueOf(userId))
                .count();
        return taskNum;
    }

    /**
     * 向组任务中添加成员
     * @param processInstanceId
     * @param userId
     */
    public void addGroupUser(String processInstanceId,String userId){
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        if(task!=null){
            taskService.addCandidateUser(task.getId(), userId);
        }
    }

    /**
     * 从组任务中删除成员
     * @param processInstanceId
     * @param userId
     */
    public void deleteGroupUser(String processInstanceId,String userId){
        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
        if(task!=null){
            taskService.deleteCandidateUser(task.getId(), userId);
        }
    }


    /**查询历史活动*/
    public List<HistoricActivityInstance> findHistoryActivity(String processInstanceId){
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().asc().list();
        return historicActivityInstanceList;
    }


    /**查询历史变量详细信息*/
    public List<HistoricDetail> findHistoricDetail(String activityInstanceId){
        List<HistoricDetail> historicDetailList = historyService.createHistoricDetailQuery()
                .activityInstanceId(activityInstanceId).orderByTime().asc().list();
        return historicDetailList;
    }

}
