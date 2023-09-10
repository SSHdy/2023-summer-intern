package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.entity.Estate;
import com.ryzw.housemanager.vo.EstateVo;

/**
 * <p>
 * 物业信息表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-05-05
 */
public interface IEstateService extends IService<Estate> {

    /**
     * 查询物业信息列表
     *
     * @param estateVo
     * @return
     */
    Page<Estate> selectEstateList(EstateVo estateVo);

    /**
     * 删除物业信息
     *
     * @param estateId
     * @return
     */
    boolean deleteEstate(Long estateId);
}
