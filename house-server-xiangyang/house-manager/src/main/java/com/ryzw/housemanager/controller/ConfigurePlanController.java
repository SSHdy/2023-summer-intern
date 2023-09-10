package com.ryzw.housemanager.controller;

import com.ryzw.housemanager.entity.Plan;
import com.ryzw.housemanager.service.IPlanService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.ConfigurePlanDataVo;
import com.ryzw.housemanager.vo.ConfigurePlanVo;
import com.ryzw.housemanager.vo.JsonPlanVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     配置方案表 前端控制器
 * </p>
 *
 * @author wj
 * @since 2019-06-17 0017
 */
@RestController
@RequestMapping("/configurePlan")
@Api(value = "配置方案管理", description = "配置方案管理")
public class ConfigurePlanController {

    @Autowired
    IPlanService planService;

    /**
     * 配置方案列表
     *
     * @param configurePlanVo
     * @return
     */
    @RequestMapping(value = "configurePlanList", method = RequestMethod.POST)
    @ApiOperation(value = "配置方案列表", notes = "配置方案列表")
    public List<Plan> configurePlanList(@RequestBody @Valid ConfigurePlanVo configurePlanVo) {
        return planService.configurePlanList(configurePlanVo);
    }

    /**
     * 新增配置方案
     * @param plan
     * @return
     */
    @RequestMapping(value = "addConfigurePlan", method = RequestMethod.POST)
    @ApiOperation(value = "新增配置方案", notes = "新增配置方案")
    public boolean addConfigurePlan(@RequestBody @Valid Plan plan){
        return planService.addConfigurePlan(plan);
    }

    /**
     * 修改配置方案
     * @param plan
     * @return
     */
    @RequestMapping(value = "updateConfigurePlan", method = RequestMethod.POST)
    @ApiOperation(value = "修改配置方案", notes = "修改配置方案")
    public boolean updateConfigurePlan(@RequestBody @Valid Plan plan){
        return planService.updateConfigurePlan(plan);
    }

    /**
     * 删除配置方案
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "deleteConfigurePlan", method = RequestMethod.POST)
    @ApiOperation(value = "删除配置方案", notes = "修改配置方案")
    public boolean deleteConfigurePlan(@RequestBody @Valid BasicVo<Long> basicVo){
        return planService.deleteConfigurePlan(basicVo);
    }

    /**
     * 查询配置方案相关联的配置方案数据
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "configurePlanDataCount", method = RequestMethod.POST)
    @ApiOperation(value = "查询配置方案相关联的配置方案数据", notes = "查询配置方案相关联的配置方案数据")
    public Integer configurePlanDataCount(@RequestBody @Valid BasicVo<Long> basicVo){
        return planService.configurePlanDataCount(basicVo);
    }


    /**
     * 新增或修改方案配数据
     * @param configurePlanDataVo
     * @return
     */
    @RequestMapping(value = "aueConfigurePlanData", method = RequestMethod.POST)
    @ApiOperation(value = "新增或修改方案配数据", notes = "新增或修改方案配数据")
    public boolean aueConfigurePlanData(@RequestBody @Valid ConfigurePlanDataVo configurePlanDataVo){
        return planService.aueConfigurePlanData(configurePlanDataVo);
    }


    /**
     * 根据buildId，floorName查询mongodb的对应的json
     * @param jsonPlanVo
     * @return
     */
    @ApiOperation(value = "json查询", notes = "json查询")
    @RequestMapping(value = "/jsonPlan", method = RequestMethod.POST)
    public Map<String,Object> getJsonPlan(@RequestBody JsonPlanVo jsonPlanVo){
        return planService.selectJsonPlan(jsonPlanVo);
    }



}
