package com.ryzw.housemanager.service.impl;

import com.ryzw.housemanager.dto.LeadingOfficeReportDto;
import com.ryzw.housemanager.entity.LeadingOffice;
import com.ryzw.housemanager.mapper.LeadingOfficeMapper;
import com.ryzw.housemanager.service.ILeadingOfficeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.vo.LeadingOfficeReportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyx
 * @since 2018-12-14
 */
@Service
public class LeadingOfficeServiceImpl extends ServiceImpl<LeadingOfficeMapper, LeadingOffice> implements ILeadingOfficeService {
    @Autowired
    private LeadingOfficeMapper leadingOfficeMapper;

    /**
     * 领导人员办公室报表信息查询
     * @param leadingOfficeReportVo
     * @return
     */
    @Override
    public List<LeadingOfficeReportDto> officeReport(LeadingOfficeReportVo leadingOfficeReportVo) {
        return leadingOfficeMapper.officeReport(leadingOfficeReportVo);
    }

    /**
     * 一般人员办公室报表信息查询
     * @param leadingOfficeReportVo
     * @return
     */
    @Override
    public List<LeadingOfficeReportDto> commonReport(LeadingOfficeReportVo leadingOfficeReportVo) {
        return leadingOfficeMapper.commonReport(leadingOfficeReportVo);
    }
}
