package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.housemanager.entity.RepairParts;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 建筑物保养部位表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
public interface RepairPartsMapper extends BaseMapper<RepairParts> {

    /**
     * 计算维修申请使用的维修部位数量
     *
     * @param repairPartId
     * @return
     */
    Integer selectApplyParts(@Param("repairPartId") Integer repairPartId);
}
