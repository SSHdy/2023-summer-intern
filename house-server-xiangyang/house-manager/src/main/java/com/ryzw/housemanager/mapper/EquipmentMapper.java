package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.housemanager.dto.EquipmentDetailDto;
import com.ryzw.housemanager.entity.Equipment;
import com.ryzw.housemanager.vo.BasicVo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 设备管理表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
public interface EquipmentMapper extends BaseMapper<Equipment> {
    /**
     * 查询设备信息详情
     * @param basicVo
     * @return
     */
    EquipmentDetailDto equipmentDetail(BasicVo<Long> basicVo);

    /**
     * 查询某个楼栋下的最后几条设备信息列表
     * @param basicVo
     * @return
     */
    List<Equipment> equipmentLast(BasicVo<Long> basicVo);
}
