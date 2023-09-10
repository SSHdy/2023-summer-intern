package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.ConfigureRoom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.housemanager.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 配置房间表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
public interface ConfigureRoomMapper extends BaseMapper<ConfigureRoom> {

    /**
     * 查询自动分房的筛选条件列表
     * @return
     */
    List<AutoConditionDto> autoCondition(@Param("con")BasicVo<Long> basicVo);

    /**
     * 查询自动分房符合条件的房间
     * @param autoAllocationRoomVo
     * @return
     */
    Page<AutoAllocationRoomDto> autoAllocationRoom(Page page, @Param("auto") AutoAllocationRoomVo autoAllocationRoomVo,@Param("houseIds") List<String> houseIds);

    /**
     * 查询人工分房，符合条件的房间
     * @param page
     * @param artificialAllocationVo
     * @return
     */
    Page<ArtificialAllocationRoomDto> artificialAllocation(Page page, @Param("plan")ArtificialAllocationVo artificialAllocationVo);

    /**
     * 查询分配单位信息
     * @param allocationUnitVo
     * @return
     */
    List<AllocationUnitDto> allocationUnit(@Param("cp")AllocationUnitVo allocationUnitVo);

    /**
     * 查询分配单位配置相关的信息
     * @param allocationUnitVo
     * @return
     */
    List<AllocationUnitDto> allocationConf(@Param("cp")AllocationUnitVo allocationUnitVo);

    /**
     * 查询配置相关的原单位信息
     * @param allocationUnitVo
     * @return
     */
    List<OldUnitDto> configOldUnit(@Param("old")AllocationUnitVo allocationUnitVo);

    /**
     * 查询配置相关的新单位信息
     * @param allocationUnitVo
     * @return
     */
    List<NewUnitDto> configNewUnit(@Param("ne")AllocationUnitVo allocationUnitVo);

    /**
     * 查询一层楼的房间信息
     * @param floorHouseInfoVo
     * @return
     */
    List<FloorHouseInfoDto> floorHouseInfo(@Param("floor")FloorHouseInfoVo floorHouseInfoVo);

    /**
     * 按照楼层查询人工分房的房间信息
     * @param allocationFloorVo
     * @return
     */
    List<AllocationFloorDto> allocationFloor(@Param("floor")AllocationFloorVo allocationFloorVo);

    /**
     * 查询每一层楼的房间信息
     * @param floorHouseListVo
     * @return
     */
    List<String> floorHouse(@Param("floor")FloorHouseListVo floorHouseListVo);

}
