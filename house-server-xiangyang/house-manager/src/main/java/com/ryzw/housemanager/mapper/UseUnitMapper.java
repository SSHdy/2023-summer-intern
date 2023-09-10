package com.ryzw.housemanager.mapper;

import com.ryzw.housemanager.entity.UseUnit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2019-02-28
 */
public interface UseUnitMapper extends BaseMapper<UseUnit> {

    /**
     * 查询房间使用单位
     * @param unitId
     * @return
     */
    int houseUseUnit(@Param("unitId")Long unitId);

    /**
     * 查询自用单位列表
     * @return
     */
    List<Long> useUnitIds();



}
