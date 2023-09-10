package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.SpecialDetailDto;
import com.ryzw.housemanager.dto.SpecialListDto;
import com.ryzw.housemanager.entity.SpecialInspection;
import com.ryzw.housemanager.mapper.SpecialInspectionMapper;
import com.ryzw.housemanager.service.ISpecialInspectionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.SpecialInspectionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 专项巡检表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-10-08
 */
@Service
public class SpecialInspectionServiceImpl extends ServiceImpl<SpecialInspectionMapper, SpecialInspection> implements ISpecialInspectionService {

    @Autowired
    private SpecialInspectionMapper specialInspectionMapper;

    /**
     * 专项检查信息列表
     * @param specialInspectionVo
     * @return
     */
    @Override
    public Page<SpecialListDto> specialList(SpecialInspectionVo specialInspectionVo) {
        Page<SpecialListDto> page = new Page<>(specialInspectionVo.getCurrentPage(),specialInspectionVo.getPageSize());
        return specialInspectionMapper.specialList(page,specialInspectionVo);
    }

    /**
     * 专项检查详情
     * @param basicVo
     * @return
     */
    @Override
    public SpecialDetailDto specialDetail(BasicVo<Long> basicVo) {
        return specialInspectionMapper.specialDetail(basicVo);
    }
}
