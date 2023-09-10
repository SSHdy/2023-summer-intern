package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.HouseDto;
import com.ryzw.housemanager.dto.RoomDetailDto;
import com.ryzw.housemanager.service.IHouseService;
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
 * 房屋信息表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2018-12-07
 */
@RestController
@Slf4j
@Api(value = "房屋信息管理", description = "房屋信息管理")
@RequestMapping("/house")
public class HouseController {
    @Autowired
    IHouseService houseService;

    /**
     * 添加房屋UUID，修改房屋，单位，职称等信息，根据UUID删除房间
     *
     * @param floorVo
     * @return
     */
    @RequestMapping(value = "addUpdateDeleteHouseAndJsonPlan", method = RequestMethod.POST)
    @ApiOperation(value = "添加房屋UUID，修改房屋，单位，职称等信息，根据UUID删除房间", notes = "添加房屋UUID，修改房屋，单位，职称等信息，根据UUID删除房间")
    public boolean addUpdateDeleteHouseAndJsonPlan(@RequestBody @Valid FloorVo floorVo) {
        //获取院落id
        Long yardId = floorVo.getYardId();
        //获取楼座id
        Long buildId = floorVo.getBuildId();
        //获取楼层
        Integer floorName = floorVo.getFloorName();

        //获取备案人
        String recordHolder = floorVo.getRecordHolder();

        //获取备案人id
        Long recordHolderId = floorVo.getRecordHolderId();

        //获取新增的UUID房间编号数组
        List<String> addHouseIdList = floorVo.getAddHouseIdList();
        //获取修改房屋信息的数组
        List<HouseVo> houseVoList = floorVo.getHouseVoList();
        //获取删除的UUID房间编号数组
        List<String> delHouseIdList = floorVo.getDelHouseIdList();

        //获取平面图json对象
        JsonPlanVo jsonPlanVo = floorVo.getJsonPlanVo();

        //获取选择回退的配置申请
        List<Long> configureIdList = floorVo.getConfigureIds();

        //获取出租转自用房的房间号数组
        List<String> rentHouseIds = floorVo.getRentHouseIds();

        return houseService.addUpdateDeleteHouseAndPlan(yardId, buildId, floorName, recordHolder, recordHolderId, addHouseIdList, houseVoList, delHouseIdList, configureIdList, rentHouseIds, jsonPlanVo);
    }


    /**
     * 房屋信息详情
     *
     * @param houseDetailVo
     * @return
     */
    @ApiOperation(value = "房屋信息详情", notes = "房屋信息详情")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public HouseDto houseDetails(@RequestBody @Valid HouseDetailVo houseDetailVo) {
        return houseService.houseDetail(houseDetailVo);
    }

    /**
     * 楼层所有房屋信息列表
     *
     * @param
     * @return
     */
    @ApiOperation(value = "楼层所有房屋信息列表", notes = "楼层所有房屋信息列表")
    @RequestMapping(value = "/allInfo", method = RequestMethod.POST)
    public List<HouseDto> allInfo(@RequestBody @Valid FloorHouseVo floorHouseVo) {
        return houseService.floorHouse(floorHouseVo);
    }

    /**
     * 根据buildId，floorName查询mongodb的对应的json
     *
     * @param jsonPlanVo
     * @return
     */
    @ApiOperation(value = "json查询", notes = "json查询")
    @RequestMapping(value = "/jsonPlan", method = RequestMethod.POST)
    public Map<String, Object> getJsonPlan(@RequestBody JsonPlanVo jsonPlanVo) {
        return houseService.selectJsonPlan(jsonPlanVo);
    }


    /**
     * 房间明细查询
     *
     * @param roomDetailVo
     * @return
     */
    @ApiOperation(value = "房间明细查询", notes = "房间明细查询")
    @RequestMapping(value = "/roomDetail", method = RequestMethod.POST)
    public Page<RoomDetailDto> roomDetail(@RequestBody @Valid RoomDetailVo roomDetailVo) {
        return houseService.roomDetail(roomDetailVo);
    }


}
