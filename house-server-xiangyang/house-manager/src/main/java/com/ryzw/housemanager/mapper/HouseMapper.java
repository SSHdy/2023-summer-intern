package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.House;
import com.ryzw.housemanager.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 房屋信息表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2018-12-11
 */
public interface HouseMapper extends BaseMapper<House> {


    /**
     * 房屋使用详情
     *
     * @param houseDetailVo
     * @return
     */
    HouseDto houseDetail(@Param("House") HouseDetailVo houseDetailVo);

    /**
     * 楼层房屋列表
     *
     * @param floorHouseVo
     * @return
     */
    List<HouseDto> floorHouse(@Param("House") FloorHouseVo floorHouseVo);

    /**
     * 房屋信息列表
     *
     * @param page
     * @param houseInfoListVo
     * @return
     */
    Page<HouseInfoListDto> houseInfoList(Page page, @Param("House") HouseInfoListVo houseInfoListVo);


    /**
     * 房屋使用超标报表
     *
     * @param page
     * @param houseProofVo
     * @return
     */
    Page<HouseProofDto> houseProof(Page page, @Param("Ho") HouseProofVo houseProofVo, @Param("unitIds") List<Long> unitIds);


    /**
     * 查询无人办公室
     *
     * @param page
     * @param noOfficeVo
     * @return
     */
    Page<NoOfficeDto> selectNoOffice(Page page, @Param("NoOffice") NoOfficeVo noOfficeVo);


    /**
     * 单位占用汇总
     *
     * @param page
     * @param unitNameVo
     * @return
     */
    Page<UnitUsageDto> selectUnitUsage(Page<HouseInfoListDto> page, @Param("UnitName") UnitNameVo unitNameVo);


    /**
     * 闲置用房树查询
     *
     * @param spareTreeDto
     * @return
     */
    List<SpareRoomTreeDto> spareRoomTree(@Param("spareTreeDto") SpareTreeDto spareTreeDto);


    /**
     * 闲置用房查询
     *
     * @param page
     * @param spareRoomVo
     * @param unitIds
     * @return
     */
    Page<SpareRoomDto> spareRoom(Page page, @Param("spare") SpareRoomVo spareRoomVo, @Param("unitIds") List<Long> unitIds);

    /**
     * 计算闲置用房报表合计数据
     *
     * @param spareRoomVo
     * @param unitIds
     * @return
     */
    List<SpareRoomDto> spareRoom(@Param("spare") SpareRoomVo spareRoomVo, @Param("unitIds") List<Long> unitIds);

    /**
     * 租赁房屋报表查询
     *
     * @param page
     * @param rentRoomReportVo
     * @return
     */
    Page<RentRoomReportDto> rentRoomReport(Page page, @Param("rent") RentRoomReportVo rentRoomReportVo);

    /**
     * 租赁房屋报表查询合计数据
     *
     * @param rentRoomReportVo
     * @return
     */
    List<RentRoomReportDto> rentRoomReport(@Param("rent") RentRoomReportVo rentRoomReportVo);

    /**
     * 按房间编号批量删除所有房间关联关系
     *
     * @param houseIds
     * @return
     */
    int deleteByHouseId(@Param("houseIds") List<String> houseIds);

    /**
     * 查询房间明细
     *
     * @param page
     * @param roomDetailVo
     * @return
     */
    Page<RoomDetailDto> roomDetail(Page page, @Param("rd") RoomDetailVo roomDetailVo, @Param("unitIds") List<Long> unitIds);

    /**
     * 信息档案楼层树
     *
     * @param buildAndYardVo
     * @return
     */
    MessageFloorDto messageFloorTree(@Param("buildAndYardVo") BuildAndYardVo buildAndYardVo);


}
