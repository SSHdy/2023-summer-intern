package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.RepairRecordDetailDto;
import com.ryzw.housemanager.dto.RepairRecordDto;
import com.ryzw.housemanager.dto.RepairRemindDto;
import com.ryzw.housemanager.dto.RepairRemindListDto;
import com.ryzw.housemanager.entity.RepairDic;
import com.ryzw.housemanager.entity.RepairRemind;
import com.ryzw.housemanager.entity.RepairRemindUnit;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.RepairDicMapper;
import com.ryzw.housemanager.mapper.RepairRemindMapper;
import com.ryzw.housemanager.mapper.RepairRemindUnitMapper;
import com.ryzw.housemanager.service.IRepairRemindService;
import com.ryzw.housemanager.vo.RepairRemindAddVo;
import com.ryzw.housemanager.vo.RepairRemindVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 维修提醒管理 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-02-27
 */
@Service
@Slf4j
public class RepairRemindServiceImpl extends ServiceImpl<RepairRemindMapper, RepairRemind> implements IRepairRemindService {

    @Autowired
    RepairRemindMapper repairRemindMapper;

    @Autowired
    RepairRemindUnitMapper repairRemindUnitMapper;

    @Autowired
    RepairDicMapper repairDicMapper;

    /**
     * 维修提醒列表
     *
     * @param repairRemindVo
     * @return
     */
    @Override
    public Page<RepairRemindListDto> selectRepairRemindList(RepairRemindVo repairRemindVo) {
        try {
            RepairDic repairDic = repairDicMapper.selectOne(new QueryWrapper<RepairDic>().eq("repair_dic_key", "repair_dic_days"));
            //获取提醒天数
            String repairRemindDay = repairDic.getRepairDicValue();
            repairRemindVo.setRepairRemindDay(Integer.valueOf(repairRemindDay));
            Page<RepairRemindListDto> page = new Page<>(repairRemindVo.getCurrentPage(), repairRemindVo.getPageSize());
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            String format = simpleDateFormat1.format(new Date());
            Date parse = simpleDateFormat1.parse(format);
            repairRemindVo.setCurrDate(parse);
            //计算开始提醒时间
            Calendar now = Calendar.getInstance();
            now.add(Calendar.DATE, -Integer.valueOf(repairRemindDay));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
            String endDate = simpleDateFormat.format(now.getTime());
            Date endTime = simpleDateFormat.parse(endDate);
            repairRemindVo.setEndDate(endTime);
            Page<RepairRemindListDto> repairRemindListDtoPage = repairRemindMapper.selectRepairRemindList(page, repairRemindVo);
            List<RepairRemindListDto> records = repairRemindListDtoPage.getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                for (RepairRemindListDto repairRemindListDto : records) {
                    Long yardId = repairRemindListDto.getYardId();
                    Long buildId = repairRemindListDto.getBuildId();
                    String houseId = repairRemindListDto.getHouseId();
                    Long repairPart = repairRemindListDto.getRepairPart();
                    //查询相关维修模块记录（到最近的一次）
                    RepairRecordDto repairRecordDto = repairRemindMapper.selectRepairRecord(yardId, buildId, houseId, repairPart);
                    if (repairRecordDto != null) {
                        Date maxTime = repairRecordDto.getMaxTime();
                        repairRemindListDto.setLastRepairTime(maxTime);
                    }
                }
            }
            return repairRemindListDtoPage;
        } catch (Exception ex) {
            log.error("selectRepairRemindListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询相关维修记录
     *
     * @param repairRemindVo
     * @return
     */
    @Override
    public List<RepairRecordDetailDto> selectRepairRemindRecord(RepairRemindVo repairRemindVo) {
        try {
            Long buildId = repairRemindVo.getBuildId();
            Long yardId = repairRemindVo.getYardId();
            Integer repairPart = repairRemindVo.getRepairPart();
            String houseId = repairRemindVo.getHouseId();
            List<RepairRecordDetailDto> repairRecordDetailDto = repairRemindMapper.selectRepairRemindRecord(yardId, buildId, houseId, repairPart);
            return repairRecordDetailDto;
        } catch (Exception ex) {
            log.error("selectRepairRemindRecordError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 新增维修提醒
     *
     * @param repairRemindAddVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRepairRemind(RepairRemindAddVo repairRemindAddVo) {
        try {
            RepairRemind repairRemind = repairRemindAddVo.getRepairRemind();
            repairRemind.setRemaindHandle(1);
            //新增维修提醒
            repairRemindMapper.insert(repairRemind);
            Long repairRemindId = repairRemind.getRepairRemind();
            List<Long> unitList = repairRemindAddVo.getUnitList();
            if (CollectionUtils.isNotEmpty(unitList)) {
                for (Long unitId : unitList) {
                    RepairRemindUnit repairRemindUnit = new RepairRemindUnit();
                    repairRemindUnit.setRepairRemindId(repairRemindId);
                    repairRemindUnit.setUnitId(unitId);
                    //新增维修提醒单位
                    repairRemindUnitMapper.insert(repairRemindUnit);
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("addRepairRemindError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询维修提醒详情
     *
     * @param repairRemindId
     * @return
     */
    @Override
    public RepairRemindDto selectRepairRemindDetail(Long repairRemindId) {
        try {
            return repairRemindMapper.selectRepairRemindDetail(repairRemindId);
        } catch (Exception ex) {
            log.error("selectRepairRemindDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 批量删除维修提醒
     *
     * @param repairRemindIdList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteRepairRemind(List<Long> repairRemindIdList) {
        try {
            repairRemindMapper.deleteBatchIds(repairRemindIdList);
            if (CollectionUtils.isNotEmpty(repairRemindIdList)) {
                for (Long repairRemindId : repairRemindIdList) {
                    repairRemindUnitMapper.delete(new QueryWrapper<RepairRemindUnit>().eq("repair_remind_id", repairRemindId));
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("batchDeleteRepairRemindError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改提醒天数
     *
     * @param repairDic
     * @return
     */
    @Override
    public boolean updateRemindDay(RepairDic repairDic) {
        try {
            repairDicMapper.update(repairDic, new QueryWrapper<RepairDic>().eq("repair_dic_key", "repair_dic_days"));
            return true;
        } catch (Exception ex) {
            log.error("updateRemindDayError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 点击忽略（暂不提醒）
     *
     * @param repairRemindId
     * @return
     */
    @Override
    public boolean notRemind(Long repairRemindId) {
        try {
            RepairRemind repairRemind = new RepairRemind();
            repairRemind.setRepairRemind(repairRemindId);
            repairRemind.setRemaindHandle(2);
            repairRemindMapper.updateById(repairRemind);
            return true;
        } catch (Exception ex) {
            log.error("notRemindError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 查询维修提醒的数量
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer repairRemindNum() {
        try {
            RepairDic repairDic = repairDicMapper.selectOne(new QueryWrapper<RepairDic>().eq("repair_dic_key", "repair_dic_days"));
            String repairRemindDay = repairDic.getRepairDicValue();
            RepairRemindVo repairRemindVo = new RepairRemindVo();
            repairRemindVo.setRepairRemindDay(Integer.valueOf(repairRemindDay));
            return repairRemindMapper.repairRemindNum(repairRemindVo);
        } catch (Exception ex) {
            log.error("repair/repairRemindNum error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}