package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryzw.housemanager.entity.UnitType;
import com.ryzw.housemanager.service.IUnitTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyx
 * @since 2018-12-12
 */
@RestController
@RequestMapping("/unitType")
@Api(value = "UnitTypeController", description = "类型管理")
public class UnitTypeController {
    @Autowired
    IUnitTypeService unitTypeService;

    /**
     * 单位类型查询
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "单位类型查询", notes = "单位类型查询")
    public List<UnitType> getAllUnitType() {
        QueryWrapper<UnitType> wrapper = new QueryWrapper<>();
        wrapper.orderByAsc("unitType_id");
        return unitTypeService.list(wrapper);
    }
}
