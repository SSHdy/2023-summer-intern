package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.EquipmentDetailDto;
import com.ryzw.housemanager.entity.Equipment;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.EquipmentVo;

import java.util.List;

/**
 * <p>
 * 设备管理表 服务类
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
public interface IEquipmentService extends IService<Equipment> {

    /**
     * 删除单个设备信息
     *
     * @param
     */
    boolean delete(BasicVo<Long> basicVo);

    /**
     * 新增设备信息
     *
     * @param equipmentVo
     * @return
     */
    boolean addEquipment(EquipmentVo equipmentVo);

    /**
     * 修改设备信息
     *
     * @param equipmentVo
     * @return
     */
    boolean update(EquipmentVo equipmentVo);

    /**
     * 设备信息详情
     *
     * @param basicVo
     * @return
     */
    EquipmentDetailDto equipmentDetail(BasicVo<Long> basicVo);

    /**
     * 查询某个楼栋下的最后几条设备信息列表
     *
     * @param basicVo
     * @return
     */
    List<Equipment> equipmentLast(BasicVo<Long> basicVo);
}
