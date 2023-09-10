package com.ryzw.housemanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryzw.housemanager.dto.StatueDto;
import com.ryzw.housemanager.dto.StepNodeDto;
import com.ryzw.housemanager.entity.*;
import com.ryzw.housemanager.service.impl.WorkstatusServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 枚举类型维护查询
 * </p>
 *
 * @author mfl
 * @since 2018/12/18
 */
@RestController
@RequestMapping("/statue")
@Api(value = "workStatusController", description = "枚举类型管理")
public class TypeController {

    @Autowired
    WorkstatusServiceImpl workstatusService;

    /**
     * 在职情况枚举列表
     *
     * @return
     */
    @RequestMapping(value = "/statue/list", method = RequestMethod.POST)
    @ApiOperation(value = "在职情况查询", notes = "在职情况查询")
    public List<Workstatus> getStatue() {
        QueryWrapper<Workstatus> queryWrapper = new QueryWrapper<>();
        return workstatusService.list(queryWrapper);
    }

    /**
     * 維修流程结点查询
     *
     * @return
     */
    @RequestMapping(value = "/repair/list", method = RequestMethod.POST)
    @ApiOperation(value = "維修流程结点查询", notes = "維修流程结点查询")
    public List<StepNodeDto> getRepair() {
        return Arrays.stream(StepNode.values()).map(p -> new StepNodeDto(p.getValue(), p.getName())).collect(Collectors.toList());
    }

    /**
     * 处置管理结点查询
     *
     * @return
     */
    @RequestMapping(value = "/handle/list", method = RequestMethod.POST)
    @ApiOperation(value = "处置管理结点查询", notes = "处置管理结点查询")
    public List<StepNodeDto> getHandle() {
        return Arrays.stream(HandleStepNode.values()).map(p -> new StepNodeDto(p.getValue(), p.getName())).collect(Collectors.toList());
    }

    /**
     * 配置管理结点查询
     *
     * @return
     */
    @RequestMapping(value = "/config/list", method = RequestMethod.POST)
    @ApiOperation(value = "配置管理结点查询", notes = "配置管理结点查询")
    public List<StepNodeDto> getConfig() {
        return Arrays.stream(ConfigStepNode.values()).map(p -> new StepNodeDto(p.getValue(), p.getName())).collect(Collectors.toList());
    }

}
