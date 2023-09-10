package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.RepairRecordDetailDto;
import com.ryzw.housemanager.dto.RepairRecordDto;
import com.ryzw.housemanager.dto.RepairRemindDto;
import com.ryzw.housemanager.dto.RepairRemindListDto;
import com.ryzw.housemanager.entity.RepairRemind;
import com.ryzw.housemanager.vo.RepairRemindVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 维修提醒管理 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-02-27
 */
public interface RepairRemindMapper extends BaseMapper<RepairRemind> {

    /**
     * 维修提醒列表
     *
     * @param repairRemind
     * @return
     */
    Page<RepairRemindListDto> selectRepairRemindList(Page<RepairRemindListDto> page, @Param("repairRemind") RepairRemindVo repairRemindVo);

    /**
     * 查询相关维修模块记录(最近一条)
     *
     * @param yardId
     * @param buildId
     * @param houseId
     * @param repairPart
     * @return
     */
    RepairRecordDto selectRepairRecord(@Param("yardId") Long yardId, @Param("buildId") Long buildId,
                                       @Param("houseId") String houseId, @Param("repairPart") Long repairPart);

    /**
     * 查询相关维修记录
     *
     * @param yardId
     * @param buildId
     * @param houseId
     * @param repairPart
     * @return
     */
    List<RepairRecordDetailDto> selectRepairRemindRecord(@Param("yardId") Long yardId, @Param("buildId") Long buildId,
                                                         @Param("houseId") String houseId, @Param("repairPart") Integer repairPart);

    /**
     * 查询维修提醒详情
     *
     * @param repairRemindId
     * @return
     */
    RepairRemindDto selectRepairRemindDetail(@Param("repairRemindId") Long repairRemindId);

    /**
     * 查询正在使用的维修部位维修提醒数量
     *
     * @param repairPartId
     * @return
     */
    Integer selectRemindCount(@Param("repairRemind") RepairRemindVo repairRemindVo);

    /**
     * 查询维修提醒的数量
     *
     * @param repairRemindVo
     * @return
     */
    Integer repairRemindNum(@Param("repairRemind") RepairRemindVo repairRemindVo);
}
