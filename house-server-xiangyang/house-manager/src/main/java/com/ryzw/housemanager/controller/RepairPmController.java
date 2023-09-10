package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryzw.housemanager.entity.PmChangeLog;
import com.ryzw.housemanager.entity.RepairPm;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IRepairPmService;
import com.ryzw.housemanager.vo.BasicVo;
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
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 维修项目管理人表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
@Slf4j
@Api(value = "维修项目人员管理", description = "维修项目人员管理")
@RestController
@RequestMapping("/repairPm")
public class RepairPmController {
    @Autowired
    private IRepairPmService repairPmService;
    @Autowired
    IAuthorityUnitService authorityUnitService;


    /**
     * 查询项目转移人员列表
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/userList", method = RequestMethod.POST)
    @ApiOperation(value = "查询项目转移人员列表", notes = "查询项目转移人员列表")
    public List<Map<String, Object>> userList(@RequestBody BasicVo<Long> basicVo) {
        QueryWrapper<RepairPm> wrapper = new QueryWrapper<>();
        wrapper.select("repair_pm_userid");
        wrapper.eq("repair_apply_id", basicVo.getId());
        List<Long> userIds = repairPmService.list(wrapper).stream().map(RepairPm::getRepairPmUserid)
                .collect(Collectors.toList());
        return authorityUnitService.userList(userIds);
    }

    /**
     * 维修项目人员转移
     *
     * @param pmChangeLog
     * @return
     */
    @RequestMapping(value = "/changeManage", method = RequestMethod.POST)
    @ApiOperation(value = "维修项目人员转移", notes = "维修项目人员转移")
    public boolean changeManage(@RequestBody PmChangeLog pmChangeLog) {
        return repairPmService.changeManage(pmChangeLog);
    }

    /**
     * 查询项目负责人
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/resUser", method = RequestMethod.POST)
    @ApiOperation(value = "查询项目负责人", notes = "查询项目负责人")
    public List<RepairPm> resUser(@RequestBody @Valid BasicVo<Long> basicVo) {
        QueryWrapper<RepairPm> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("repair_pm_userid", "repair_pm_name");
        queryWrapper.eq("repair_apply_id", basicVo.getId());
        return repairPmService.list(queryWrapper);
    }

}
