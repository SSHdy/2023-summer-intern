package com.ryzw.housemanager.service.impl;

import com.ryzw.housemanager.entity.UseUnit;
import com.ryzw.housemanager.mapper.UseUnitMapper;
import com.ryzw.housemanager.service.IUseUnitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wj
 * @since 2019-02-28
 */
@Service
public class UseUnitServiceImpl extends ServiceImpl<UseUnitMapper, UseUnit> implements IUseUnitService {

    @Autowired
    private UseUnitMapper useUnitMapper;

    /**
     * 查询自用单位列表
     * @return
     */
    @Override
    public List<Long> useUnitIds() {
        return useUnitMapper.useUnitIds();
    }
}
