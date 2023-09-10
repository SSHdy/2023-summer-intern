package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.entity.Estate;
import com.ryzw.housemanager.vo.EstateVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 物业信息表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-05-05
 */
public interface EstateMapper extends BaseMapper<Estate> {

    /**
     * 查询物业信息列表
     *
     * @param page
     * @param estateVo
     * @return
     */
    Page<Estate> selectEstateList(Page<Estate> page, @Param("EstateVo") EstateVo estateVo);

    /**
     * 查询被关联的物业数
     *
     * @param estateId
     * @return
     */
    Integer selectCountEstate(@Param("estateId") Long estateId);
}
