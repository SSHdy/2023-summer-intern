package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.RepairQualityDto;
import com.ryzw.housemanager.dto.RepairRecordReportDto;
import com.ryzw.housemanager.entity.RepairModule;
import com.ryzw.housemanager.vo.RepairQualityVo;
import com.ryzw.housemanager.vo.RepairRecordReportVo;

import java.util.List;

/**
 * <p>
 * 维修模块表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-03-28
 */
public interface IRepairModuleService extends IService<RepairModule> {

    /**
     * 质保期信息管理查询
     *
     * @param repairQualityVo
     * @return
     */
    Page<RepairQualityDto> repairQuality(RepairQualityVo repairQualityVo);

    /**
     * 维修报表查询
     *
     * @param repairRecordReportVo
     * @return
     */
    List<RepairRecordReportDto> repairRecordReport(RepairRecordReportVo repairRecordReportVo);

    /**
     * 维修模块位置列表
     *
     * @return
     */
    List<RepairModule> selectList();
}
