package com.ryzw.housemanager.mapper;

import com.ryzw.housemanager.dto.RecordDto;
import com.ryzw.housemanager.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.housemanager.vo.ExcessiveVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 备案表 Mapper 接口
 * </p>
 *
 * @author wj
 * @since 2019-01-30
 */
public interface RecordMapper extends BaseMapper<Record> {

    /**
     * 房屋超标信息查询
     * @param excessiveVo
     * @return
     */
    List<RecordDto> recordDetail(@Param("record")ExcessiveVo excessiveVo);
}
