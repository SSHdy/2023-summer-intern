package com.ryzw.housemanager.mapper;

import com.ryzw.housemanager.entity.RepairImg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 维修照片表 Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2019-02-28
 */
public interface RepairImgMapper extends BaseMapper<RepairImg> {


    /**
     * 通过repair查询所有相关的维修图片
     * @param repairModuleId
     * @return
     */
    List<RepairImg> selectByRepairId(Long repairModuleId);

}
