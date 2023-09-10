package com.ryzw.housemanager.service;

import com.ryzw.housemanager.dto.ProcessHistoryDto;
import com.ryzw.housemanager.dto.ProcessNumDto;

import java.util.List;

/**
 * <p>
 *     公共流程 服务类
 * </p>
 *
 * @author wj
 * @since 2019-05-29 0029
 */
public interface IProcessService {

    /**
     * 查询审批流程
     *
     * @param processInstanceId 流程实例ID
     * @return
     */
    List<ProcessHistoryDto> selectProcessHistory( String processInstanceId);


    /**
     * 统计待办事项数量
     *
     * @param userId 用户ID
     * @return
     */
    Long todoListNum(Long userId);

    /**
     *  统计每个待办事项数量
     * @param userId
     * @return
     */
    ProcessNumDto todoListProcessNum(Long userId);
}
