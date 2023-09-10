package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryzw.housemanager.dto.LeadingOfficeReportDto;
import com.ryzw.housemanager.entity.LeadingOffice;
import com.ryzw.housemanager.vo.LeadingOfficeReportVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyx
 * @since 2018-12-14
 */
public interface LeadingOfficeMapper extends BaseMapper<LeadingOffice> {

    /**
     * 领导人员办公室报表信息查询
     * @param leadingOfficeReportVo
     * @return
     */
    List<LeadingOfficeReportDto> officeReport(@Param("Report") LeadingOfficeReportVo leadingOfficeReportVo);

    /**
     * 一般人员办公室报表信息查询
     * @param leadingOfficeReportVo
     * @return
     */
    List<LeadingOfficeReportDto> commonReport(@Param("Report") LeadingOfficeReportVo leadingOfficeReportVo);
}
