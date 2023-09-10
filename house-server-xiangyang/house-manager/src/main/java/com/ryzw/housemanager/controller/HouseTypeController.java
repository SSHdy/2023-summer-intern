package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryzw.housemanager.entity.HouseType;
import com.ryzw.housemanager.service.IHouseTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 房屋类型表 前端控制器
 * </p>
 *
 * @author lyx
 * @since 2018-12-13
 */
@RestController
@RequestMapping("/houseType")
@Api(value = "HouseTypeController", description = "房屋类型管理")
public class HouseTypeController {
    @Autowired
    IHouseTypeService houseTypeService;

    /**
     * 获取所有的房屋类型
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "房屋类型查询", notes = "房屋类型查询")
    public List<HouseType> getAllHouseType() {
        QueryWrapper<HouseType> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("houseType_id","houseType_Name","house_type_flag");
        queryWrapper.orderByAsc("houseType_id");
        return houseTypeService.list(queryWrapper);
    }
}
