package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.House;
import com.ryzw.housemanager.vo.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 房屋信息表 服务类
 * </p>
 *
 * @author mfl
 * @since 2018-12-11
 */
public interface IHouseService extends IService<House> {
    /**
     * 新增UUID，修改房屋信息，单位关系，职称关系，删除UUID
     *
     * @param addHouseIdList
     * @param houseVoList
     * @param delHouseIdList
     * @return
     */
    boolean addUpdateDeleteHouseAndPlan(Long yardId, Long buildId, Integer floorName, String recordHolder, Long recordHolderId, List<String> addHouseIdList, List<HouseVo> houseVoList, List<String> delHouseIdList, List<Long> configureIdList, List<String> rentHouseIds, JsonPlanVo jsonPlanVo);

    /**
     * 房屋使用详情
     *
     * @param houseDetailVo
     * @return
     */
    HouseDto houseDetail(HouseDetailVo houseDetailVo);

    /**
     * 楼层房屋列表
     *
     * @param
     * @return
     */
    List<HouseDto> floorHouse(FloorHouseVo floorHouseVo);

    /**
     * 查询json
     *
     * @param jsonPlanVo
     * @return
     */
    Map<String, Object> selectJsonPlan(JsonPlanVo jsonPlanVo);

    /**
     * 房屋信息查询
     *
     * @param houseInfoListVo
     * @return
     */
    Page<HouseInfoListDto> houseInfoList(HouseInfoListVo houseInfoListVo);

    /**
     * 房屋使用超标报表
     *
     * @param houseProofVo
     * @return
     */
    Page<HouseProofDto> houseProof(HouseProofVo houseProofVo);

    /**
     * 查询无人办公室
     *
     * @param noOfficeVo
     * @return
     */
    Page<NoOfficeDto> selectNoOffice(NoOfficeVo noOfficeVo);

    /**
     * 单位占用汇总
     *
     * @param unitNameVo
     * @return
     */
    Page<UnitUsageDto> selectUnitUsage(UnitNameVo unitNameVo);

    /**
     * 闲置用房树查询
     *
     * @return
     */
    List<SpareRoomTreeDto> spareRoomTree(SpareTreeDto spareTreeDto);

    /**
     * 闲置用房查询
     *
     * @return
     */
    SpareRoomAndSumDto spareRoom(SpareRoomVo spareRoomVo, List<Long> unitIds);

    /**
     * 租赁房屋报表查询
     *
     * @param rentRoomReportVo
     * @return
     */
    RentRoomAndSumDto rentRoomReport(RentRoomReportVo rentRoomReportVo);

    /**
     * 查询房间明细
     *
     * @param roomDetailVo
     * @return
     */
    Page<RoomDetailDto> roomDetail(RoomDetailVo roomDetailVo);

    /**
     * 信息档案楼层树
     *
     * @param buildAndYardVo
     * @return
     */
    MessageFloorDto messageFloorTree(BuildAndYardVo buildAndYardVo);


}
