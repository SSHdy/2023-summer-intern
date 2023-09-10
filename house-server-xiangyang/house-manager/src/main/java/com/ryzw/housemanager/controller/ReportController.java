package com.ryzw.housemanager.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.service.*;
import com.ryzw.housemanager.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 报表信息查询
 * </p>
 *
 * @author mfl
 * @since 2019/1/18
 */
@RestController
@Slf4j
@Api(value = "报表信息管理", description = "报表信息管理")
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private IHouseService houseService;
    @Autowired
    private IHouseTypeService houseTypeService;
    @Autowired
    private IRecordService recordService;
    @Autowired
    private IRepairApplyService repairApplyService;
    @Autowired
    private IUnitService unitService;
    @Autowired
    private ILeadingOfficeService leadingOfficeService;
    @Autowired
    private IRepairModuleService repairModuleService;

    @Autowired
    private IAuthorityUnitService authorityUnitService;

    /**
     * 房屋信息查询
     *
     * @param houseInfoListVo
     * @return
     */
    @ApiOperation(value = "房屋信息查询", notes = "房屋信息查询")
    @RequestMapping(value = "/houseInfoList", method = RequestMethod.POST)
    public Page<HouseInfoListDto> houseInfoList(@RequestBody @Valid HouseInfoListVo houseInfoListVo) {
        return houseService.houseInfoList(houseInfoListVo);
    }

    /**
     * 房屋超标报表查询
     *
     * @param houseProofVo
     * @return
     */
    @ApiOperation(value = "房屋超标报表查询", notes = "房屋超标报表查询")
    @RequestMapping(value = "/houseProof", method = RequestMethod.POST)
    public Page<HouseProofDto> houseProof(@RequestBody @Valid HouseProofVo houseProofVo) {
        return houseService.houseProof(houseProofVo);
    }

    /**
     * 查询无人办公室
     *
     * @param noOfficeVo
     * @return
     */
    @RequestMapping(value = "selectNoOffice", method = RequestMethod.POST)
    @ApiOperation(value = "查询无人办公室", notes = "查询无人办公室")
    public Page<NoOfficeDto> selectNoOffice(@RequestBody NoOfficeVo noOfficeVo) {
        return houseService.selectNoOffice(noOfficeVo);
    }

    /**
     * 单位占用汇总
     *
     * @param unitNameVo
     * @return
     */
    @RequestMapping(value = "selectUnitUsage", method = RequestMethod.POST)
    @ApiOperation(value = "单位占用汇总", notes = "单位占用汇总")
    public Page<UnitUsageDto> selectUnitUsage(@RequestBody UnitNameVo unitNameVo) {
        return houseService.selectUnitUsage(unitNameVo);
    }

    /**
     * 闲置用房查询
     *
     * @param spareRoomVo
     * @return
     */
    @RequestMapping(value = "spareRoom", method = RequestMethod.POST)
    @ApiOperation(value = "闲置用房查询", notes = "闲置用房查询")
    public SpareRoomAndSumDto spareRoom(@RequestBody SpareRoomVo spareRoomVo) {
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();
        return houseService.spareRoom(spareRoomVo, unitIds);
    }

    /**
     * 租赁房屋报表查询
     *
     * @param rentRoomReportVo
     * @return
     */
    @RequestMapping(value = "rentRoomReport", method = RequestMethod.POST)
    @ApiOperation(value = "租赁房屋报表查询", notes = "租赁房屋报表查询")
    public RentRoomAndSumDto rentRoomReport(@RequestBody RentRoomReportVo rentRoomReportVo) {
        return houseService.rentRoomReport(rentRoomReportVo);
    }

    /**
     * 房屋类型报表查询
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "houseType", method = RequestMethod.POST)
    @ApiOperation(value = "房屋类型报表查询", notes = "房屋类型报表查询")
    public List<HouseTypeReportDto> houseTypeReport(@RequestBody BasicVo<Long> basicVo) {
        List<Long> unitIds = new ArrayList<>();
        if (basicVo.getId() != null) {
            unitIds.add(basicVo.getId());
        } else {
            unitIds.addAll(authorityUnitService.getUnitIdByToken());
        }

        return houseTypeService.houseTypeReport(unitIds, basicVo);
    }

    /**
     * 房屋超标信息查询
     *
     * @param excessiveVo
     * @return
     */
    @RequestMapping(value = "rocordDetail", method = RequestMethod.POST)
    @ApiOperation(value = "房屋超标信息查询", notes = "房屋超标信息查询")
    public List<RecordDto> rocordDetail(@RequestBody @Valid ExcessiveVo excessiveVo) {
        return recordService.recordDetail(excessiveVo);
    }

    /**
     * 维修类型报表查询
     *
     * @param repairReportVo
     * @return
     */
    @RequestMapping(value = "typeReport", method = RequestMethod.POST)
    @ApiOperation(value = "维修类型报表查询", notes = "维修类型报表查询")
    public List<RepairReportDto> typeReport(@RequestBody @Valid RepairReportVo repairReportVo) {
        return repairApplyService.typeReport(repairReportVo);
    }

    /**
     * 单位报表信息查询
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "unitReport", method = RequestMethod.POST)
    @ApiOperation(value = "单位报表信息查询", notes = "单位报表信息查询")
    public UnitReportInfoDto unitReport(@RequestBody @Valid BasicVo<Long> basicVo) {
        return unitService.unitReport(basicVo.getId());
    }

    /**
     * 查询房屋类型面积
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "typeArea", method = RequestMethod.POST)
    @ApiOperation(value = "单位报表信息查询", notes = "单位报表信息查询")
    public List<TypeAreaDto> typeArea(@RequestBody @Valid BasicVo<Long> basicVo) {
        return unitService.typeArea(basicVo.getId());
    }

    /**
     * 领导人员办公室报表信息查询
     *
     * @param leadingOfficeReportVo
     * @return
     */
    @RequestMapping(value = "officeReport", method = RequestMethod.POST)
    @ApiOperation(value = "领导人员办公室报表信息查询", notes = "领导人员办公室报表信息查询")
    public List<LeadingOfficeReportDto> officeReport(@RequestBody @Valid LeadingOfficeReportVo leadingOfficeReportVo) {
        return leadingOfficeService.officeReport(leadingOfficeReportVo);
    }

    /**
     * 一般人员办公室报表信息查询
     *
     * @param leadingOfficeReportVo
     * @return
     */
    @RequestMapping(value = "commonReport", method = RequestMethod.POST)
    @ApiOperation(value = "一般人员办公室报表信息查询", notes = "一般人员办公室报表信息查询")
    public List<LeadingOfficeReportDto> commonReport(@RequestBody @Valid LeadingOfficeReportVo leadingOfficeReportVo) {
        return leadingOfficeService.commonReport(leadingOfficeReportVo);
    }

    /**
     * 服务用房报表查询
     *
     * @param houseTypeVo
     * @return
     */
    @RequestMapping(value = "serviceHouseReport", method = RequestMethod.POST)
    @ApiOperation(value = "服务用房报表查询", notes = "服务用房报表查询")
    public List<TypeReportDto> serviceHouseReport(@RequestBody @Valid HouseTypeVo houseTypeVo) {
        return houseTypeService.serviceHouseReport(houseTypeVo);
    }

    /**
     * 维修报表查询
     *
     * @param repairRecordReportVo
     * @return
     */
    @RequestMapping(value = "repairRecordReport", method = RequestMethod.POST)
    @ApiOperation(value = "查询办公用房维修情况列表", notes = "查询办公用房维修情况列表")
    public List<RepairRecordReportDto> repairRecordReport(@RequestBody RepairRecordReportVo repairRecordReportVo) {
        return repairModuleService.repairRecordReport(repairRecordReportVo);
    }

    @RequestMapping(value = "unitSubordinate", method = RequestMethod.POST)
    @ApiOperation(value = "下属单位信息信息报表查询", notes = "下属单位信息信息报表查询")
    public List<UnitSubordinateDto> unitSubordinate(@RequestBody UnitSubordinateVo unitSubordinateVo) {
        return unitService.unitSubordinate(unitSubordinateVo);
    }

    /**
     * 查询房屋类型报表
     *
     * @param unitAndBuildVo
     * @return
     */
    @RequestMapping(value = "selectHouseType", method = RequestMethod.POST)
    @ApiOperation(value = "查询房屋类型报表", notes = "查询房屋类型报表")
    public List<HouseTypeDto> selectHouseType(@RequestBody UnitAndBuildVo unitAndBuildVo) {
        return houseTypeService.selectHouseType(unitAndBuildVo);
    }

    /**
     * 查询各楼栋的系数
     *
     * @return
     */
    @RequestMapping(value = "selectBuildCoe", method = RequestMethod.POST)
    @ApiOperation(value = "查询各楼栋的系数", notes = "查询各楼栋的系数")
    public List<BuildCoeDto> selectBuildCoe() {
        return houseTypeService.selectBuildCoe();
    }

    /**
     * 查询单楼栋的系数
     *
     * @return
     */
    @RequestMapping(value = "selectOneBuildCoe", method = RequestMethod.POST)
    @ApiOperation(value = "查询单个楼栋的系数", notes = "查询单个楼栋的系数")
    public Object selectOneBuildCoe(@RequestBody BasicVo<Long> basicVo) {
        return houseTypeService.selectOneBuildCoe(basicVo.getId());
    }

    /**
     * 查询超标单位
     *
     * @return
     */
    @RequestMapping(value = "selectExcessUnit", method = RequestMethod.POST)
    @ApiOperation(value = "查询超标单位", notes = "查询超标单位")
    public List<ExcessUnitDto> selectExcessUnit(@RequestBody BasicIdsVo<Long> basicIdsVo) {
        return unitService.selectExcessUnit(basicIdsVo.getIds());
    }

    /**
     * 查询房间明细汇总
     *
     * @return
     */
    @RequestMapping(value = "selectHouseDetailByUnit", method = RequestMethod.POST)
    @ApiOperation(value = "查询房间明细汇总", notes = "查询房间明细汇总")
    public List<HouseDetailByUnitDto> selectHouseDetailByUnit() {
        return unitService.selectHouseDetailByUnit();
    }

    /**
     * 房间明细信息导出
     *
     * @param houseDetailByUnitDtoList
     * @param response
     * @return
     */
    @RequestMapping(value = "/exportHouseDetailExcel", method = RequestMethod.POST)
    @ApiOperation(value = "房间明细信息导出", notes = "房间明细信息导出")
    public boolean exportDataExcel(@RequestBody List<HouseDetailByUnitDto> houseDetailByUnitDtoList, HttpServletResponse response) {
        return unitService.exportHouseDetailExcel(houseDetailByUnitDtoList, response);
    }


}
