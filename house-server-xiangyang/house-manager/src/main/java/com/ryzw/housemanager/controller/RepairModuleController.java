package com.ryzw.housemanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.RepairQualityDto;
import com.ryzw.housemanager.entity.RepairModule;
import com.ryzw.housemanager.service.IRepairModuleService;
import com.ryzw.housemanager.vo.RepairQualityVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 维修模块信息管理
 * </p>
 *
 * @author mfl
 * @since 2019/4/11
 */
@RestController
@RequestMapping("/repairModule")
@Api(value = "RepairModuleController", description = "维修模块信息管理")
@Slf4j
public class RepairModuleController {
    @Autowired
    private IRepairModuleService repairModuleService;

    /**
     * 维修模块信息管理列表
     *
     * @param repairQualityVo
     * @return
     */
    @RequestMapping(value = "/pageList", method = RequestMethod.POST)
    @ApiOperation(value = "维修模块信息管理列表", notes = "维修模块信息管理列表")
    public Page<RepairQualityDto> repairQuality(@RequestBody RepairQualityVo repairQualityVo) {
        return repairModuleService.repairQuality(repairQualityVo);
    }

    /**
     * 维修模块位置列表
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "维修模块位置列表", notes = "维修模块位置列表")
    public List<RepairModule> list() {

        return repairModuleService.selectList();
    }

}
