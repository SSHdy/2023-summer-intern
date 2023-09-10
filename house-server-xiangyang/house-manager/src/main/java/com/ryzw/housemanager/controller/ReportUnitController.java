package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.ReportEnclosure;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IReportEnclosureService;
import com.ryzw.housemanager.service.IReportUnitService;
import com.ryzw.housemanager.upload.FileOperateUtils;
import com.ryzw.housemanager.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 单位上报表 前端控制器
 * </p>
 *
 * @author zf
 * @since 2019-08-29
 */
@RestController
@Api(value = "单位上报管理", description = "单位上报管理")
@RequestMapping("/reportUnit")
public class ReportUnitController {

    @Autowired
    private IReportUnitService reportUnitService;

    @Autowired
    private IReportEnclosureService reportEnclosureService;

    @Autowired
    private IAuthorityUnitService authorityUnitService;

    /**
     * 查询单位上报列表
     *
     * @param reportUnitVo
     * @return
     */
    @RequestMapping(value = "/reportUnitList", method = RequestMethod.POST)
    @ApiOperation(value = "查询单位上报列表", notes = "查询单位上报列表")
    public Page<ReportUnitDto> reportUnitList(@RequestBody @Valid ReportUnitVo reportUnitVo) {

        return reportUnitService.reportUnitList(reportUnitVo);
    }

    /**
     * 查询所有单位集合
     *
     * @param reportUnitVo
     * @return
     */
    @RequestMapping(value = "/unitList", method = RequestMethod.POST)
    @ApiOperation(value = "查询所有单位集合", notes = "查询所有单位集合")
    public List<ReportUnitDto> unitList(@RequestBody ReportUnitVo reportUnitVo) {

        return reportUnitService.unitList(reportUnitVo);
    }


    /**
     * 单位上报
     *
     * @param reportEnclosure
     * @return
     */
    @RequestMapping(value = "/reportUnit", method = RequestMethod.POST)
    @ApiOperation(value = "单位上报", notes = "单位上报")
    public boolean reportUnit(@RequestBody @Valid ReportEnclosure reportEnclosure) {

        return reportEnclosureService.reportUnit(reportEnclosure);
    }


    /**
     * 单位上报文件下载
     *
     * @param downLoadFiletVo
     * @param response
     */
    @RequestMapping(value = "/downLoad", method = RequestMethod.POST)
    @ApiOperation(value = "单位上报文件下载", notes = "单位上报文件下载")
    public Object download(@RequestBody DownLoadFiletVo downLoadFiletVo, HttpServletResponse response) {
        List<AdressAndNameVo> addressList = downLoadFiletVo.getAddressList();
        if (CollectionUtils.isEmpty(addressList)) {
            throw new ServiceException("暂无上报文件");
        }
        List<DownLoadDto> addressListDto = reportEnclosureService.downLoad(downLoadFiletVo);
        String fileName = "";
        if (addressListDto.size() > 1) {
            fileName = "单位上报文件.zip";
        } else {
            fileName = addressListDto.get(0).getDownLoadName();
        }
        FileOperateUtils.fileToZip(response, addressListDto, fileName);
        return addressListDto;
    }


    /**
     * 业务用房列表
     *
     * @return
     */
    @RequestMapping(value = "/businessHouse", method = RequestMethod.POST)
    @ApiOperation(value = "业务用房列表", notes = "业务用房列表")
    public List<BusinessHouseDto> businessHousing() {
        //获取用户可以查看的单位信息
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();
        return reportUnitService.businessHousing(unitIds);
    }

    /**
     * 建设项目列表
     *
     * @return
     */
    @RequestMapping(value = "/constructHouseList", method = RequestMethod.POST)
    @ApiOperation(value = "建设项目列表", notes = "建设项目列表")
    public List<ConstructHouseDto> constructHouseList() {
        //获取用户可以查看的单位信息
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();
        return reportUnitService.constructHouseList(unitIds);
    }


    /**
     * 副省级城市数据上报信息导出
     *
     * @param
     * @param response
     * @return
     */
    @RequestMapping(value = "/exportDataExcel", method = RequestMethod.POST)
    @ApiOperation(value = "副省级城市数据上报信息导出", notes = "副省级城市数据上报信息导出")
    public boolean exportDataExcel(@RequestBody @Valid UploadDataVo uploadDataVo, HttpServletResponse response) {
        return reportUnitService.exportDataExcel(uploadDataVo, response);
    }

    /**
     * 查询所有单位上报文件按年份
     *
     * @param reportUnitYearVo
     * @return
     */
    @RequestMapping(value = "/allReportEnclosure", method = RequestMethod.POST)
    @ApiOperation(value = "查询所有单位上报文件按年份", notes = "查询所有单位上报文件按年份")
    public List<AdressAndNameVo> allReportEnclosure(@RequestBody @Valid ReportUnitYearVo reportUnitYearVo) {

        return reportEnclosureService.allReportEnclosure(reportUnitYearVo);
    }


}
