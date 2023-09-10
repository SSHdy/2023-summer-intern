package com.ryzw.housemanager.dto;

import lombok.Data;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     输出给前端的查看流程审批历史记录
 * </p>
 *
 * @author wj
 * @since 2019/4/15 0015
 */
@Data
public class ProcessHistoryDto {

    /**
     * 流程步骤名称
     */
    private String stepName;

    /**
     * 办理时间
     */
    private Date processingTime;

    /**
     * 办理人
     */
    private String transactors;

}
