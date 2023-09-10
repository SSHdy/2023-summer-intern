package com.ryzw.housemanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.entity.Property;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IPropertyService;
import com.ryzw.housemanager.vo.BasicIdsVo;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.PropertyDetailVo;
import com.ryzw.housemanager.vo.PropertyVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
 *  资产前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-10-17
 */
@Slf4j
@Api(value = "资产信息管理",description = "资产信息管理")
@RestController
@RequestMapping("/property")
public class PropertyController {
    @Autowired
    private IPropertyService propertyService;

    /**
     * 新增或修改资产信息
     * @param property
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    @ApiOperation(value = "新增或修改资产信息",notes = "新增或修改资产信息")
    public boolean addPro(@RequestBody Property property) {
        try {
            return propertyService.saveOrUpdate(property);
        } catch (Exception ex){
            log.error("addProError",ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除一条资产信息
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除一条资产信息",notes = "删除一条资产信息")
    public boolean delete(@RequestBody BasicVo<Long> basicVo) {
        try {
            return propertyService.removeById(basicVo.getId());
        } catch (Exception ex){
            log.error("deleteError",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }

    /**
     * 批量删除资产信息
     * @param basicIdsVo
     * @return
     */
    @RequestMapping(value = "/batchDelete",method = RequestMethod.POST)
    @ApiOperation(value = "批量删除资产信息",notes = "批量删除资产信息")
    public boolean deleteBatchPro(@RequestBody BasicIdsVo<Long> basicIdsVo) {
        try {
            return propertyService.removeByIds(basicIdsVo.getIds());
        } catch (Exception ex){
            log.error("deleteBatchProError",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }

    /**
     * 资产管理列表
     * @param propertyVo
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "资产管理列表",notes = "资产管理列表")
    public IPage<Property> getList(@RequestBody @Valid PropertyVo propertyVo) {
        try {
            QueryWrapper<Property> wrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(propertyVo.getPurchasingId())) {
                wrapper.like("purchasing_id", propertyVo.getPurchasingId());
            }
            wrapper.orderByDesc("asset_id");
            IPage<Property> page = new Page<>();
            page.setCurrent(propertyVo.getCurrentPage());
            page.setSize(propertyVo.getPageSize());
            return propertyService.page(page,wrapper);
        }catch (Exception ex){
            log.error("getListError",ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 资产信息详情
     * @param propertyDetailVo
     * @return
     */
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    @ApiOperation(value = "资产信息详情",notes = "资产信息详情")
    public List<Property> detail(@RequestBody PropertyDetailVo propertyDetailVo) {
        try {
            QueryWrapper<Property> wrapper = new QueryWrapper<>();
            if (StringUtils.isNotEmpty(propertyDetailVo.getPurchasingId())) {
                wrapper.eq("purchasing_id", propertyDetailVo.getPurchasingId());
            }
            return propertyService.list(wrapper);
        } catch (Exception ex){
            log.error("addProError",ex);
            throw new ServiceException(ex.getMessage());
        }
    }

}
