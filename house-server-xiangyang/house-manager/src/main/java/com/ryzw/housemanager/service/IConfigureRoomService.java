package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.ConfigureRoom;
import com.ryzw.housemanager.vo.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 配置房间表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
public interface IConfigureRoomService extends IService<ConfigureRoom> {

    /**
     * 查询自动分房的筛选条件列表
     * @param basicVo
     * @return
     */
    List<AutoConditionDto> autoCondition(BasicVo<Long> basicVo);

    /**
     * 查询自动分房符合条件的房间
     * @param autoAllocationRoomVo
     * @return
     */
    Page<AutoAllocationRoomDto> autoAllocationRoom(AutoAllocationRoomVo autoAllocationRoomVo);

    /**
     * 查询人工分房，符合条件的房间
     * @param artificialAllocationVo
     * @return
     */
    Page<ArtificialAllocationRoomDto> artificialAllocation(ArtificialAllocationVo artificialAllocationVo);

    /**
     * 查询分配单位信息
     * @param allocationUnitVo
     * @return
     */
    Map<Object, Object> allocationUnitList(AllocationUnitVo allocationUnitVo);

    /**
     * 查询新单位信息
     * @param allocationUnitVo
     * @return
     */
    List<NewUnitDto> configNewUnit(AllocationUnitVo allocationUnitVo);

    /**
     * 查询一层楼的房间信息
     * @param floorHouseInfoVo
     * @return
     */
    List<FloorHouseInfoDto> floorHouseInfo(FloorHouseInfoVo floorHouseInfoVo);

    /**
     * 按照楼层查询人工分房的房间信息
     * @param allocationFloorVo
     * @return
     */
    List<AllocationFloorDto> allocationFloor(AllocationFloorVo allocationFloorVo);

    /**
     * 查询每一层楼的房间信息
     * @param floorHouseListVo
     * @return
     */
    List<String> floorHouse(FloorHouseListVo floorHouseListVo);

    /**
     * 判断某一楼栋下是否关联配置
     * @param floorHouseListVo
     * @return
     */
    boolean isUnionConfig(FloorHouseListVo floorHouseListVo);

}
