package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.RepairQualityDto;
import com.ryzw.housemanager.dto.RepairRecordReportDto;
import com.ryzw.housemanager.entity.RepairModule;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.RepairModuleMapper;
import com.ryzw.housemanager.service.IRepairModuleService;
import com.ryzw.housemanager.vo.RepairQualityVo;
import com.ryzw.housemanager.vo.RepairRecordReportVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 维修模块表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-03-28
 */
@Service
@Slf4j
public class RepairModuleServiceImpl extends ServiceImpl<RepairModuleMapper, RepairModule> implements IRepairModuleService {
    @Autowired
    private RepairModuleMapper repairModuleMapper;

    /**
     * 质保期信息管理查询
     *
     * @param repairQualityVo
     * @return
     */
    @Override
    public Page<RepairQualityDto> repairQuality(RepairQualityVo repairQualityVo) {
        try {
            Page<RepairQualityDto> page = new Page<>(repairQualityVo.getCurrentPage(), repairQualityVo.getPageSize());
            return repairModuleMapper.repairQuality(page, repairQualityVo);
        } catch (Exception ex) {
            log.error("repairQualityError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 维修报表查询
     *
     * @param repairRecordReportVo
     * @return
     */
    @Override
    public List<RepairRecordReportDto> repairRecordReport(RepairRecordReportVo repairRecordReportVo) {
        try {
            return repairModuleMapper.repairRecordReport(repairRecordReportVo);
        } catch (Exception ex) {
            log.error("repairRecordReportError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 维修模块位置列表
     *
     * @return
     */
    @Override
    public List<RepairModule> selectList() {
        try {
            return repairModuleMapper.getList();
        } catch (Exception ex) {
            log.error("selectListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}
