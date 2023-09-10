package com.ryzw.housemanager.service;

import com.ryzw.housemanager.dto.RecordDto;
import com.ryzw.housemanager.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.vo.ExcessiveVo;

import java.util.List;

/**
 * <p>
 * 备案表 服务类
 * </p>
 *
 * @author wj
 * @since 2019-01-30
 */
public interface IRecordService extends IService<Record> {


    /**
     * 房屋超标信息查询
     * @param excessiveVo
     * @return
     */
    List<RecordDto> recordDetail(ExcessiveVo excessiveVo);
}
