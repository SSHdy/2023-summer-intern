package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.RepairRecordDetailDto;
import com.ryzw.housemanager.dto.RepairRemindDto;
import com.ryzw.housemanager.dto.RepairRemindListDto;
import com.ryzw.housemanager.entity.RepairRemind;
import com.ryzw.housemanager.entity.RepairDic;
import com.ryzw.housemanager.vo.RepairRemindAddVo;
import com.ryzw.housemanager.vo.RepairRemindVo;

import java.util.List;

/**
 * <p>
 * 维修提醒管理 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-02-27
 */
public interface IRepairRemindService extends IService<RepairRemind> {

    /**
     * 维修提醒列表
     *
     * @param repairRemindVo
     * @return
     */
    Page<RepairRemindListDto> selectRepairRemindList(RepairRemindVo repairRemindVo);

    /**
     * 查询相关维修记录
     *
     * @param repairRemindVo
     * @return
     */
    List<RepairRecordDetailDto> selectRepairRemindRecord(RepairRemindVo repairRemindVo);

    /**
     * 新增维修提醒
     *
     * @param repairRemindAddVo
     * @return
     */
    boolean addRepairRemind(RepairRemindAddVo repairRemindAddVo);

    /**
     * 查询维修提醒详情
     *
     * @param repairRemindId
     * @return
     */
    RepairRemindDto selectRepairRemindDetail(Long repairRemindId);

    /**
     * 批量删除维修提醒
     *
     * @param repairRemindIdList
     * @return
     */
    boolean batchDeleteRepairRemind(List<Long> repairRemindIdList);

    /**
     * 修改提醒天数
     *
     * @param repairDic
     * @return
     */
    boolean updateRemindDay(RepairDic repairDic);

    /**
     * 点击忽略（暂不提醒）
     *
     * @param repairRemindId
     * @return
     */
    boolean notRemind(Long repairRemindId);

    /**
     * 查询维修提醒的数量
     * @return
     */
    Integer repairRemindNum();
}
