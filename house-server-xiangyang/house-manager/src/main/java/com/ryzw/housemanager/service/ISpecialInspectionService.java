package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.SpecialDetailDto;
import com.ryzw.housemanager.dto.SpecialListDto;
import com.ryzw.housemanager.entity.SpecialInspection;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.SpecialInspectionVo;

/**
 * <p>
 * 专项巡检表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-10-08
 */
public interface ISpecialInspectionService extends IService<SpecialInspection> {

    /**
     * 专项检查信息列表
     *
     * @param specialInspectionVo
     * @return
     */
    Page<SpecialListDto> specialList( SpecialInspectionVo specialInspectionVo);

    /**
     * 专项检查详情
     * @param basicVo
     * @return
     */
    SpecialDetailDto specialDetail(BasicVo<Long> basicVo);
}
