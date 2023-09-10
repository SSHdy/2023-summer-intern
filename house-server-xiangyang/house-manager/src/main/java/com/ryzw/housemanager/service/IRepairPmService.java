package com.ryzw.housemanager.service;

import com.ryzw.housemanager.entity.PmChangeLog;
import com.ryzw.housemanager.entity.RepairPm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 维修项目管理人表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
public interface IRepairPmService extends IService<RepairPm> {
    boolean changeManage(PmChangeLog pmChangeLog);

}
