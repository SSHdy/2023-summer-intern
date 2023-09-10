package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.SpecialDetailDto;
import com.ryzw.housemanager.dto.SpecialListDto;
import com.ryzw.housemanager.entity.SpecialInspection;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.SpecialInspectionVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 专项巡检表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-10-08
 */
public interface SpecialInspectionMapper extends BaseMapper<SpecialInspection> {

    /**
     * 专项检查信息列表
     * @param page
     * @param specialInspectionVo
     * @return
     */
    Page<SpecialListDto> specialList(Page page, @Param("inspection") SpecialInspectionVo specialInspectionVo);

    /**
     * 专项检查详情
     * @param basicVo
     * @return
     */
    SpecialDetailDto specialDetail(@Param("id") BasicVo<Long> basicVo);
}
