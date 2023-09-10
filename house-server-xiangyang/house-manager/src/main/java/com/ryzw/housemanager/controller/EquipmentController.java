package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.EquipmentDetailDto;
import com.ryzw.housemanager.entity.Build;
import com.ryzw.housemanager.entity.Equipment;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IBuildService;
import com.ryzw.housemanager.service.IEquipmentService;
import com.ryzw.housemanager.service.IYardService;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2018-11-05
 */
@RestController
@RequestMapping("/equipment")
@Api(value = "EquipmentController", description = "资产管理：重大设备")
@Slf4j
public class EquipmentController {
    @Autowired
    private IEquipmentService equipmentService;
    @Autowired
    private IYardService yardService;
    @Autowired
    private IBuildService buildService;
    @Autowired
    private IAuthorityUnitService authorityUnitService;

    /**
     * 用户权限下的设备信息列表
     *
     * @return
     */
    @RequestMapping(value = "equipmentList", method = RequestMethod.POST)
    @ApiOperation(value = "用户权限下的设备信息列表", notes = "用户权限下的设备信息列表")
    public IPage<Equipment> equipmentList(@RequestBody @Valid PageVo pageVo) {
        QueryWrapper<Equipment> wrapper = new QueryWrapper<>();
        //获取用户可以查看的单位信息
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();
        List<YardResultVo> yardList = yardService.getAllByUnitID(unitIds);
        wrapper.in("yard_id", yardList.stream().map(yardResultVo -> yardResultVo.getYardId()).collect(Collectors.toList()));
        IPage<Equipment> page = new Page<>();
        page.setCurrent(pageVo.getCurrentPage());
        page.setSize(pageVo.getPageSize());
        return equipmentService.page(page, wrapper);
    }

    /**
     * 查询某个楼栋下的最后几条设备信息列表
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "equipmentLast", method = RequestMethod.POST)
    @ApiOperation(value = "查询某个楼栋下的最后几条设备信息列表", notes = "查询某个楼栋下的最后几条设备信息列表")
    public List<Equipment> equipmentLast(@RequestBody @Valid BasicVo<Long> basicVo) {
        return equipmentService.equipmentLast(basicVo);
    }


    /**
     * 查询楼座id,名称
     *
     * @param yardVo
     * @return
     */
    @RequestMapping(value = "/buildDetails", method = RequestMethod.POST)
    @ApiOperation(value = "查询楼座id,名称", notes = "查询楼座id,名称")
    public List<Build> yardDetails(@RequestBody @Valid YardVo yardVo) {
        return buildService.list(new QueryWrapper<Build>().eq("yard_id", yardVo.getYardId()).select("build_id", "build_name"));
    }

    /**
     * 批量删除设备信息
     *
     * @param basicIdsVo
     * @return
     */
    @RequestMapping(value = "/deleteBatch", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除设备信息", notes = "批量删除设备信息")
    public boolean deleteBatch(@RequestBody @Valid BasicIdsVo<Long> basicIdsVo) {
        List<Long> ids = basicIdsVo.getIds();
        return equipmentService.removeByIds(ids);
    }


    /**
     * 删除单个设备信息
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除单个设备信息", notes = "删除单个设备信息")
    public boolean delete(@RequestBody @Valid BasicVo<Long> basicVo) {
        return equipmentService.delete(basicVo);
    }

    /**
     * 新增设备信息
     *
     * @param equipmentVo
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增设备信息", notes = "新增设备信息")
    public boolean addEquipment(@RequestBody EquipmentVo equipmentVo) {
        return equipmentService.addEquipment(equipmentVo);
    }

    /**
     * 修改设备信息
     *
     * @param equipmentVo
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改设备信息", notes = "修改设备信息")
    public boolean update(@RequestBody EquipmentVo equipmentVo) {
        return equipmentService.update(equipmentVo);
    }

    /**
     * 查询设备信息详情
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation(value = "查询设备信息详情", notes = "查询设备信息详情")
    public EquipmentDetailDto equipmentDetail(@RequestBody @Valid BasicVo<Long> basicVo) {
        return equipmentService.equipmentDetail(basicVo);
    }
}
