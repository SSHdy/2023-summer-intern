package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.housemanager.entity.FloorImg;

import java.util.List;

/**
 * <p>
 * 楼座照片表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2018-11-23
 */
public interface FloorImgMapper extends BaseMapper<FloorImg> {

    /**
     * 通过buildId查询所有相关的楼座图片
     */
    List<FloorImg> selectByBuildId(Long buildId);
}
