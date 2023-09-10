package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.entity.Estate;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.EstateMapper;
import com.ryzw.housemanager.service.IEstateService;
import com.ryzw.housemanager.vo.EstateVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 物业信息表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-05-05
 */
@Service
@Slf4j
public class EstateServiceImpl extends ServiceImpl<EstateMapper, Estate> implements IEstateService {

    @Autowired
    EstateMapper estateMapper;

    /**
     * 查询物业信息列表
     *
     * @param estateVo
     * @return
     */
    @Override
    public Page<Estate> selectEstateList(EstateVo estateVo) {
        try {
            Page<Estate> page = new Page<>(estateVo.getCurrentPage(), estateVo.getPageSize());
            return estateMapper.selectEstateList(page, estateVo);
        } catch (Exception ex) {
            log.error("selectEstateListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除物业信息
     *
     * @param estateId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEstate(Long estateId) {
        try {
            Integer count = estateMapper.selectCountEstate(estateId);
            if(count>0){
                throw new ServiceException("该物业有关联院落，不能删除");
            }
            estateMapper.deleteById(estateId);
            return true;
        } catch (Exception ex) {
            log.error("deleteEstateError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}
