package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.AssetsComnDto;
import com.ryzw.housemanager.entity.Assets;
import com.ryzw.housemanager.vo.*;
import org.bson.Document;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 资产表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-01-10
 */
public interface IAssetsService extends IService<Assets> {
    /**
     * 资产入库
     * @param assetsDbVo  资产
     * @return
     */
    List<String> addAssets(AssetsDbVo assetsDbVo);

    /**
     * 资产入库数据导出Excel
     * @return
     */
    Boolean exportAssetsExcel(AssetsExportVo assetsExportVo, HttpServletResponse response);

    /**
     * 通过mongodb分页查询资产信息列表
     * @param assetsComnVo
     * @return
     */
    Page<AssetsComnDto> pageList( AssetsComnVo assetsComnVo);

    /**
     * 通过资产编号查询资产信息
     * @param findByAssetIdVo
     * @return
     */
    Page<Document> findByAssetId(FindByAssetIdVo findByAssetIdVo);

    /**
     * 查询已经入库的资产信息
     * @param assetsStorageVo
     * @return
     */
    Page<AssetsComnDto> storageAsset(AssetsStorageVo assetsStorageVo);

    /**
     * 账面资产列表
     * @return
     */
    Object selectAssets();

    /**
     * 账面资产详情
     * @param assetsByIdVo
     * @return
     */
    Object selectPurchasingById(AssetsByIdVo assetsByIdVo);

    /**
     * 资产入库
     * @param assetsDbVo
     * @return
     */
    List<String> add(AssetsDbVo assetsDbVo);

    /**
     * 资产修改
     * @param assetsUpdateVo
     * @return
     */
    boolean updateAssert(AssetsUpdateVo assetsUpdateVo);

    /**
     * 修改资产信息
     * @param renewalAssetVo
     * @return
     */
    boolean renewal(RenewalAssetVo renewalAssetVo);

    /**
     * 按操作时间所有操作类型统计
     * @param assetsStatisticsVo
     * @return
     */
    List<Document> assertStatistics(AssetsStatisticsVo assetsStatisticsVo);

}
