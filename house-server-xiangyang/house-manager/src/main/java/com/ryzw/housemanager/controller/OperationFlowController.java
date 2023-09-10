package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.entity.OperationFlow;
import com.ryzw.housemanager.service.IOperationFlowService;
import com.ryzw.housemanager.vo.OperationFlowVo;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 操作流水表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-01-11
 */
@RestController
@RequestMapping("/operationFlow")
public class OperationFlowController {
    @Autowired
    private IOperationFlowService operationFlowService;

    /**
     * 分页查询流水信息列表
     * @param operationFlowVo
     * @return
     */
    @RequestMapping(value = "list",method = RequestMethod.POST)
    @ApiOperation(value = "分页查询流水信息列表",notes = "分页查询流水信息列表")
    public IPage<OperationFlow> getList(@RequestBody @Valid OperationFlowVo operationFlowVo) {
        QueryWrapper<OperationFlow> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(operationFlowVo.getPurchasingId())) {
            wrapper.like("purchasing_id", operationFlowVo.getPurchasingId());
        }
        if (StringUtils.isNotBlank(operationFlowVo.getPurchasingPname())) {
            wrapper.like("purchasing_pname", operationFlowVo.getPurchasingPname());
        }
        if (StringUtils.isNotBlank(operationFlowVo.getSerialNumber())) {
            wrapper.like("serial_number", operationFlowVo.getSerialNumber());
        }
        wrapper.orderByDesc("operation_flow_id");
        IPage<OperationFlow> page = new Page<>();
        page.setCurrent(operationFlowVo.getCurrentPage());
        page.setSize(operationFlowVo.getPageSize());
        return operationFlowService.page(page,wrapper);
    }


}
