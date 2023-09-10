package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.RepairProjectListDto;
import com.ryzw.housemanager.entity.RepairApply;
import com.ryzw.housemanager.service.IRepairApplyService;
import com.ryzw.housemanager.service.IRepairProjectService;
import com.ryzw.housemanager.vo.RepairProjectListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 项目管理表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */

@Slf4j
@Api(value = "RepairProjectController", description = "维修项目管理")
@RestController
@RequestMapping("/repairProject")
public class RepairProjectController {
    @Autowired
    private IRepairProjectService repairProjectService;
    @Autowired
    private IRepairApplyService repairApplyService;

    /**
     * 分页查询维修项目列表
     *
     * @param repairProjectListVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询维修项目列表", notes = "分页查询维修项目列表")
    public Page<RepairProjectListDto> list(@RequestBody @Valid RepairProjectListVo repairProjectListVo) {
        return repairProjectService.repairProject(repairProjectListVo);
    }

    /**
     * 工程名称列表
     *
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ApiOperation(value = "工程名称列表", notes = "工程名称列表")
    public List<RepairApply> getList() {
        QueryWrapper<RepairApply> wrapper = new QueryWrapper<>();
        wrapper.select("repair_apply_id", "repair_project");
        wrapper.orderByDesc("repair_apply_id");
        return repairApplyService.list(wrapper);
    }


}
