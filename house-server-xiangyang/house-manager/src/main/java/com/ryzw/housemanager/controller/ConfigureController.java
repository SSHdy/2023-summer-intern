package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.Configure;
import com.ryzw.housemanager.entity.ConfigureEnclosure;
import com.ryzw.housemanager.entity.ConfigurePlanChangeData;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IConfigureEnclosureService;
import com.ryzw.housemanager.service.IConfigurePlanChangeDataService;
import com.ryzw.housemanager.service.IConfigureRoomService;
import com.ryzw.housemanager.service.IConfigureService;
import com.ryzw.housemanager.vo.*;
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

/**
 * <p>
 * 配置表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
@RestController
@RequestMapping("/configure")
@Api(value = "配置申请管理", description = "配置申请管理")
@Slf4j
public class ConfigureController {

    @Autowired
    private IConfigureService configureService;
    @Autowired
    private IConfigureRoomService configureRoomService;
    @Autowired
    private IConfigureEnclosureService configureEnclosureService;
    @Autowired
    private IConfigurePlanChangeDataService configurePlanChangeDataService;

    /**
     * 新增配置申请
     *
     * @param configureAddVo
     * @return
     */
    @RequestMapping(value = "addConfigure", method = RequestMethod.POST)
    @ApiOperation(value = "新增配置申请", notes = "新增配置申请")
    public boolean addConfigure(@RequestBody @Valid ConfigureAddVo configureAddVo) {

        return configureService.addConfigure(configureAddVo);
    }

    /**
     * 查询配置申请基础数据
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "basicConfigure", method = RequestMethod.POST)
    @ApiOperation(value = "查询配置申请基础数据", notes = "查询配置申请基础数据")
    public ConfigureDto basicConfigure(@RequestBody BasicVo<Long> basicVo) {

        return configureService.basicConfigure(basicVo.getId());
    }

    /**
     * 修改配置申请
     *
     * @param configureAddVo
     * @return
     */
    @RequestMapping(value = "updateConfigure", method = RequestMethod.POST)
    @ApiOperation(value = "修改配置申请", notes = "修改配置申请")
    public boolean updateConfigure(@RequestBody @Valid ConfigureAddVo configureAddVo) {

        return configureService.updateConfigure(configureAddVo);
    }

    /**
     * 配置申请详情
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "configureDetail", method = RequestMethod.POST)
    @ApiOperation(value = "配置申请详情", notes = "配置申请详情")
    public ConfigureDetailDto configureDetail(@RequestBody @Valid BasicVo<Long> basicVo) {
        try {
            ConfigureDetailDto configureDetailDto = new ConfigureDetailDto();
            QueryWrapper<Configure> wrapper = new QueryWrapper<>();
            wrapper.eq("configure_id", basicVo.getId());
            wrapper.select("apply_reason", "contact_phone", "contacts", "unit_property", "unit_address", "unit_id");
            Configure configure = configureService.getOne(wrapper);
            //配置详情
            configureDetailDto.setConfigure(configure);
            List<ConfigureEnclosure> enclosureList = configureEnclosureService.list(new QueryWrapper<ConfigureEnclosure>().eq("configure_id", basicVo.getId()));
            //配置附件详情
            configureDetailDto.setConfigureEnclosureList(enclosureList);
            return configureDetailDto;
        } catch (Exception ex) {
            log.error("configureDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 配置申请列表
     *
     * @param configureVo
     * @return
     */
    @RequestMapping(value = "configureList", method = RequestMethod.POST)
    @ApiOperation(value = "配置申请列表", notes = "配置申请列表")
    public Page<ConfigureDto> configureList(@RequestBody ConfigureVo configureVo) {

        return configureService.configureList(configureVo);
    }

    /**
     * 更新用户配置权限
     *
     * @return
     */
    @RequestMapping(value = "/updateConfigureAuthority", method = RequestMethod.POST)
    @ApiOperation(value = "更新用户配置权限", notes = "更新用户配置权限")
    public boolean updateConfigureAuthority(@RequestBody @Valid ActivityAuthorityVo activityAuthorityVo) {
        return configureService.updateConfigureAuthority(activityAuthorityVo);
    }

    /**
     * 配置审批列表
     *
     * @param configureVo
     * @return
     */
    @RequestMapping(value = "configureApprovalList", method = RequestMethod.POST)
    @ApiOperation(value = "配置审批列表", notes = "配置审批列表")
    public Page<ConfigureDto> configureApprovalList(@RequestBody @Valid ConfigureVo configureVo) {
        return configureService.configureApprovalList(configureVo);
    }

    /**
     * 审核申请审批
     *
     * @param configureFlowVo
     * @return
     */
    @ApiOperation(value = "审核申请审批", notes = "审核申请审批")
    @RequestMapping(value = "/saveSubmitConfigure", method = RequestMethod.POST)
    public boolean saveSubmitConfigure(@RequestBody @Valid ConfigureFlowVo configureFlowVo) {
        return configureService.saveSubmitConfigure(configureFlowVo);
    }

    /**
     * 内控 领导审批
     *
     * @param processHttpResultVo
     * @return
     */
    @RequestMapping(value = "/leaderApproval", method = RequestMethod.POST)
    @ApiOperation(value = "内控 领导审批", notes = "内控 领导审批")
    public boolean leaderApproval(@RequestBody @Valid ProcessHttpResultVo processHttpResultVo) {
        return configureService.leaderApproval(processHttpResultVo);
    }

    /**
     * 查询自动分房的筛选条件列表
     *
     * @param basicVo
     * @return
     */
    @ApiOperation(value = "查询自动分房的筛选条件列表", notes = "查询自动分房的筛选条件列表")
    @RequestMapping(value = "/autoCondition", method = RequestMethod.POST)
    public List<AutoConditionDto> autoCondition(@RequestBody @Valid BasicVo<Long> basicVo) {
        return configureRoomService.autoCondition(basicVo);
    }

    /**
     * 查询自动分房符合条件的房间
     *
     * @param autoAllocationRoomVo
     * @return
     */
    @ApiOperation(value = "查询自动分房符合条件的房间", notes = "查询自动分房符合条件的房间")
    @RequestMapping(value = "/autoAllocationRoom", method = RequestMethod.POST)
    public Page<AutoAllocationRoomDto> autoAllocationRoom(@RequestBody @Valid AutoAllocationRoomVo autoAllocationRoomVo) {
        return configureRoomService.autoAllocationRoom(autoAllocationRoomVo);
    }

    /**
     * 查询人工分房，符合条件的房间
     *
     * @param artificialAllocationVo
     * @return
     */
    @ApiOperation(value = "查询人工分房，符合条件的房间", notes = "查询人工分房，符合条件的房间")
    @RequestMapping(value = "/artificialAllocation", method = RequestMethod.POST)
    public Page<ArtificialAllocationRoomDto> artificialAllocation(@RequestBody @Valid ArtificialAllocationVo artificialAllocationVo) {
        return configureRoomService.artificialAllocation(artificialAllocationVo);
    }

    /**
     * 查询分配单位信息
     *
     * @param allocationUnitVo
     * @return
     */
    @ApiOperation(value = "查询分配单位信息", notes = "查询分配单位信息")
    @RequestMapping(value = "/allocationUnit", method = RequestMethod.POST)
    public Map<Object, Object> allocationUnit(@RequestBody @Valid AllocationUnitVo allocationUnitVo) {
        return configureRoomService.allocationUnitList(allocationUnitVo);
    }

    @ApiOperation(value = "查询分配单位信息", notes = "查询分配单位信息")
    @RequestMapping(value = "/configNewUnit", method = RequestMethod.POST)
    public List<NewUnitDto> configNewUnit(@RequestBody @Valid AllocationUnitVo allocationUnitVo) {
        return configureRoomService.configNewUnit(allocationUnitVo);
    }

    /**
     * 查询一层楼的房间信息
     *
     * @param floorHouseInfoVo
     * @return
     */
    @ApiOperation(value = "查询一层楼的房间信息", notes = "查询一层楼的房间信息")
    @RequestMapping(value = "/floorHouseInfo", method = RequestMethod.POST)
    public List<FloorHouseInfoDto> floorHouseInfo(@RequestBody @Valid FloorHouseInfoVo floorHouseInfoVo) {
        return configureRoomService.floorHouseInfo(floorHouseInfoVo);
    }


    /**
     * 按照楼层查询人工分房的房间信息
     *
     * @param allocationFloorVo
     * @return
     */
    @ApiOperation(value = "按照楼层查询人工分房的房间信息", notes = "按照楼层查询人工分房的房间信息")
    @RequestMapping(value = "/allocationFloor", method = RequestMethod.POST)
    public List<AllocationFloorDto> allocationFloor(@RequestBody @Valid AllocationFloorVo allocationFloorVo) {
        return configureRoomService.allocationFloor(allocationFloorVo);
    }

    /**
     * 查询配置步骤集合
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/selectConfigureStep", method = RequestMethod.POST)
    @ApiOperation(value = "查询配置步骤集合", notes = "查询配置步骤集合")
    public List<StepNodeDto> selectConfigureStep(@RequestBody @Valid BasicVo<Long> basicVo) {
        return configureService.selectConfigureStep(basicVo.getId());
    }

    /**
     * 领导查看配置流程
     *
     * @param leaderReviewVo
     * @return
     */
    @RequestMapping(value = "/leaderReviewDetail", method = RequestMethod.POST)
    @ApiOperation(value = "领导查看配置流程", notes = "领导查看配置流程")
    public ConLeaderDto leaderReviewDetail(@RequestBody @Valid LeaderReviewVo leaderReviewVo) {
        return configureService.leaderReviewDetail(leaderReviewVo);
    }

    /**
     * 查看分配方案数据(分配方案查询)
     *
     * @param planDataVo
     * @return
     */
    @RequestMapping(value = "/planDataDetail", method = RequestMethod.POST)
    @ApiOperation(value = "查看分配方案数据(智能)", notes = "查看分配方案数据(智能)")
    public Page<ConPlanDataDto> planDataDetail(@RequestBody @Valid PlanDataVo planDataVo) {
        return configureService.planDataDetail(planDataVo);
    }

    /**
     * 查看分配方案数据(人工)
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/planData", method = RequestMethod.POST)
    @ApiOperation(value = "查看分配方案数据(人工)", notes = "查看分配方案数据(人工)")
    public String planData(@RequestBody @Valid BasicVo<Long> basicVo) {
        return configureService.planData(basicVo.getId());
    }

    /**
     * 查询某个用户所有审批配置记录
     *
     * @param approvalRecordVo
     * @return
     */
    @RequestMapping(value = "/selectConfigureRecord", method = RequestMethod.POST)
    @ApiOperation(value = "查询某个用户所有审批配置记录", notes = "查询某个用户所有审批配置记录")
    public Page<ConfigureDto> selectConfigureRecord(@RequestBody @Valid ApprovalRecordVo approvalRecordVo) {

        return configureService.selectConfigureRecord(approvalRecordVo);
    }

    /**
     * 查询某个用户审批配置详情列表
     *
     * @param approvalConfigureVo
     * @return
     */
    @RequestMapping(value = "/selectApprovalDetail", method = RequestMethod.POST)
    @ApiOperation(value = "查询某个用户审批配置详情列表", notes = "查询某个用户审批配置详情列表")
    public List<StepNodeDto> selectApprovalDetail(@RequestBody @Valid ApprovalConfigureVo approvalConfigureVo) {

        return configureService.selectApprovalDetail(approvalConfigureVo);
    }

    /**
     * 查询单位房间面积变化
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/change", method = RequestMethod.POST)
    @ApiOperation(value = "查询单位房间面积变化", notes = "查询单位房间面积变化")
    public List<ConfigurePlanChangeData> change(@RequestBody @Valid BasicVo<Long> basicVo) {
        QueryWrapper<ConfigurePlanChangeData> wrapper = new QueryWrapper<>();
        wrapper.select("unit_names", "house_number", "use_area");
        if (basicVo.getId() != null) {
            wrapper.eq("plan_id", basicVo.getId());
        }
        return configurePlanChangeDataService.list(wrapper);

    }

    /**
     * 判断领导审批是否被打回
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/isLeaderApproval", method = RequestMethod.POST)
    @ApiOperation(value = "判断领导审批是否被打回", notes = "判断领导审批是否被打回")
    public String isLeaderApproval(@RequestBody @Valid BasicVo<Long> basicVo) {

        return configureService.isLeaderApproval(basicVo.getId());
    }

    /**
     * 查询每一层楼的房间信息
     *
     * @param floorHouseListVo
     * @return
     */
    @ApiOperation(value = "查询每一层楼的房间信息", notes = "查询每一层楼的房间信息")
    @RequestMapping(value = "/floorHouse", method = RequestMethod.POST)
    public List<String> floorHouse(@RequestBody @Valid FloorHouseListVo floorHouseListVo) {
        return configureRoomService.floorHouse(floorHouseListVo);
    }

    /**
     * 显示相关联的配置申请(houseIdList)
     *
     * @param houseListVo
     * @return
     */
    @RequestMapping(value = "/configureListByHouseId", method = RequestMethod.POST)
    @ApiOperation(value = "显示相关联的配置申请通过房间编号", notes = "显示相关联的配置申请")
    public Page<Configure> configureListByHouseId(@RequestBody @Valid HouseListVo houseListVo) {

        return configureService.configureListByHouseId(houseListVo);
    }

    /**
     * 显示相关联的配置申请(build+floorNameList)
     *
     * @param houseListVo
     * @return
     */
    @RequestMapping(value = "/configureListByFloor", method = RequestMethod.POST)
    @ApiOperation(value = "显示相关联的配置申请通过楼层", notes = "显示相关联的配置申请通过楼层")
    public Page<Configure> configureListByFloor(@RequestBody HouseListVo houseListVo) {
        if (houseListVo.getFloorHouseListVo() != null) {
            List<String> houseIdList = this.floorHouse(houseListVo.getFloorHouseListVo());
            houseListVo.setHouseIdList(houseIdList);
        }
        return configureService.configureListByFloor(houseListVo);
    }

    /**
     * 判断某一楼栋下是否关联配置
     *
     * @param floorHouseListVo
     * @return
     * @throws Throwable
     */
    @ApiOperation(value = "判断某一楼栋下是否关联配置", notes = "判断某一楼栋下是否关联配置")
    @RequestMapping(value = "/isUnionConfig", method = RequestMethod.POST)
    public boolean isUnionConfig(@RequestBody @Valid FloorHouseListVo floorHouseListVo) {
        return configureRoomService.isUnionConfig(floorHouseListVo);

    }

    /**
     * 获取相关联的配置申请Id集合
     *
     * @param houseListVo
     * @return
     */
    @RequestMapping(value = "/configureIdList", method = RequestMethod.POST)
    @ApiOperation(value = "获取相关联的配置申请Id集合", notes = "获取相关联的配置申请Id集合")
    public List<Long> configureIdList(@RequestBody HouseListVo houseListVo) {
        if (houseListVo.getFloorHouseListVo() != null) {
            List<String> houseIdList = this.floorHouse(houseListVo.getFloorHouseListVo());
            houseListVo.setHouseIdList(houseIdList);
        }
        return configureService.configureIdList(houseListVo);
    }

}
