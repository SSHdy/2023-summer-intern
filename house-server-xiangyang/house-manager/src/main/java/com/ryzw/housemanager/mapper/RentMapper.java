package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.HouseColorDto;
import com.ryzw.housemanager.dto.RentDetailDto;
import com.ryzw.housemanager.dto.RentListDto;
import com.ryzw.housemanager.entity.Rent;
import com.ryzw.housemanager.vo.RentPageVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 租赁房间信息表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-03-13
 */
public interface RentMapper extends BaseMapper<Rent> {

    /**
     * 查询租赁详情信息
     *
     * @param rentId
     * @return
     */
    RentDetailDto rentDetail(@Param("rentId") Long rentId);

    /**
     * 租赁列表
     *
     * @param page
     * @param rentPageVo
     * @return
     */
    Page<RentListDto> getList(Page page, @Param("RentPageVo") RentPageVo rentPageVo);

    /**
     * 退租
     *
     * @param rentId
     * @return
     */
    int rentWithdrawal(@Param("rentId") Long rentId);

    /**
     * 出租提醒列表
     *
     * @param page
     * @param rentPageVo
     * @return
     */
    Page<RentListDto> rentRmindList(Page<RentListDto> page, @Param("RentPageVo") RentPageVo rentPageVo, @Param("rentRemindDay") String rentRemindDay);

    /**
     * 查询出租租金提醒数量
     *
     * @return
     */
    Integer rentRemindNum(@Param("rentRemindDay") String rentRemindDay);

    /**
     * 出租到期提醒列表
     *
     * @param page
     * @param rentPageVo
     * @param rentExpireDay
     * @return
     */
    Page<RentListDto> rentExpireList(Page<RentListDto> page, @Param("RentPageVo") RentPageVo rentPageVo, @Param("rentExpireDay") String rentExpireDay);

    /**
     * 查询租赁到期提醒数量
     *
     * @param rentExpireDay
     * @return
     */
    Integer rentExpireNum(@Param("rentExpireDay") String rentExpireDay);

    /**
     * 查询房间颜色对象集合
     *
     * @param houseIdList
     * @return
     */
    List<HouseColorDto> selectHouseList(@Param("houseIdList") List<String> houseIdList);

}
