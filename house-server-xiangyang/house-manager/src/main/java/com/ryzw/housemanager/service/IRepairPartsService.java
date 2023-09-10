package com.ryzw.housemanager.service;

import com.ryzw.housemanager.entity.RepairParts;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.vo.BasicVo;

/**
 * <p>
 * 建筑物保养部位表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
public interface IRepairPartsService extends IService<RepairParts> {

    /**
     * 删除维修部位
     * @param basicVo
     * @return
     */
    boolean delete(BasicVo<Integer> basicVo);

}
