package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.entity.PmChangeLog;
import com.ryzw.housemanager.entity.RepairPm;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.PmChangeLogMapper;
import com.ryzw.housemanager.mapper.RepairPmMapper;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IRepairPmService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 维修项目管理人表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
@Slf4j
@Service
public class RepairPmServiceImpl extends ServiceImpl<RepairPmMapper, RepairPm> implements IRepairPmService {
    @Autowired
    private PmChangeLogMapper pmChangeLogMapper;
    @Autowired
    private RepairPmMapper repairPmMapper;
    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Autowired
    IAuthorityUnitService authorityUnitService;

    @Override
    public boolean changeManage(PmChangeLog pmChangeLog) {
        try {
            Map<String, Object> maps = authorityUnitService.getUserByToken();
            if (MapUtils.isNotEmpty(maps)) {
//            更新人员管理表
                UpdateWrapper<RepairPm> wrapper = new UpdateWrapper<>();
                wrapper.eq("repair_apply_id", pmChangeLog.getRepairApplyId());
                wrapper.eq("repair_pm_userid", maps.get("user_id"));
                RepairPm repairPm = dozerBeanMapper.map(pmChangeLog, RepairPm.class);
                repairPm.setRepairPmUserid(pmChangeLog.getRepairNewUserid());
                repairPmMapper.update(repairPm, wrapper);
                pmChangeLog.setRepairUserid(Long.valueOf(String.valueOf(maps.get("user_id"))));
                pmChangeLog.setRepairUsername(String.valueOf(maps.get("username")));
                pmChangeLog.setRepairChangeTime(new Date());

//              添加或者更新日志信息
                pmChangeLogMapper.insert(pmChangeLog);
                return true;
            }
            throw new ServiceException("未查询到当前用户!");

        } catch (Exception ex) {
            log.error("changeManageError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}
