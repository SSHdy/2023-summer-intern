package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.entity.Region;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IRegionService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.RegionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 区域表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-06-03
 */
@Api(value = "RegionController", description = "区域列表")
@Slf4j
@RestController
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private IRegionService regionService;

    /**
     * 区域列表
     *
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ApiOperation(value = "区域列表", notes = "区域列表")
    public List<Region> getList() {
        QueryWrapper<Region> wrapper = new QueryWrapper<>();
        wrapper.select("region_id", "region_name");
        wrapper.orderByAsc("region_id");
        return regionService.list(wrapper);
    }


    /**
     * 分页查询区域信息列表
     *
     * @param regionVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询区域信息列表", notes = "分页查询区域信息列表")
    public IPage<Region> getList(@RequestBody @Valid RegionVo regionVo) {
        QueryWrapper<Region> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(regionVo.getRegionName())) {
            wrapper.like("region_name", regionVo.getRegionName());
        }
        wrapper.orderByDesc("region_id");
        IPage<Region> page = new Page<>();
        page.setCurrent(regionVo.getCurrentPage());
        page.setSize(regionVo.getPageSize());
        return regionService.page(page, wrapper);
    }

    /**
     * 批量删除区域信息
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/batchDelete", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除区域信息", notes = "批量删除区域信息")
    public boolean deleteBatchPro(@RequestBody List<Long> ids) {
        try {
            return regionService.removeByIds(ids);
        } catch (Exception ex) {
            log.error("region/batchDelete error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 单条删除区域信息
     *
     * @param region
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "单条删除区域信息", notes = "单条删除区域信息")
    public boolean delete(@RequestBody Region region) {
        try {
            return regionService.removeById(region.getRegionId());
        } catch (Exception ex) {
            log.error("region/delete error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 新增或修改区域信息
     *
     * @param region
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "新增或修改区域信息", notes = "新增或修改区域信息")
    public boolean addPro(@RequestBody Region region) {
        try {
            return regionService.saveOrUpdate(region);
        } catch (Exception ex) {
            log.error("region/saveOrUpdate error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

}
