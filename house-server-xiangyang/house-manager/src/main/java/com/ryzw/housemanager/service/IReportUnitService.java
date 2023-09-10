package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.BusinessHouseDto;
import com.ryzw.housemanager.dto.ConstructHouseDto;
import com.ryzw.housemanager.dto.ReportUnitDto;
import com.ryzw.housemanager.dto.YardUnitProfessionalDto;
import com.ryzw.housemanager.entity.ReportUnit;
import com.ryzw.housemanager.vo.DistInformationVo;
import com.ryzw.housemanager.vo.ReportUnitVo;
import com.ryzw.housemanager.vo.UploadDataVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 单位上报表 服务类
 * </p>
 *
 * @author yz
 * @since 2019-08-29
 */
public interface IReportUnitService extends IService<ReportUnit> {

    /**
     * 查询单位上报列表
     *
     * @param reportUnitVo
     * @return
     */
    Page<ReportUnitDto> reportUnitList(ReportUnitVo reportUnitVo);

    /**
     * 业务用房列表
     *
     * @param unitIds
     * @return
     */
    List<BusinessHouseDto> businessHousing(List<Long> unitIds);

    /**
     * 建设项目列表
     *
     * @return
     */
    List<ConstructHouseDto> constructHouseList(List<Long> unitIds);

    /**
     * 副省级城市数据上报信息导出
     *
     * @param response
     * @return
     */
    boolean exportDataExcel(UploadDataVo uploadDataVo, HttpServletResponse response);

    /**
     * 查询所有单位集合
     *
     * @param reportUnitVo
     * @return
     */
    List<ReportUnitDto> unitList(ReportUnitVo reportUnitVo);

}
