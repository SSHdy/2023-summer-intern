package com.ryzw.housemanager.mapper;

import com.ryzw.housemanager.entity.Purchasing;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 采购表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-10-17
 */
public interface PurchasingMapper extends BaseMapper<Purchasing> {

    /**
     * 查询未采购数量
     * @param id
     * @return
     */
    int selectNum(@Param("id") Long id);
}
