package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.vo.PageVo;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 *     输出给前端的维修审批信息
 * </p>
 *
 * @author wj
 * @since 2019/3/7 0007
 */
@Data
public class RepairApprovalDto extends PageVo {

    /**
     * 维修编号
     */
    private Long repairApplyId;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 申请编号
     */
    private String repairApplyNo;

    /**
     * 项目名称
     */
    private String repairProject;


    /**
     * 申请人
     */
    private String repairApplicant;

    /**
     * 申请时间
     */
    private Date repairApplyDate;

    /**
     * 任务id
     */
    private  String taskId;

    /**
     * 当前流程号
     */
    private Integer step;

    /**
     * 申请面积
     */
    private Double repairModuleArea;

}
