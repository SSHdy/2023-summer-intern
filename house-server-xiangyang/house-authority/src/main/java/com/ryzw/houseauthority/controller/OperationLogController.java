package com.ryzw.houseauthority.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.houseauthority.entity.OperationLog;
import com.ryzw.houseauthority.service.IOperationLogService;
import com.ryzw.houseauthority.vo.OperationLogVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 操作日志控制器
 * </p>
 *
 * @author yz
 * @since 2019/5/15
 */
@RestController
@RequestMapping("/operationLog")
@Api(value = "日志信息查询", description = "日志信息查询")
public class OperationLogController {

    @Autowired
    IOperationLogService operationLogService;

    /**
     * 按条件分页查询操作日志
     *
     * @param operationLogVo
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "日志信息查询", notes = "日志信息查询")
    public Page<OperationLog> pageQuery(@RequestBody @Valid OperationLogVo operationLogVo) {
        return operationLogService.pageQuery(operationLogVo);
    }
}
