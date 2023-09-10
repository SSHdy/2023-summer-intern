package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.EquipmentDetailDto;
import com.ryzw.housemanager.entity.Equipment;
import com.ryzw.housemanager.entity.EquipmentUse;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.EquipmentMapper;
import com.ryzw.housemanager.mapper.EquipmentUseMapper;
import com.ryzw.housemanager.service.IEquipmentService;
import com.ryzw.housemanager.service.IEquipmentUseService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.EquipmentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备管理表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
@Service
@Slf4j
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements IEquipmentService {
    @Autowired
    EquipmentMapper equipmentMapper;
    @Autowired
    private IEquipmentUseService equipmentUseService;
    @Autowired
    private EquipmentUseMapper equipmentUseMapper;
    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    /**
     * 删除单个设备信息
     *
     * @param basicVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(BasicVo<Long> basicVo) {
        try {
            QueryWrapper<EquipmentUse> wrapper = new QueryWrapper<>();
            wrapper.eq("equipment_id", basicVo.getId());
            equipmentUseService.remove(wrapper);
            return this.removeById(basicVo.getId());
        } catch (Exception ex) {
            log.error("deleteError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 新增设备信息
     *
     * @param equipmentVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addEquipment(EquipmentVo equipmentVo) {
        try {
            Equipment equipment = dozerBeanMapper.map(equipmentVo, Equipment.class);
            equipmentMapper.insert(equipment);
//            this.saveOrUpdate(equipment);
            Long equipmentId = equipment.getEquipmentId();
            List<Long> buildIdList = equipmentVo.getBuildIdList();
            List<EquipmentUse> equipmentUseList = new ArrayList<>();
            for (Long a : buildIdList) {
                equipmentUseList.add(EquipmentUse.builder().buildId(a).equipmentId(equipmentId).build());
            }
            if (CollectionUtils.isNotEmpty(equipmentUseList)) {
                equipmentUseService.saveOrUpdateBatch(equipmentUseList);
            } else {
                throw new ServiceException("请填写重大设备使用关系!");
            }
            return true;
        } catch (Exception ex) {
            log.error("addEquipmentError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改设备信息
     *
     * @param equipmentVo
     * @return
     */
    @Override
    public boolean update(EquipmentVo equipmentVo) {
        try {
            if (equipmentVo.getEquipmentId() != null) {
                // 新传入的use数组
                List<EquipmentUse> equipmentUseList = new ArrayList<>();
                //查询旧的对应的use数组
                List<Long> deleteIds = equipmentUseService.list(new QueryWrapper<EquipmentUse>()
                        .eq("equipment_id", equipmentVo.getEquipmentId())).stream().map(EquipmentUse::getEquipmentuseId)
                        .collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(deleteIds)) {
                    equipmentUseMapper.deleteBatchIds(deleteIds);
                }
                Optional.ofNullable(equipmentVo.getBuildIdList()).ifPresent(list -> list.forEach(
                        m -> equipmentUseList.add(EquipmentUse.builder().equipmentId(equipmentVo.getEquipmentId()).buildId(m).build())
                ));
                if (CollectionUtils.isNotEmpty(equipmentUseList)) {
                    equipmentUseService.saveBatch(equipmentUseList);
                } else {
                    throw new ServiceException("请填写重大设备使用关系!");
                }
                Equipment equipment = dozerBeanMapper.map(equipmentVo, Equipment.class);
                equipmentMapper.updateById(equipment);
            }
            return true;
        } catch (Exception ex) {
            log.error("updateError", ex);
            throw new ServiceException(ex.getMessage());
        }

    }

    /**
     * 查询设备信息详情
     *
     * @param basicVo
     * @return
     */
    @Override
    public EquipmentDetailDto equipmentDetail(BasicVo<Long> basicVo) {
        return equipmentMapper.equipmentDetail(basicVo);
    }

    /**
     * 查询某个楼栋下的最后几条设备信息列表
     *
     * @param basicVo
     * @return
     */
    @Override
    public List<Equipment> equipmentLast(BasicVo<Long> basicVo) {
        return equipmentMapper.equipmentLast(basicVo);
    }
}
