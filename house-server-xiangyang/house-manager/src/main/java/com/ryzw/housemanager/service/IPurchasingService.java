package com.ryzw.housemanager.service;

import com.ryzw.housemanager.dto.PurchasingDetailDto;
import com.ryzw.housemanager.entity.Purchasing;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.vo.PurchasingAddVo;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 采购表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-10-17
 */
public interface IPurchasingService extends IService<Purchasing> {

    /**
     * 采购详情和资产详情
     * @param purchasingId
     * @return
     */
    PurchasingDetailDto purchasingDetail(Long purchasingId);

    /**
     * 添加采购和资产信息
     * @param purchasingAddVo
     * @return
     */
    PurchasingDetailDto add(PurchasingAddVo purchasingAddVo);
}
