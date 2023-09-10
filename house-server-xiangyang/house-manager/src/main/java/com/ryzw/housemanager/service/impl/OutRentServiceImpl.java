package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.OutRentDto;
import com.ryzw.housemanager.entity.OutRent;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.OutRentMapper;
import com.ryzw.housemanager.service.IOutRentService;
import com.ryzw.housemanager.vo.OutRentVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 出借用房信息表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-05-05
 */
@Service
@Slf4j
public class OutRentServiceImpl extends ServiceImpl<OutRentMapper, OutRent> implements IOutRentService {

    @Autowired
    OutRentMapper outRentMapper;

    /**
     * 查询出租用房信息列表
     *
     * @param outRentVo
     * @return
     */
    @Override
    public Page<OutRentDto> selectOutRentList(OutRentVo outRentVo) {
        try {
            Page<OutRentDto> page = new Page<>(outRentVo.getCurrentPage(), outRentVo.getPageSize());
            return outRentMapper.selectOutRentList(page, outRentVo);
        } catch (Exception ex) {
            log.error("selectOutRentListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}
