package com.ryzw.houseprocess;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author wj
 * @since 2019/3/7 0007
 */
@Data
public class RepairWorkFlow{

    /**
     * 维修审批id
     */
    private Long repairId;

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
     *内控系统审批意见
     */
    private Integer approvalState;

    /**
     * 备注
     */
    private String comment;

    /**
     * 所有拥有线下党组会审批权限的用户ID
     */
    private List<String> pwUserIds;


    /**
     * 所有拥有自费维修上传维修方案权限的用户ID
     */
    private List<String> repairPlanUserIds;

    /**
     * 拥有审批维修方案权限的用户ID
     */
    private List<String> approvalRepairUserIds;


    /**
     * 拥有自费维修上传预算方案权限的用户ID
     */
    private List<String> budgetPlanUserIds;


    /**
     * 拥有审批预算方案权限的用户ID
     */
    private List<String> approvalBudgetUserIds;


    /**
     * 所有拥有项目方案设计可行性研究权限的用户ID
     */
    private List<String> researchUserIds;

    /**
     * 所有拥有领导审批权限的用户ID
     */
    private List<String> leaderApprovalUserIds;


    /**
     * 报政府或发改委的用户ID
     */
    private List<String> gtUserIds;


    /**
     * 项目施工设计的用户ID
     */
    private List<String> pcdUserIds;


    /**
     * 项目预算的用户ID
     */
    private List<String> pdUserIds;


    /**
     * 项目评审的用户ID
     */
    private List<String> prUserIds;


    /**
     * 项目负责人的用户ID
     */
    private List<String> leaderUserIds;



}
