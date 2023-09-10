package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.OutRentDto;
import com.ryzw.housemanager.entity.OutRent;
import com.ryzw.housemanager.vo.OutRentVo;

/**
 * <p>
 * 出借用房信息表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-05-05
 */
public interface IOutRentService extends IService<OutRent> {

    /**
     * 查询出租用房信息列表
     *
     * @param outRentVo
     * @return
     */
    Page<OutRentDto> selectOutRentList(OutRentVo outRentVo);
}
