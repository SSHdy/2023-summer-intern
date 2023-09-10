package com.ryzw.housemanager.service.impl;

import com.ryzw.housemanager.dto.RecordDto;
import com.ryzw.housemanager.entity.Record;
import com.ryzw.housemanager.mapper.RecordMapper;
import com.ryzw.housemanager.service.IRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.vo.ExcessiveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 备案表 服务实现类
 * </p>
 *
 * @author wj
 * @since 2019-01-30
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements IRecordService {
    @Autowired
    RecordMapper recordMapper;

    /**
     * 房屋超标信息查询
     * @param excessiveVo
     * @return
     */
    @Override
    public List<RecordDto> recordDetail(ExcessiveVo excessiveVo) {
        return recordMapper.recordDetail(excessiveVo);
    }
}
