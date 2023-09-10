package com.ryzw.houseprocess;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *     配置工作流实体类
 * </p>
 *
 * @author wj
 * @since 2019-06-04 0004
 */
@Data
public class ConfigureWorkFlow {

    /**
     * 配置申请编号
     */
    private Long configureId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 当前用户名
     */
    private String currentUser;


    /**
     * 当前执行的任务id
     */
    private String taskId;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 连线名称
     */
    private Integer auditState;

    /**
     * 分配方案
     */
    private Integer allocationPlan;


    /**
     * 所有拥有机关管理局用房情况审查权限的用户ID
     */
    private List<String> gtUserIds;

    /**
     * 所有拥有拟定分配方案权限的用户ID
     */
    private List<String> duPlanUserIds;

    /**
     * 所有拥有领导审批权限的用户ID
     */
    private List<String> leaderUserIds;

    /**
     * 所有拥有完善信息权限的用户ID
     */
    private List<String> inGdataUserIds;


}
