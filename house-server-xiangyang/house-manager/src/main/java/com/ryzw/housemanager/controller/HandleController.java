package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.service.IHandleService;
import com.ryzw.housemanager.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 处置表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-05-24
 */
@RestController
@RequestMapping("/handle")
@Slf4j
@Api(value = "处置申请管理", description = "处置申请管理")
public class HandleController {

    @Autowired
    IHandleService handleService;

    /**
     * 新增处置申请
     *
     * @param handleAddVo
     * @return
     */
    @RequestMapping(value = "addHandle", method = RequestMethod.POST)
    @ApiOperation(value = "新增处置申请", notes = "新增处置申请")
    public boolean addHandle(@RequestBody @Valid HandleAddVo handleAddVo) {

        return handleService.addHandle(handleAddVo);

    }

    /**
     * 修改处置申请
     *
     * @param handleUpdateVo
     * @return
     */
    @RequestMapping(value = "updateHandle", method = RequestMethod.POST)
    @ApiOperation(value = "修改处置申请", notes = "修改处置申请")
    public boolean updateHandle(@RequestBody @Valid HandleUpdateVo handleUpdateVo) {

        return handleService.updateHandle(handleUpdateVo);

    }

    /**
     * 处置申请详情
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "handleDetail", method = RequestMethod.POST)
    @ApiOperation(value = "处置申请详情", notes = "处置申请详情")
    public HandleDetailDto handleDetail(@RequestBody @Valid BasicVo<Long> basicVo) {

        return handleService.handleDetail(basicVo.getId());

    }

    /**
     * 处置申请列表
     *
     * @param handleVo
     * @return
     */
    @RequestMapping(value = "handleList", method = RequestMethod.POST)
    @ApiOperation(value = "处置申请列表", notes = "处置申请列表")
    public Page<HandleDetailDto> handleList(@RequestBody HandleVo handleVo) {

        return handleService.handleList(handleVo);

    }


    /**
     * 更新用户处置权限
     *
     * @return
     */
    @RequestMapping(value = "/updateHandleAuthority", method = RequestMethod.POST)
    @ApiOperation(value = "更新用户处置权限", notes = "更新用户处置权限")
    public boolean updateHandleAuthority(@RequestBody @Valid ActivityAuthorityVo activityAuthorityVo) {
        return handleService.updateHandleAuthority(activityAuthorityVo);
    }

    /**
     * 处置审批列表
     *
     * @param handleVo
     * @return
     */
    @RequestMapping(value = "handleApprovalList", method = RequestMethod.POST)
    @ApiOperation(value = "处置审批列表", notes = "处置审批列表")
    public Page<HandleDetailDto> handleApprovalList(@RequestBody HandleVo handleVo) {
        return handleService.handleApprovalList(handleVo);
    }

    /**
     * 审核申请审批
     *
     * @param handleFlowVo
     * @return
     */
    @ApiOperation(value = "审核申请审批", notes = "审核申请审批")
    @RequestMapping(value = "/saveSubmitHandle", method = RequestMethod.POST)
    public boolean saveSubmitHandle(@RequestBody @Valid HandleFlowVo handleFlowVo) {
        return handleService.saveSubmitHandle(handleFlowVo);
    }

    /**
     * 新增时查询可以处置的位置
     */
    @ApiOperation(value = "新增时查询可以处置的位置", notes = "新增时查询可以处置的位置")
    @RequestMapping(value = "/selectHandlePo", method = RequestMethod.POST)
    public List<Map<String, Object>> selectHandlePo() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<HandlePositionDto> handlePositionDtoList = handleService.selectHandlePo();
        return this.selectHanPo(handlePositionDtoList, mapList);
    }

    /**
     * 查询处置位置树
     *
     * @param handlePositionDtoList
     * @param mapList
     * @return
     */
    public List<Map<String, Object>> selectHanPo(List<HandlePositionDto> handlePositionDtoList, List<Map<String, Object>> mapList) {
        if (CollectionUtils.isNotEmpty(handlePositionDtoList)) {
            for (HandlePositionDto handlePositionDto : handlePositionDtoList) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", handlePositionDto.getYardName());
                map.put("value", handlePositionDto.getYardId());
                map.put("id", handlePositionDto.getYardId());
                map.put("type", 0);
                if (CollectionUtils.isNotEmpty(handlePositionDto.getBuildList())) {
                    List<Map<String, Object>> childList = new ArrayList<>();
                    for (BuildPositionDto buildPositionDto : handlePositionDto.getBuildList()) {
                        Map<String, Object> childMap = new HashMap<>();
                        childMap.put("name", buildPositionDto.getBuildName());
                        childMap.put("value", buildPositionDto.getBuildId());
                        childMap.put("id", handlePositionDto.getYardId() + ":" + buildPositionDto.getBuildId());
                        childMap.put("type", 1);
                        if (CollectionUtils.isNotEmpty(buildPositionDto.getFloorNameDtoList())) {
                            List<Map<String, Object>> floorList = new ArrayList<>();
                            for (FloorNameDto floorNameDto : buildPositionDto.getFloorNameDtoList()) {
                                Map<String, Object> floorMap = new HashMap<>();
                                floorMap.put("name", floorNameDto.getFloorName());
                                floorMap.put("value", floorNameDto.getFloorName());
                                floorMap.put("id", handlePositionDto.getYardId() + ":" + buildPositionDto.getBuildId() + ":" + floorNameDto.getFloorName());
                                floorMap.put("type", 2);
                                floorList.add(floorMap);
                                if (CollectionUtils.isNotEmpty(floorNameDto.getHouseNumber())) {
                                    List<Map<String, Object>> houseList = this.houseList(handlePositionDto, buildPositionDto, floorNameDto);
                                    floorMap.put("children", houseList);
                                }
                            }
                            childMap.put("children", floorList);
                        }
                        childList.add(childMap);
                        map.put("children", childList);
                    }
                }
                mapList.add(map);
            }
        }
        return mapList;
    }


    /**
     * 遍历树--房间
     */
    public List<Map<String, Object>> houseList(HandlePositionDto handlePositionDto, BuildPositionDto buildPositionDto, FloorNameDto floorNameDto) {
        List<Map<String, Object>> houseList = new ArrayList<>();
        for (HouseNumDto houseNumDto : floorNameDto.getHouseNumber()) {
            Map<String, Object> houseMap = new HashMap<>();
            houseMap.put("name", houseNumDto.getHouseNumber());
            houseMap.put("value", houseNumDto.getHouseId());
            houseMap.put("id", handlePositionDto.getYardId() + ":" +
                    buildPositionDto.getBuildId() + ":" + floorNameDto.getFloorName() + ":" + houseNumDto.getHouseId());
            houseMap.put("type", 3);
            houseList.add(houseMap);
        }
        return houseList;
    }

    /**
     * 修改时查询可以处置的位置
     */
    @ApiOperation(value = "修改时查询可以处置的位置", notes = "修改时查询可以处置的位置")
    @RequestMapping(value = "/selectHandlePoUp", method = RequestMethod.POST)
    public List<Map<String, Object>> selectHandlePoUp(@RequestBody @Valid BasicVo<Long> basicVo) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<HandlePositionDto> handlePositionDtoList = handleService.selectHandlePoUp(basicVo.getId());
        return this.selectHanPo(handlePositionDtoList, mapList);
    }

    /**
     * 查询所有院落楼栋树
     *
     * @return
     */
    @ApiOperation(value = "查询所有院落楼栋树", notes = "查询所有院落楼栋树")
    @RequestMapping(value = "/selectAllHandlePo", method = RequestMethod.POST)
    public List<Map<String, Object>> selectAllHandlePo() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<HandlePositionDto> handlePositionDtoList = handleService.selectAllHandlePo();
        return this.selectHanPo(handlePositionDtoList, mapList);
    }


    /**
     * 查询处置类型列表
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/selectHandleType", method = RequestMethod.POST)
    @ApiOperation(value = "查询处置类型列表", notes = "查询处置类型列表")
    public List<StepNodeDto> selectHandleType(@RequestBody @Valid BasicVo<Long> basicVo) {
        return handleService.selectHandleType(basicVo.getId());
    }

    /**
     * 领导查看处置流程
     *
     * @param leaderReviewVo
     * @return
     */
    @RequestMapping(value = "/leaderReviewDetail", method = RequestMethod.POST)
    @ApiOperation(value = "领导查看处置流程", notes = "领导查看处置流程")
    public List<Document> leaderReviewDetail(@RequestBody LeaderReviewVo leaderReviewVo) {
        return handleService.leaderReviewDetail(leaderReviewVo);
    }

    /**
     * 领导查看
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/leaderDetail", method = RequestMethod.POST)
    @ApiOperation(value = "领导查看", notes = "领导查看")
    public LeaderDetailDto leaderDetail(@RequestBody @Valid BasicVo<Long> basicVo) {
        return handleService.leaderDetail(basicVo.getId());
    }


    /**
     * 查询处置列表（不包含出租出借）
     * @param handleLsVo
     * @return
     */
    @RequestMapping(value = "handleLs", method = RequestMethod.POST)
    @ApiOperation(value = "查询处置列表（不包含出租出借）", notes = "查询处置列表（不包含出租出借）")
    public Page<HandleDetailDto> handleLs(@RequestBody @Valid HandleLsVo handleLsVo) {
        return handleService.handleLs(handleLsVo);
    }


    /**
     * 查询处置列表（不包含出租出借）
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "handleLsDetail", method = RequestMethod.POST)
    @ApiOperation(value = "查询处置详情（不包含出租出借）", notes = "查询处置详情（不包含出租出借）")
    public HandleLsDto handleLsDetail(@RequestBody @Valid BasicVo<Long> basicVo) {
        return handleService.handleLsDetail(basicVo);
    }

}