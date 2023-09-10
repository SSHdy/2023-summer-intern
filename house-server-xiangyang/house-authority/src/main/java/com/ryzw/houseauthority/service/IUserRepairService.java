package com.ryzw.houseauthority.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.houseauthority.entity.UserRepair;

import java.util.Set;

/**
 * <p>
 * 用户维修权限表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-04-15
 */
public interface IUserRepairService extends IService<UserRepair> {

    /**
     * 根据权限Id查询对应的用户
     *
     * @param userId
     * @return
     */
    Set selectRepairList(Long repairId);

    /**
     * 删除用户
     *
     * @param userId
     * @return
     */
    boolean deleteRepairById(Long userId);
}
