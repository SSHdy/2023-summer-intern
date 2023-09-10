package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.AssetsComnDto;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IAssetsService;
import com.ryzw.housemanager.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 资产表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-01-10
 */
@Slf4j
@Api(value = "资产信息列表", description = "资产信息列表")
@RestController
@RequestMapping("/assets")
public class AssetsController {

    @Autowired
    private IAssetsService assetsService;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 通过mongodb分页查询资产信息列表
     *
     * @param assetsComnVo
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ApiOperation(value = "通过mongodb分页查询资产信息列表", notes = "通过mongodb分页查询资产信息列表")
    public Page<AssetsComnDto> pageList(@RequestBody @Valid AssetsComnVo assetsComnVo) {
        return assetsService.pageList(assetsComnVo);
    }

    /**
     * 通过资产编号查询资产信息
     *
     * @param findByAssetIdVo
     * @return
     */
    @RequestMapping(value = "findByAssetId", method = RequestMethod.POST)
    @ApiOperation(value = "通过资产编号查询资产信息", notes = "通过资产编号查询资产信息")
    public Page<Document> findByAssetId(@RequestBody @Valid FindByAssetIdVo findByAssetIdVo) {
        return assetsService.findByAssetId(findByAssetIdVo);
    }

    /**
     * 查询已经入库的资产信息
     * @param assetsStorageVo
     * @return
     */
    @RequestMapping(value = "storageAsset", method = RequestMethod.POST)
    @ApiOperation(value = "查询已经入库的资产信息", notes = "查询已经入库的资产信息")
    public Page<AssetsComnDto> storageAsset(@RequestBody @Valid AssetsStorageVo assetsStorageVo) {
        return assetsService.storageAsset(assetsStorageVo);
    }

    /**
     * 通过mongodb查询资产信息详情
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation(value = "通过mongodb查询资产信息详情", notes = "通过mongodb查询资产信息详情")
    public Document detail(@RequestBody @Valid BasicVo<String> basicVo) {
        Document document = mongoTemplate.findById(basicVo.getId(), Document.class, "assert");
        return document;
    }

    /**
     * 账面资产列表
     * @return
     */
    @RequestMapping(value = "/selectAssets", method = RequestMethod.POST)
    @ApiOperation(value = "账面资产列表", notes = "账面资产列表")
    public Object selectAssets() {
        return assetsService.selectAssets();
    }

    /**
     * 账面资产详情
     * @param assetsByIdVo
     * @return
     */
    @RequestMapping(value = "/selectPurchasingById", method = RequestMethod.POST)
    @ApiOperation(value = "账面资产详情", notes = "账面资产详情")
    public Object selectPurchasingById(@RequestBody @Valid AssetsByIdVo assetsByIdVo) {
        return assetsService.selectPurchasingById(assetsByIdVo);
    }


    /**
     * 资产入库
     *
     * @param assetsDbVo
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ApiOperation(value = "资产入库", notes = "资产入库")
    public List<String> add(@RequestBody AssetsDbVo assetsDbVo) {
        return assetsService.add(assetsDbVo);
    }


    /**
     * 资产修改
     *
     * @param
     * @return
     */
    @RequestMapping(value = "updateAssert", method = RequestMethod.POST)
    @ApiOperation(value = "资产修改", notes = "资产修改")
    public boolean updateAssert(@RequestBody @Valid AssetsUpdateVo assetsUpdateVo) {
        return assetsService.updateAssert(assetsUpdateVo);
    }

    /**
     * 修改资产信息
     *
     * @param renewalAssetVo
     * @return
     */
    @RequestMapping(value = "renewal", method = RequestMethod.POST)
    @ApiOperation(value = "修改资产信息", notes = "修改资产信息")
    public boolean renewal(@RequestBody @Valid RenewalAssetVo renewalAssetVo) {
        return assetsService.renewal(renewalAssetVo);
    }

    /**
     * 资产入库数据导出Excel
     *
     * @param
     * @param response
     * @return
     */
    @RequestMapping(value = "exportAssetsExcel", method = RequestMethod.POST)
    @ApiOperation(value = "资产导出", notes = "资产导出")
    public boolean exportAssetsExcel(@RequestBody @Valid AssetsExportVo assetsExportVo, HttpServletResponse response) {
        try {
            return assetsService.exportAssetsExcel(assetsExportVo, response);
        } catch (Exception ex){
            log.error("error",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }

    /**
     * 按操作时间所有操作类型统计
     * @param assetsStatisticsVo
     * @return
     */
    @RequestMapping(value = "assertStatistics", method = RequestMethod.POST)
    @ApiOperation(value = "资产统计", notes = "资产统计")
    public List<Document> assertStatistics(@RequestBody @Valid AssetsStatisticsVo assetsStatisticsVo) {
        return assetsService.assertStatistics(assetsStatisticsVo);
    }
}
