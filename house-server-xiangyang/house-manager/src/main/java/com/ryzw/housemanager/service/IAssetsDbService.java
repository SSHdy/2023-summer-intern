package com.ryzw.housemanager.service;

import com.ryzw.housemanager.config.TokenFeignConfiguration;
import com.ryzw.housemanager.dto.HttpResultDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  调道邦账面资产接口
 * </p>
 *
 * @author wj
 * @since 2019/1/11 0011
 */
//@FeignClient(name = "asset-service",url = "${assert.internal-controls}",configuration = TokenFeignConfiguration.class)
@FeignClient(name = "asset-service",url = "${daoBang.internal-controls}",configuration = TokenFeignConfiguration.class)
public interface IAssetsDbService {

    /**
     * 调道邦接口查询全部账面资产
     * @return
     */
//    @RequestMapping(value = "/api/assets/purchase/getapprovaledlist",method = RequestMethod.POST)
    @RequestMapping(value = "/purchasing/getPurchasingList",method = RequestMethod.POST)
    HttpResultDto selectAssets();

    /**
     * 调道邦接口查询账面资产详情
     * @param purchasingId 采购项目编号
     * @return
     */
//    @RequestMapping(value = "/api/assets/purchase/getassetslist",method = RequestMethod.POST)
    @RequestMapping(value = "/purchasing/selectPurchasingById",method = RequestMethod.POST)
    HttpResultDto selectPurchasingById(@RequestParam("purchasingId") String purchasingId);

    /**
     * 调道邦接口资产入库
     * @param assetsDb
     * @return
     */
//    @RequestMapping(value = "/api/assets/asset/storagepush",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded")
    @RequestMapping(value = "/purchasing/storagePush",method = RequestMethod.POST)
    HttpResultDto<HttpResultDto.DataDetail> storagePush(String assetsDb);

}
