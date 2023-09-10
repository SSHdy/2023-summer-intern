package com.ryzw.houseauthority.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.houseauthority.entity.OperationLog;
import com.ryzw.houseauthority.vo.OperationLogVo;

/**
 * <p>
 * 操作日志服务类
 * </p>
 *
 * @author yz
 * @since 2019/5/15
 */
public interface IOperationLogService {

    /**
     * 分页查询系统日志
     * @param operationLogVo
     * @return
     */
    Page<OperationLog> pageQuery(OperationLogVo operationLogVo);
}
