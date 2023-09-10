package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.ConfigureBuild;
import com.ryzw.housemanager.entity.DistInformation;
import com.ryzw.housemanager.entity.ReportUnit;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.ReportUnitMapper;
import com.ryzw.housemanager.service.IDistInformationService;
import com.ryzw.housemanager.service.IReportUnitService;
import com.ryzw.housemanager.service.IYardService;
import com.ryzw.housemanager.util.ExcelUtil;
import com.ryzw.housemanager.vo.DistInformationVo;
import com.ryzw.housemanager.vo.ReportUnitVo;
import com.ryzw.housemanager.vo.UploadDataVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.bson.Document;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * <p>
 * 单位上报表 服务实现类
 * </p>
 *
 * @author yz
 * @since 2019-08-29
 */
@Service
@Slf4j
public class ReportUnitServiceImpl extends ServiceImpl<ReportUnitMapper, ReportUnit> implements IReportUnitService {

    @Autowired
    private ReportUnitMapper reportUnitMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IDistInformationService distInformationService;

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Autowired
    private IYardService yardService;

    /**
     * 查询单位上报列表
     *
     * @param reportUnitVo
     * @return
     */
    @Override
    public Page<ReportUnitDto> reportUnitList(ReportUnitVo reportUnitVo) {
        try {
            Page<ReportUnitDto> page = new Page<>(reportUnitVo.getCurrentPage(), reportUnitVo.getPageSize());
            return reportUnitMapper.reportUnitList(page, reportUnitVo);
        } catch (Exception ex) {
            log.error("reportUnitListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询所有单位集合
     *
     * @param reportUnitVo
     * @return
     */
    @Override
    public List<ReportUnitDto> unitList(ReportUnitVo reportUnitVo) {
        try {
            Date currDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String yearFormat = sdf.format(currDate);
            reportUnitVo.setReportYear(Integer.valueOf(yearFormat));
            return reportUnitMapper.unitList(reportUnitVo);
        } catch (Exception ex) {
            log.error("unitListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 业务用房列表
     *
     * @return
     */
    @Override
    public List<BusinessHouseDto> businessHousing(List<Long> unitIds) {
        try {
            List<BusinessHouseDto> businessHouseDtos = reportUnitMapper.businessHousing(unitIds);
            if (CollectionUtils.isNotEmpty(businessHouseDtos)) {
                for (BusinessHouseDto businessHouseDto : businessHouseDtos) {
                    List<String> houseIdList = businessHouseDto.getHouseIdList();
                    //查询主要使用单位集合
                    List<String> unitNameList = reportUnitMapper.selectUnitList(houseIdList);
                    businessHouseDto.setUnitNameList(unitNameList);
                    Long yardId = businessHouseDto.getYardId();
                    BusinessAreaDto businessAreaDto = reportUnitMapper.selectArea(yardId);
                    if (businessAreaDto != null) {
                        businessHouseDto.setBuildDate(businessAreaDto.getBuildDate());
                        businessHouseDto.setBuildArea(businessAreaDto.getBuildArea());
                        businessHouseDto.setCoveredArea(businessAreaDto.getCoveredArea());
                    }
                    //查询权属登记情况
                    String warrantRemark = reportUnitMapper.selectRemark(yardId);
                    businessHouseDto.setWarrantRemark(warrantRemark);
                }
            }
            return businessHouseDtos;
        } catch (Exception ex) {
            log.error("businessHousingError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 建设项目列表
     *
     * @return
     */
    @Override
    public List<ConstructHouseDto> constructHouseList(List<Long> unitIds) {
        try {
            List<ConstructHouseDto> resultList = new ArrayList<>();
            //查询所有建设的配置楼栋
            List<ConfigureBuild> configureBuildList = reportUnitMapper.selectBuildConfigure();
            if (CollectionUtils.isNotEmpty(configureBuildList)) {
                for (ConfigureBuild configureBuild : configureBuildList) {
                    Long buildId = configureBuild.getBuildId();
                    Long configureId = configureBuild.getConfigureId();
                    ConstructHouseDto constructHouseDto = reportUnitMapper.constructHouseList(buildId, unitIds);
                    //查询主要使用单位
                    List<String> unitNameList = reportUnitMapper.constructHouseUnitList(buildId);
                    if (constructHouseDto != null) {
                        constructHouseDto.setUnitList(unitNameList);
                        constructHouseDto.setApprovalDepartment("湖北省发改委");
                        //查询mongo中数据
                        Document document = mongoTemplate.findById(configureId, Document.class, "configure");
                        if (document != null) {
                            Map<String, Object> map = (Map<String, Object>) document.get("improvingDataDb");
                            if (map != null && map.containsKey("improvingDataRRC")) {
                                Map<String, String> dataRRCMap = (Map<String, String>) map.get("improvingDataRRC");
                                if (dataRRCMap.containsKey("expenditure")) {
                                    String expenditure = dataRRCMap.get("expenditure");
                                    constructHouseDto.setEstimateInvestment(new BigDecimal(expenditure).divide(new BigDecimal("10000")));
                                }
                                if (dataRRCMap.containsKey("projectName")) {
                                    String projectName = dataRRCMap.get("projectName");
                                    constructHouseDto.setProjectName(projectName);
                                }
                            }
                        }
                        resultList.add(constructHouseDto);
                    }
                }
            }
            return resultList;
        } catch (Exception ex) {
            log.error("constructHouseListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 副省级城市数据上报信息导出
     * @param uploadDataVo
     * @param response
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean exportDataExcel(UploadDataVo uploadDataVo, HttpServletResponse response){
        try {
            ClassPathResource classPathResource = new ClassPathResource("static/excelTemplate/dataReport.xls");
            InputStream inputStream = classPathResource.getInputStream();
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            OutputStream output = null;
            try {
                //新增或者更新行政区划数据
                DistInformation distInformation = dozerBeanMapper.map(uploadDataVo.getDistInformation(), DistInformation.class);
                distInformationService.saveOrUpdate(distInformation);

                HSSFCellStyle style = ExcelUtil.getHSSFCellStyle(hssfWorkbook);

                CountDownLatch countDownLatch = new CountDownLatch(4);

                ExecutorService pool = new ThreadPoolExecutor(4, 10, 1000,
                        TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());


                String distCode = Optional.ofNullable( uploadDataVo.getDistInformation()).map(DistInformationVo::getDistCode).orElse(StringUtils.EMPTY);

                //行政区划
                pool.execute(() -> {
                    exportDistInfo(hssfWorkbook, uploadDataVo.getDistInformation());
                    countDownLatch.countDown();
                });

                //技术业务用房
                pool.execute(() -> {
                    exportBusinessHousing(hssfWorkbook, uploadDataVo.getBusinessHouseDtos(),distCode, style);
                    countDownLatch.countDown();
                });

                //办公用房建设项目
                pool.execute(() -> {
                    exportConstructHouse(hssfWorkbook, uploadDataVo.getConstructHouseDtos(), style);
                    countDownLatch.countDown();
                });

                //市(州、盟)办公用房
                pool.execute(() -> {
                    exportOfficeBuildings(hssfWorkbook, uploadDataVo.getYardUnitProfessionalDtos(), distCode, style);
                    countDownLatch.countDown();
                });

                countDownLatch.await();

                hssfWorkbook.setActiveSheet(0);
                hssfWorkbook.setSelectedTab(0);
                // 设置文件头
                String fileName = uploadDataVo.getDistInformation().getDistName() + "-" + uploadDataVo.getDistInformation().getDistCode() + ".xls";

                // 设置文件头
                response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setHeader("File", URLEncoder.encode(fileName, "UTF-8"));

                // 输出Excel文件
                output = new BufferedOutputStream(response.getOutputStream());

                hssfWorkbook.write(output);
                return true;
            } catch (Exception ex) {
                log.error("exportDataExcelError", ex);
                throw new ServiceException(ex.getMessage());
            }finally {
                if(hssfWorkbook !=null){
                    hssfWorkbook.close();
                }
                if(output !=null){
                    output.flush();
                    output.close();
                }
                if(inputStream !=null){
                    inputStream.close();
                }
            }
        }catch (Exception ex){
            log.error("exportDataExcelError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }



    /**
     * 导出行政区划
     *
     * @param hssfWorkbook
     * @param distInformationVo
     */
    public void exportDistInfo(HSSFWorkbook hssfWorkbook, DistInformationVo distInformationVo) {
        try {
            // 根据页面index 获取行政区划页
            HSSFSheet sheet = hssfWorkbook.getSheet("行政区划");

            //第六行相关操作
            HSSFRow row = sheet.getRow(5);
            HSSFCell hSSFCell1 = row.getCell(0);
            HSSFCell hSSFCell2 = row.getCell(1);
            HSSFCell hSSFCell3 = row.getCell(2);

            //设置表格值
            hSSFCell1.setCellValue(distInformationVo.getDistName());
            hSSFCell2.setCellValue(distInformationVo.getDistLevel());
            hSSFCell3.setCellValue(distInformationVo.getDistCode());

            HSSFRow row2 = sheet.getRow(6);
            HSSFCell hSSFCell4 = row2.getCell(1);
            hSSFCell4.setCellValue(distInformationVo.getFillerUnit() + "，" + distInformationVo.getFillerName());

            HSSFRow row3 = sheet.getRow(7);
            HSSFCell hSSFCell5 = row3.getCell(1);
            hSSFCell5.setCellValue(distInformationVo.getContactNumber());

            HSSFRow row4 = sheet.getRow(8);
            HSSFCell hSSFCell6 = row4.getCell(1);
            Calendar calendar = Calendar.getInstance();
            hSSFCell6.setCellValue(calendar.get(Calendar.YEAR) + "年　" + (calendar.get(Calendar.MONTH) + 1)
                    + "　月　" + calendar.get(Calendar.DATE) + "　日");
        } catch (Exception ex) {
            log.error("exportDistInfoError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 导出技术业务用房
     * @param hssfWorkbook
     * @param businessHouseDtoList
     * @param distCode
     * @param style
     */
    public void exportBusinessHousing(HSSFWorkbook hssfWorkbook, List<BusinessHouseDto> businessHouseDtoList, String distCode, HSSFCellStyle style) {
        try {
            HSSFSheet sheet = hssfWorkbook.getSheet("技术业务用房");
            if (CollectionUtils.isNotEmpty(businessHouseDtoList)) {
                for (int i = 0; i < businessHouseDtoList.size(); i++) {
                    BusinessHouseDto businessHouseDto = businessHouseDtoList.get(i);
                    List contentCell = new ArrayList(9);
                    contentCell.add(i + 1);
                    contentCell.add(StringUtils.isNotBlank(businessHouseDto.getYardPosition()) == false ? "" : businessHouseDto.getYardPosition());
                    contentCell.add(distCode);
                    contentCell.add(businessHouseDto.getCoveredArea() == null ? "" : businessHouseDto.getCoveredArea());
                    contentCell.add(businessHouseDto.getBuildArea() == null ? "" : businessHouseDto.getBuildArea());
                    contentCell.add(CollectionUtils.isNotEmpty(businessHouseDto.getUnitNameList()) == false ? "" : StringUtils.join(businessHouseDto.getUnitNameList(), ","));
                    contentCell.add(StringUtils.isNotBlank(businessHouseDto.getWarrantRemark()) == false ? "" : businessHouseDto.getWarrantRemark());
                    if (businessHouseDto.getBuildDate() != null) {
                        Date buildDate = businessHouseDto.getBuildDate();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(buildDate);
                        contentCell.add(calendar.get(Calendar.YEAR));
                    } else {
                        contentCell.add("");
                    }
                    contentCell.add("");
                    ExcelUtil.reportHSSFWorkbook(sheet, i, contentCell, style);
                }
            }
        } catch (Exception ex) {
            log.error("exportBusinessHousingError", ex);
            throw new ServiceException(ex.getMessage());
        }

    }

    /**
     * 导出办公用房建设项目
     * @param hssfWorkbook
     * @param constructHouseDtoList
     * @param style
     */
    public void exportConstructHouse(HSSFWorkbook hssfWorkbook, List<ConstructHouseDto> constructHouseDtoList, HSSFCellStyle style) {
        try {
            HSSFSheet sheet = hssfWorkbook.getSheet("办公用房建设项目");
            if (CollectionUtils.isNotEmpty(constructHouseDtoList)) {
                for (int i = 0; i < constructHouseDtoList.size(); i++) {
                    ConstructHouseDto constructHouseDto = constructHouseDtoList.get(i);
                    List contentCell = new ArrayList();
                    contentCell.add(i + 1);
                    contentCell.add(StringUtils.isNotBlank(constructHouseDto.getProjectName()) == false ? "" : constructHouseDto.getProjectName());
                    contentCell.add(StringUtils.isNotBlank(constructHouseDto.getApprovalDepartment()) == false ? "" : constructHouseDto.getApprovalDepartment());
                    contentCell.add(StringUtils.isNotBlank(constructHouseDto.getUnitName()) == false ? "" : constructHouseDto.getUnitName());
                    contentCell.add(CollectionUtils.isNotEmpty(constructHouseDto.getUnitList()) == false ? "" : StringUtils.join(constructHouseDto.getUnitList(), ","));
                    contentCell.add(StringUtils.isNotBlank(constructHouseDto.getYardPosition()) == false ? "" : constructHouseDto.getYardPosition());
                    contentCell.add(constructHouseDto.getBuildArea() == null ? "" : constructHouseDto.getBuildArea());
                    contentCell.add(constructHouseDto.getEstimateInvestment() == null ? "" : constructHouseDto.getEstimateInvestment());
                    contentCell.add(StringUtils.isNotBlank(constructHouseDto.getRemark()) == false ? "" : constructHouseDto.getRemark());
                    ExcelUtil.reportHSSFWorkbook(sheet, i, contentCell, style);
                }
            }
        } catch (Exception ex) {
            log.error("exportConstructHouseError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 导出市(州、盟)办公用房
     * @param hssfWorkbook
     * @param yardUnitProfessionalDtoList
     * @param distCode
     * @param style
     */
    public void exportOfficeBuildings(HSSFWorkbook hssfWorkbook, List<YardUnitProfessionalDto> yardUnitProfessionalDtoList, String distCode, HSSFCellStyle style) {
        try {
            int currentRow = 0;
            HSSFSheet sheet = hssfWorkbook.getSheet("市（州、盟）办公用房");
            if (CollectionUtils.isNotEmpty(yardUnitProfessionalDtoList)) {
                for (int i = 0; i < yardUnitProfessionalDtoList.size(); i++) {
                    YardUnitProfessionalDto yardUnitProfessionalDto = yardUnitProfessionalDtoList.get(i);

                    int lastRowNumber;
                    if (CollectionUtils.isNotEmpty(yardUnitProfessionalDto.getUseUnitAndSituation())) {
                        for (int j = 0; j < yardUnitProfessionalDto.getUseUnitAndSituation().size(); j++) {
                            lastRowNumber = sheet.getLastRowNum();

                            List contentCell = new ArrayList();
                            contentCell.add(yardUnitProfessionalDto.getId() == null ? "" : yardUnitProfessionalDto.getId());
                            contentCell.add(StringUtils.isEmpty(yardUnitProfessionalDto.getPosition()) ? "" : yardUnitProfessionalDto.getPosition());
                            contentCell.add(distCode);
                            contentCell.add(yardUnitProfessionalDto.getSiteArea() == null ? "" : yardUnitProfessionalDto.getSiteArea());
                            contentCell.add(yardUnitProfessionalDto.getTotalCoveredArea() == null ? "" : yardUnitProfessionalDto.getTotalCoveredArea());
                            contentCell.add(yardUnitProfessionalDto.getOfficeCoveredArea() == null ? "" : yardUnitProfessionalDto.getOfficeCoveredArea());
                            contentCell.add(yardUnitProfessionalDto.getTechnologyCoveredArea() == null ? "" : yardUnitProfessionalDto.getTechnologyCoveredArea());

                            //使用单位（多个单位填多条）及批复编制情况(人)
                            UseUnitAndSituationDto useUnitAndSituationDto = yardUnitProfessionalDto.getUseUnitAndSituation().get(j);
                            contentCell.add(StringUtils.isNotBlank(useUnitAndSituationDto.getUnitName()) == false ? "" : useUnitAndSituationDto.getUnitName());
                            contentCell.add(StringUtils.isNotBlank(useUnitAndSituationDto.getUnitType()) == false ? "" : useUnitAndSituationDto.getUnitType());
                            contentCell.add(useUnitAndSituationDto.getCityOfficialStaffNum() == null ? "" : useUnitAndSituationDto.getCityOfficialStaffNum());
                            contentCell.add(useUnitAndSituationDto.getCityDeputyStaffNum() == null ? "" : useUnitAndSituationDto.getCityDeputyStaffNum());
                            contentCell.add(useUnitAndSituationDto.getBureauOfficialStaffNum() == null ? "" : useUnitAndSituationDto.getBureauOfficialStaffNum());
                            contentCell.add(useUnitAndSituationDto.getBureauDeputyStaffNum() == null ? "" : useUnitAndSituationDto.getBureauDeputyStaffNum());
                            contentCell.add(useUnitAndSituationDto.getUnderDirectorNum() == null ? "" : useUnitAndSituationDto.getUnderDirectorNum());


                            contentCell.add(yardUnitProfessionalDto.getOfficeUseArea() == null ? "" : yardUnitProfessionalDto.getOfficeUseArea());
                            contentCell.add(yardUnitProfessionalDto.getServiceUseArea() == null ? "" : yardUnitProfessionalDto.getServiceUseArea());
                            contentCell.add(yardUnitProfessionalDto.getEquipmentUseArea() == null ? "" : yardUnitProfessionalDto.getEquipmentUseArea());
                            contentCell.add(yardUnitProfessionalDto.getTotalUseArea() == null ? "" : yardUnitProfessionalDto.getTotalUseArea());
                            contentCell.add(yardUnitProfessionalDto.getAffiliatedCoveredArea() == null ? "" : yardUnitProfessionalDto.getAffiliatedCoveredArea());
                            contentCell.add(StringUtils.isNotBlank(yardUnitProfessionalDto.getRegistration()) == false ? "" : yardUnitProfessionalDto.getRegistration());
                            contentCell.add(StringUtils.isNotBlank(yardUnitProfessionalDto.getBuildingAge()) == false ? "" : yardUnitProfessionalDto.getBuildingAge());
                            contentCell.add(StringUtils.isNotBlank(yardUnitProfessionalDto.getIsRent()) == false ? "" : yardUnitProfessionalDto.getIsRent());
                            contentCell.add("");

                            ExcelUtil.reportHSSFWorkbooks(sheet, lastRowNumber, contentCell, style);
                        }
                        if (yardUnitProfessionalDto.getUseUnitAndSituation().size() > 1) {
                            mergedRegion(currentRow, sheet, yardUnitProfessionalDto);
                            currentRow += yardUnitProfessionalDto.getUseUnitAndSituation().size();
                        } else {
                            currentRow++;
                        }
                    } else {
                        lastRowNumber = sheet.getLastRowNum();
                        List contentCell = new ArrayList();
                        contentCell.add(yardUnitProfessionalDto.getId() == null ? "" : yardUnitProfessionalDto.getId());
                        contentCell.add(StringUtils.isNotBlank(yardUnitProfessionalDto.getPosition()) == false ? "" : yardUnitProfessionalDto.getPosition());
                        contentCell.add(distCode);
                        contentCell.add(yardUnitProfessionalDto.getSiteArea() == null ? "" : yardUnitProfessionalDto.getSiteArea());
                        contentCell.add(yardUnitProfessionalDto.getTotalCoveredArea() == null ? "" : yardUnitProfessionalDto.getTotalCoveredArea());
                        contentCell.add(yardUnitProfessionalDto.getOfficeCoveredArea() == null ? "" : yardUnitProfessionalDto.getOfficeCoveredArea());
                        contentCell.add(yardUnitProfessionalDto.getTechnologyCoveredArea() == null ? "" : yardUnitProfessionalDto.getTechnologyCoveredArea());

                        //没有使用单位（多个单位填多条）及批复编制情况(人)
                        contentCell.add("");
                        contentCell.add("");
                        contentCell.add("");
                        contentCell.add("");
                        contentCell.add("");
                        contentCell.add("");
                        contentCell.add("");

                        contentCell.add(yardUnitProfessionalDto.getOfficeUseArea() == null ? "" : yardUnitProfessionalDto.getOfficeUseArea());
                        contentCell.add(yardUnitProfessionalDto.getServiceUseArea() == null ? "" : yardUnitProfessionalDto.getServiceUseArea());
                        contentCell.add(yardUnitProfessionalDto.getEquipmentUseArea() == null ? "" : yardUnitProfessionalDto.getEquipmentUseArea());
                        contentCell.add(yardUnitProfessionalDto.getTotalUseArea() == null ? "" : yardUnitProfessionalDto.getTotalUseArea());
                        contentCell.add(yardUnitProfessionalDto.getAffiliatedCoveredArea() == null ? "" : yardUnitProfessionalDto.getAffiliatedCoveredArea());
                        contentCell.add(StringUtils.isNotBlank(yardUnitProfessionalDto.getRegistration()) == false ? "" : yardUnitProfessionalDto.getRegistration());
                        contentCell.add(StringUtils.isNotBlank(yardUnitProfessionalDto.getBuildingAge()) == false ? "" : yardUnitProfessionalDto.getBuildingAge());
                        contentCell.add(StringUtils.isNotBlank(yardUnitProfessionalDto.getIsRent()) == false ? "" : yardUnitProfessionalDto.getIsRent());
                        contentCell.add("");

                        ExcelUtil.reportHSSFWorkbooks(sheet, lastRowNumber, contentCell, style);

                        currentRow++;
                    }

                }


            }

        } catch (Exception ex) {
            log.error("exportOfficeBuildingsError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 按文件模板合并单元格
     *
     * @param currentRow
     * @param sheet
     * @param yardUnitProfessionalDto
     */
    private void mergedRegion(int currentRow, HSSFSheet sheet, YardUnitProfessionalDto yardUnitProfessionalDto) {
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 3, 3));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 4, 4));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 5, 5));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 6, 6));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 14, 14));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 15, 15));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 16, 16));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 17, 17));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 18, 18));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 19, 19));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 20, 20));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 21, 21));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 4, 4 + currentRow + yardUnitProfessionalDto.getUseUnitAndSituation().size() - 1, 22, 22));
    }

}
