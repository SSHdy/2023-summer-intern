package com.ryzw.housegateway.service;

import com.ryzw.housegateway.entity.OperationLog;

/**
 * <p>
 * 操作日志服务类
 * </p>
 *
 * @author yz
 * @since 2019/5/15
 */
public interface OperationLogService {

    /**
     * 保存系统日志
     * @param operationLog
     * @return
     */
    OperationLog save(OperationLog operationLog);
}
