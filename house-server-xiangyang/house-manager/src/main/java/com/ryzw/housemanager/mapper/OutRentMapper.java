package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.OutRentDto;
import com.ryzw.housemanager.entity.OutRent;
import com.ryzw.housemanager.vo.OutRentVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 出借用房信息表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-05-05
 */
public interface OutRentMapper extends BaseMapper<OutRent> {

    /**
     * 查询出租用房信息列表
     *
     * @param page
     * @param outRentVo
     * @return
     */
    Page<OutRentDto> selectOutRentList(Page<OutRentDto> page, @Param("OutRentVo") OutRentVo outRentVo);
}
