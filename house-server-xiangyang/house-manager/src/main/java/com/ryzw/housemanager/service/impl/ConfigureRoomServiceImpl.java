package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.ConfigurePlanData;
import com.ryzw.housemanager.entity.ConfigureRoom;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.ConfigureRoomMapper;
import com.ryzw.housemanager.service.IConfigurePlanDataService;
import com.ryzw.housemanager.service.IConfigureRoomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 配置房间表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
@Service
@Slf4j
public class ConfigureRoomServiceImpl extends ServiceImpl<ConfigureRoomMapper, ConfigureRoom> implements IConfigureRoomService {

    @Autowired
    private ConfigureRoomMapper configureRoomMapper;
    @Autowired
    private IConfigurePlanDataService configurePlanDataService;

    /**
     * 查询自动分房的筛选条件列表
     * @param basicVo
     * @return
     */
    @Override
    public List<AutoConditionDto> autoCondition(BasicVo<Long> basicVo) {
        return configureRoomMapper.autoCondition(basicVo);
    }

    /**
     * 查询自动分房符合条件的房间
     * @param autoAllocationRoomVo
     * @return
     */
    @Override
    public Page<AutoAllocationRoomDto> autoAllocationRoom(AutoAllocationRoomVo autoAllocationRoomVo) {
        QueryWrapper<ConfigurePlanData> wrapper = new QueryWrapper<>();
        wrapper.select("house_id");
        wrapper.eq("plan_type",1);
        List<ConfigurePlanData> houseIdList = configurePlanDataService.list(wrapper);
        List<String> houseIds = new ArrayList<>();
        for (ConfigurePlanData houseId:houseIdList ){
            houseIds.add(houseId.getHouseId());
        }
        Page<AutoAllocationRoomDto> page = new Page<>(autoAllocationRoomVo.getCurrentPage(), autoAllocationRoomVo.getPageSize());
        return configureRoomMapper.autoAllocationRoom(page,autoAllocationRoomVo,houseIds);
    }

    /**
     * 查询人工分房，符合条件的房间
     * @param artificialAllocationVo
     * @return
     */
    @Override
    public Page<ArtificialAllocationRoomDto> artificialAllocation(ArtificialAllocationVo artificialAllocationVo) {
        Page<ArtificialAllocationRoomDto> page = new Page<>(artificialAllocationVo.getCurrentPage(),artificialAllocationVo.getPageSize());
        return configureRoomMapper.artificialAllocation(page,artificialAllocationVo);
    }

    /**
     * 查询分配单位信息
     * @param allocationUnitVo
     * @return
     */
    @Override
    public Map<Object, Object> allocationUnitList(AllocationUnitVo allocationUnitVo) {
        try {
            HashMap<Object, Object> hashMap = new HashMap<>();
            List<AllocationUnitDto> allocationUnit = configureRoomMapper.allocationUnit(allocationUnitVo);
            List<AllocationUnitDto> allocationConf = configureRoomMapper.allocationConf(allocationUnitVo);
            List<OldUnitDto> oldUnitList = configureRoomMapper.configOldUnit(allocationUnitVo);
            List<NewUnitDto> newUnitList =  configureRoomMapper.configNewUnit(allocationUnitVo);

            //把数据添加到dto
            hashMap.put("allocationUnit",allocationUnit);
            hashMap.put("allocationConf",allocationConf);
            hashMap.put("oldUnitList", oldUnitList);
            hashMap.put("newUnitList", newUnitList);
            return hashMap;
        }catch (Exception ex){
            log.error("configure/autoAllocationRoom error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询新单位信息
     * @param allocationUnitVo
     * @return
     */
    @Override
    public List<NewUnitDto> configNewUnit(AllocationUnitVo allocationUnitVo) {
        return configureRoomMapper.configNewUnit(allocationUnitVo);
    }

    /**
     * 查询一层楼的房间信息
     * @param floorHouseInfoVo
     * @return
     */
    @Override
    public List<FloorHouseInfoDto> floorHouseInfo(FloorHouseInfoVo floorHouseInfoVo) {
        return configureRoomMapper.floorHouseInfo(floorHouseInfoVo);
    }

    /**
     * 按照楼层查询人工分房的房间信息
     * @param allocationFloorVo
     * @return
     */
    @Override
    public List<AllocationFloorDto> allocationFloor(AllocationFloorVo allocationFloorVo) {
        return configureRoomMapper.allocationFloor(allocationFloorVo);
    }

    /**
     * 查询每一层楼的房间信息
     * @param floorHouseListVo
     * @return
     */
    @Override
    public List<String> floorHouse(FloorHouseListVo floorHouseListVo) {
        return configureRoomMapper.floorHouse(floorHouseListVo);
    }

    /**
     * 判断某一楼栋下是否关联配置
     * @param floorHouseListVo
     * @return
     */
    @Override
    public boolean isUnionConfig(FloorHouseListVo floorHouseListVo) {
        List<String> houseids = configureRoomMapper.floorHouse(floorHouseListVo);
        if (CollectionUtils.isNotEmpty(houseids)){
            return true;
        }else {
            return false;
        }
    }
}
