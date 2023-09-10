package com.ryzw.housegateway.service.impl;

import com.ryzw.housegateway.entity.OperationLog;
import com.ryzw.housegateway.service.OperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志服务类
 * </p>
 *
 * @author yz
 * @since 2019/5/15
 */
@Service
@Slf4j
public class OperationLogServiceImpl implements OperationLogService {


    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 保存系统日志
     *
     * @param operationLog
     * @return
     */
    @Override
    public OperationLog save(OperationLog operationLog) {
        try {
           return mongoTemplate.save(operationLog);
        }
        catch (Exception ex){
              log.error("gateway error",ex);
        }
        return null;
    }
}
