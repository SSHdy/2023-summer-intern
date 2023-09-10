package com.ryzw.housemanager.service;

import com.ryzw.housemanager.entity.UseUnit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wj
 * @since 2019-02-28
 */
public interface IUseUnitService extends IService<UseUnit> {

    /**
     * 查询自用单位列表
     * @return
     */
    List<Long> useUnitIds();
}
