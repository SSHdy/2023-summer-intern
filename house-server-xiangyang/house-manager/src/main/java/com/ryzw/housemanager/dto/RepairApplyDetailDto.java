package com.ryzw.housemanager.dto;

import lombok.Data;
import org.activiti.engine.task.Comment;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 返回给前段的维修详情
 * </p>
 *
 * @author zf
 * @since 2019/4/2
 */
@Data
public class RepairApplyDetailDto {

    /**
     * 维修申请编号
     */
    private Long repairApplyId;

    /**
     * 维修申请时间
     */
    private Date repairApplyDate;

    /**
     * 维修申请状态
     */
    private Integer applyStatus;

    /**
     * 维修申请人
     */
    private String repairApplicant;

    /**
     * 当前执行的任务id
     */
    private String taskId;

    /**
     * 当前流程步骤编号
     */
    private Integer step;

    /**
     * 使用单位
     */
    private List<String> unitNameList;

    /**
     * 维修模块集合
     */
    private List<RepairModuleDetailDto> RepairModuleDetailList;

    /**
     * 历史批注信息
     */
    private List<Comment> commentList;


}
