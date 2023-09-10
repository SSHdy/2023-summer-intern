package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.RepairQualityDto;
import com.ryzw.housemanager.dto.RepairRecordReportDto;
import com.ryzw.housemanager.entity.RepairModule;
import com.ryzw.housemanager.vo.RepairQualityVo;
import com.ryzw.housemanager.vo.RepairRecordReportVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 维修模块表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-03-28
 */
public interface RepairModuleMapper extends BaseMapper<RepairModule> {

    /**
     * 质保期管理查询
     *
     * @param repairQualityVo
     * @return
     */
    Page<RepairQualityDto> repairQuality(Page page, @Param("Quality") RepairQualityVo repairQualityVo);

    /**
     * 维修报表查询
     *
     * @param repairRecordReportVo
     * @return
     */
    List<RepairRecordReportDto> repairRecordReport(@Param("RepairRecordReportVo") RepairRecordReportVo repairRecordReportVo);

    /**
     * 维修模块位置列表
     *
     * @return
     */
    List<RepairModule> getList();
}
