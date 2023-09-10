package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryzw.housemanager.dto.PurchasingDetailDto;
import com.ryzw.housemanager.entity.Property;
import com.ryzw.housemanager.entity.Purchasing;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.PropertyMapper;
import com.ryzw.housemanager.mapper.PurchasingMapper;
import com.ryzw.housemanager.service.IPropertyService;
import com.ryzw.housemanager.service.IPurchasingService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.vo.PurchasingAddVo;
import javafx.beans.property.ListProperty;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 采购表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-10-17
 */
@Service
@Slf4j
public class PurchasingServiceImpl extends ServiceImpl<PurchasingMapper, Purchasing> implements IPurchasingService {

    @Autowired
    private IPropertyService propertyService;
    @Autowired
    private PurchasingMapper purchasingMapper;
    @Autowired
    private DozerBeanMapper dozerBeanMapper;
    @Autowired
    private PropertyMapper propertyMapper;
    /**
     * 采购详情和资产详情
     * @param purchasingId
     * @return
     */
    @Override
    public PurchasingDetailDto purchasingDetail(Long purchasingId) {
        try {
            PurchasingDetailDto purchasingDetailDto = new PurchasingDetailDto();

            QueryWrapper<Purchasing> wrapper = new QueryWrapper<>();
            if (purchasingId != null) {
                wrapper.eq("purchasing_id", purchasingId);
            }
           Purchasing baseInfo = purchasingMapper.selectOne(wrapper);

            QueryWrapper<Property> propertyWrapper = new QueryWrapper<>();
            if (purchasingId != null) {
                propertyWrapper.eq("purchasing_id",purchasingId);
            }
           List<Property> assetInfoList = propertyService.list(propertyWrapper);

            purchasingDetailDto.setBaseInfo(baseInfo);
            purchasingDetailDto.setAssetInfoList(assetInfoList);

            return purchasingDetailDto;
        } catch (Exception ex){
            log.error("purchasingDetail/Error",ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 添加采购信息和资产信息
     * @param purchasingAddVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PurchasingDetailDto add(PurchasingAddVo purchasingAddVo) {
        try{
            PurchasingDetailDto purchasingDetailDto = new PurchasingDetailDto();
            if (purchasingAddVo.getBaseInfo() != null ){
                Purchasing purchasing = dozerBeanMapper.map(purchasingAddVo.getBaseInfo(),Purchasing.class);
                purchasing.setStatus(0);
                purchasingMapper.insert(purchasing);

                purchasingDetailDto.setBaseInfo(purchasing);

                List<Property> assetInfoList = new ArrayList<>();
                if (purchasingAddVo.getAssetInfoList() != null){
                    for (Property property:purchasingAddVo.getAssetInfoList()){
                        property.setPurchasingId(purchasing.getPurchasingId());
                        propertyMapper.insert(property);
                        assetInfoList.add(property);
                    }
                    purchasingDetailDto.setAssetInfoList(assetInfoList);
                }
            }

            return purchasingDetailDto;
        }catch (Exception ex){
            log.error("add/Error",ex);
            throw new ServiceException(ex.getMessage());
        }

    }
}
