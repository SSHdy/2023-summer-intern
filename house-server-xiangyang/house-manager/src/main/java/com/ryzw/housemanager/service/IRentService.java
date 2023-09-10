package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.dto.RentDetailDto;
import com.ryzw.housemanager.dto.RentListDto;
import com.ryzw.housemanager.entity.Enclosure;
import com.ryzw.housemanager.entity.HousingUse;
import com.ryzw.housemanager.entity.Rent;
import com.ryzw.housemanager.vo.RentEnclosureVo;
import com.ryzw.housemanager.vo.RentPageVo;
import com.ryzw.housemanager.vo.RentWithdrawalVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 租赁房间信息表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-03-13
 */
public interface IRentService extends IService<Rent> {

    /**
     * 新增租赁信息,租赁附件信息，院落楼座关系
     *
     * @param rent
     * @param AddressList
     * @param housingUseList
     * @return
     */
    boolean addRent(Rent rent, List<Enclosure> AddressList, List<HousingUse> housingUseList, Integer state, List<Long> soilList);


    /**
     * 修改租赁信息,租赁附件信息,院落楼座关系,租赁关系
     *
     * @param rentEnclosureVo
     * @return
     */
    boolean updateRent(RentEnclosureVo rentEnclosureVo);

    /**
     * 租赁详情
     *
     * @param rentId
     * @return
     */
    RentDetailDto selectRentDetail(Long rentId);

    /**
     * 删除租赁信息
     *
     * @param rentId
     * @return
     */
    boolean deleteRent(Long rentId);

    /**
     * 房屋租赁信息列表
     *
     * @param rentPageVo
     * @return
     */
    Page<RentListDto> getlist(RentPageVo rentPageVo);

    /**
     * 查询租赁附件路径的集合
     *
     * @param enclosureIdList
     * @return
     */
    List<DownLoadDto> selectEnclosureUrl(List<Long> enclosureIdList);

    /**
     * 退租
     *
     * @param rentId
     * @return
     */
    int rentWithdrawal(Long rentId);

    /**
     * 退租时修改图形颜色,将出租改为闲置
     * @param houseIdList
     */
    void updateWithdrawalJsonPlan(List<String> houseIdList);

    /**
     * 查询租赁提醒数量
     *
     * @return
     */
    Integer rentRemindNum();

    /**
     * 查询提醒数量
     *
     * @return
     */
    Map<Object, Object> remindNum();

    /**
     * 出租提醒列表
     *
     * @param rentPageVo
     * @return
     */
    Page<RentListDto> rentRmindList(RentPageVo rentPageVo);

    /**
     * 出租到期提醒列表
     *
     * @param rentPageVo
     * @return
     */
    Page<RentListDto> rentExpireList(RentPageVo rentPageVo);
}
