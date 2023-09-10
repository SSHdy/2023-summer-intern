package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.housemanager.entity.HousingUse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 院落楼座单位关系表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2018-12-11
 */
public interface HousingUseMapper extends BaseMapper<HousingUse> {

    /**
     * 查询出减少房间对应的最大的housingUseId
     *
     * @param delHouseIdList
     * @return
     */
    List<Long> getMaxHouseId(@Param("delHouseIdList") List<String> delHouseIdList);
}
