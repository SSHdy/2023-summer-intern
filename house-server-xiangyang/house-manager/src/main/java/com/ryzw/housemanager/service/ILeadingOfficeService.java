package com.ryzw.housemanager.service;

import com.ryzw.housemanager.dto.LeadingOfficeReportDto;
import com.ryzw.housemanager.entity.LeadingOffice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.vo.LeadingOfficeReportVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lyx
 * @since 2018-12-14
 */
public interface ILeadingOfficeService extends IService<LeadingOffice> {

    /**
     * 领导人员办公室报表信息查询
     * @param leadingOfficeReportVo
     * @return
     */
    List<LeadingOfficeReportDto> officeReport( LeadingOfficeReportVo leadingOfficeReportVo);

    /**
     * 一般人员办公室报表信息查询
     * @param leadingOfficeReportVo
     * @return
     */
    List<LeadingOfficeReportDto> commonReport( LeadingOfficeReportVo leadingOfficeReportVo);
}
