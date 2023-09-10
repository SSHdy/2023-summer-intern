package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.PurchasingDetailDto;
import com.ryzw.housemanager.entity.Purchasing;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IPropertyService;
import com.ryzw.housemanager.service.IPurchasingService;
import com.ryzw.housemanager.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 采购表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-10-17
 */
@Slf4j
@Api(value = "PurchasingController",description = "采购信息管理")
@RestController
@RequestMapping("/purchasing")
//@RequiredArgsConstructor
public class PurchasingController {

    @Autowired
    private IPurchasingService purchasingService;
    @Autowired
    private DozerBeanMapper dozerBeanMapper;
    @Autowired
    private IPropertyService propertyService;

    /**
     * 新增或修改采购信息
     * @param purchasingInfoVo
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    @ApiOperation(value = "新增或修改采购信息",notes = "新增或修改采购信息")
    public boolean addPro(@RequestBody PurchasingInfoVo purchasingInfoVo) {
        try {
            Purchasing purchasing = dozerBeanMapper.map(purchasingInfoVo,Purchasing.class);
            return purchasingService.saveOrUpdate(purchasing);
        } catch (Exception ex){
            log.error("addProError",ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除一条采购信息
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除一条采购信息",notes = "删除一条采购信息")
    public boolean delete(@RequestBody BasicVo<Long> basicVo) {
        try {
            return purchasingService.removeById(basicVo.getId());
        } catch (Exception ex){
            log.error("deleteError",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }

    /**
     * 批量删除采购信息
     * @param basicIdsVo
     * @return
     */
    @RequestMapping(value = "/batchDelete",method = RequestMethod.POST)
    @ApiOperation(value = "批量删除采购信息",notes = "批量删除采购信息")
    public boolean deleteBatchPro(@RequestBody BasicIdsVo<Long> basicIdsVo) {
        try {
            return purchasingService.removeByIds(basicIdsVo.getIds());
        } catch (Exception ex){
            log.error("deleteBatchProError",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }

    /**
     * 采购项目管理列表
     * @param purchasingVo
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "采购项目管理列表",notes = "采购项目管理列表")
    public IPage<Purchasing> getList(@RequestBody @Valid PurchasingVo purchasingVo) {
        try {
            QueryWrapper<Purchasing> wrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(purchasingVo.getPurchasingPname())) {
                wrapper.like("purchasing_pname", purchasingVo.getPurchasingPname());
            }
            if (purchasingVo.getStatus() != null){
                wrapper.eq("status",purchasingVo.getStatus());
            }
            wrapper.orderByDesc("purchasing_id");
            IPage<Purchasing> page = new Page<>();
            page.setCurrent(purchasingVo.getCurrentPage());
            page.setSize(purchasingVo.getPageSize());
            return purchasingService.page(page,wrapper);
        }catch (Exception ex){
            log.error("getListError",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }


    /**
     * 采购信息详情
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    @ApiOperation(value = "采购信息详情",notes = "采购信息详情")
    public Purchasing detail(@RequestBody BasicVo<Long> basicVo) {
        try {
            QueryWrapper<Purchasing> wrapper = new QueryWrapper<>();
            if (basicVo.getId() != null) {
                wrapper.eq("purchasing_id", basicVo.getId());
            }
            return purchasingService.getOne(wrapper);
        } catch (Exception ex){
            log.error("addProError",ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 采购详情和资产详情
     * @param purchasingDetailVo
     * @return
     */
    @RequestMapping(value = "/purchasingDetail",method = RequestMethod.POST)
    @ApiOperation(value = "采购详情和资产详情",notes = "采购详情和资产详情")
    public PurchasingDetailDto purchasingDetail(@RequestBody PurchasingDetailVo purchasingDetailVo) {
        return purchasingService.purchasingDetail(purchasingDetailVo.getPurchasingId());
    }

    /**
     * 添加采购信息和资产信息
     * @param purchasingAddVo
     * @return
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @ApiOperation(value = "添加采购信息和资产信息",notes = "添加采购信息和资产信息")
    public PurchasingDetailDto add(@RequestBody PurchasingAddVo purchasingAddVo) {
        return purchasingService.add(purchasingAddVo);
    }

}
