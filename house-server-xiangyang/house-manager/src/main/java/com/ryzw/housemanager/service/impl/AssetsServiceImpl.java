package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import com.ryzw.housemanager.dto.AssetsComnDto;
import com.ryzw.housemanager.dto.HttpResultDto;
import com.ryzw.housemanager.entity.*;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.AssetsMapper;
import com.ryzw.housemanager.mapper.OperationFlowMapper;
import com.ryzw.housemanager.mapper.PropertyMapper;
import com.ryzw.housemanager.mapper.PurchasingMapper;
import com.ryzw.housemanager.service.IAssetsDbService;
import com.ryzw.housemanager.service.IAssetsService;
import com.ryzw.housemanager.service.IPurchasingService;
import com.ryzw.housemanager.util.ExcelUtil;
import com.ryzw.housemanager.vo.*;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.Document;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 资产表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-01-10
 */
@Service
@Slf4j
public class AssetsServiceImpl extends ServiceImpl<AssetsMapper, Assets> implements IAssetsService {

    @Autowired
    private AssetsMapper assetsMapper;

    @Autowired
    private OperationFlowMapper operationFlowMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private IAssetsDbService assetsDbService;

    @Autowired
    private IPurchasingService purchasingService;
    @Autowired
    private DozerBeanMapper dozerBeanMapper;
    @Autowired
    private PropertyMapper propertyMapper;
    @Autowired
    private PurchasingMapper purchasingMapper;

    /**
     * 新增assets，并新增assets下面的资产图片
     *
     * @param assetsDbVo
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<String> addAssets(AssetsDbVo assetsDbVo) {
        try {
            //入库批号
            String batchNo = "STORAGE" + System.currentTimeMillis();
            Date operateDate = new Date();

            StringBuilder assetsDb = new StringBuilder();
            List<BasicDBObject> assetsList = assetsDbVo.getAssetsList();
            if (CollectionUtils.isNotEmpty(assetsList)) {
                for (int i = 0; i < assetsList.size(); i++) {
                    BasicDBObject bct = assetsList.get(i);
                    assetsDb.append("items[" + i + "].state=1"
                            + "&" + "items[" + i + "].storeageDate=" + operateDate + "&items[" + i + "].assetId=" + bct.get("icassetId") + "&");
                }
            }
            assetsDb.append("batchNo=" + batchNo + "&" + "operateDate=" + operateDate);
            OperationFlow operationFlow = new OperationFlow();
            synchronized (this) {
                operationFlow.setSerialNumber(batchNo);
            }
            operationFlow.setOperationTime(operateDate);
            operationFlow.setPurchasingId(assetsDbVo.getPurchasingId());
            operationFlow.setPurchasingPname(assetsDbVo.getPurchasingPname());
            //操作类型(1入库  2转移)
            operationFlow.setOperationType(1);
            if (StringUtils.isNotBlank(assetsDbVo.getOperator())) {
                operationFlow.setOperator(assetsDbVo.getOperator());
            }
            operationFlowMapper.insert(operationFlow);

            List<LsHistory> lsHistoryList = new ArrayList<>();
            LsHistory lsHistory = new LsHistory();
            lsHistory.setState(operationFlow.getOperationType());
            lsHistory.setOperationTime(operateDate);
            if (StringUtils.isNotBlank(operationFlow.getOperator())) {
                lsHistory.setOperation(operationFlow.getOperator());
            }
            lsHistory.setSerialNumber(operationFlow.getSerialNumber());
            lsHistoryList.add(lsHistory);

            if (StringUtils.isBlank(operationFlow.getSerialNumber())) {
                throw new ServiceException("流水号生成失败！");
            }

            for (BasicDBObject bjt : assetsList) {
                bjt.append("serialNumber", operationFlow.getSerialNumber());
                bjt.append("state", operationFlow.getOperationType());
                bjt.append("history", lsHistoryList);
                //遍历资产卡片，并对资产卡片数量做减一操作
                if (bjt.get("assetId") != null  ) {
                    Long assetId = Long.parseLong( bjt.get("assetId").toString());
//
                    Property property = propertyMapper.selectById(assetId);
                    String number = property.getNumber();
                    property.setNumber((Integer.parseInt(number) - 1)+ "");
                    UpdateWrapper<Property> wrapper = new UpdateWrapper<>();
                    wrapper.eq("asset_id",assetId);
                    propertyMapper.update(property,wrapper);
                }

                    //如果某个采购下的所有资产录入完毕，则将采购状态改为完成
                if (StringUtils.isNotEmpty(assetsDbVo.getPurchasingId())){
                    Long purchasingId = Long.valueOf(assetsDbVo.getPurchasingId());
                    Purchasing purchasing = purchasingMapper.selectById(purchasingId);
                    int num = purchasingMapper.selectNum(purchasingId);
                    if (num < 1){
                        purchasing.setStatus(1);
                    }else {
                        purchasing.setStatus(0);
                    }
                }

            }
            Collection<BasicDBObject> dbObjects = mongoTemplate.insert(assetsDbVo.getAssetsList(), "assert");
            return dbObjects.stream().map(p -> String.valueOf(p.getObjectId("_id"))).collect(Collectors.toList());

        } catch (Exception ex) {
            log.error("addAssetsError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 资产入库数据导出Excel
     *
     * @param assetsExportVo
     * @param response
     * @return
     */
    @Override
    public Boolean exportAssetsExcel(AssetsExportVo assetsExportVo, HttpServletResponse response) {
        try {
            if (CollectionUtils.isEmpty(assetsExportVo.getAssetsIds())) {
                return false;
            } else {

                List<Document> documentList = new ArrayList<>();
                for (String assetsId : assetsExportVo.getAssetsIds()) {
                    Document document = mongoTemplate.findById(assetsId, Document.class, "assert");
                    documentList.add(document);
                }

                // excel文件名
                String fileName = "资产入库表" + System.currentTimeMillis() + ".xls";

                // sheet名
                String sheetName = "资产入库";

                HSSFWorkbook wb = new HSSFWorkbook();
                String[][] content = new String[documentList.size()][];

                //车辆
                if (AssetsTypeNode.VEHICLE.getValue().equals(assetsExportVo.getAssetsType())) {
                    //excel标题
                    String[] title = {"卡片编号", "资产名称", "资产分类代码", "资产分类名称", "采购组织形式", "预算项目编号", "取得方式", "取得日期",
                            "车辆产地", "规格型号", "品牌", "车辆识别号", "车牌号", "排气量", "发动机号", "车辆行驶证", "销售商", "合同编号", "发票号", "保修截止日期",
                            "编制情况", "财政拨款", "非财政拨款", "财务入账状态", "价值类型", "数量", "价值", "财务入账日期", "入账科目", "会计凭证号", "使用状况",
                            "投入使用日期", "使用/管理部门", "使用人", "累计行驶里程", "车辆用途", "折旧状态", "备注", "持证人", "登记日期", "座位数", "条形码"};

                    for (int i = 0; i < documentList.size(); i++) {
                        content[i] = new String[title.length];
                        Document dot = documentList.get(i);
                        content[i][0] = dot.get("cardId") == null ? "" : String.valueOf(dot.get("cardId"));
                        content[i][1] = dot.get("assetNameComn") == null ? "" : String.valueOf(dot.get("assetNameComn"));
                        content[i][2] = dot.get("categoryCodeComn") == null ? "" : String.valueOf(dot.get("categoryCodeComn"));
                        content[i][3] = dot.get("categoryName") == null ? "" : String.valueOf(dot.get("categoryName"));
                        content[i][4] = dot.get("procurementModeComn") == null ? "" : String.valueOf(dot.get("procurementModeComn"));
                        content[i][5] = dot.get("budgetItemNumberComn") == null ? "" : String.valueOf(dot.get("budgetItemNumberComn"));
                        content[i][6] = dot.get("acquireWayComn") == null ? "" : String.valueOf(dot.get("acquireWayComn"));
                        content[i][7] = dot.get("acquireDateComn") == null ? "" : String.valueOf(dot.get("acquireDateComn"));
                        content[i][8] = dot.get("vehicleProducingArea") == null ? "" : String.valueOf(dot.get("vehicleProducingArea"));
                        content[i][9] = dot.get("specificationType") == null ? "" : String.valueOf(dot.get("specificationType"));
                        content[i][10] = dot.get("assetBrand") == null ? "" : String.valueOf(dot.get("assetBrand"));
                        content[i][11] = dot.get("vehicleIdentificationNumber") == null ? "" : String.valueOf(dot.get("vehicleIdentificationNumber"));
                        content[i][12] = dot.get("licensePlateNumber") == null ? "" : String.valueOf(dot.get("licensePlateNumber"));
                        content[i][13] = dot.get("dischargeVolume") == null ? "" : String.valueOf(dot.get("dischargeVolume"));
                        content[i][14] = dot.get("engineNumber") == null ? "" : String.valueOf(dot.get("engineNumber"));
                        content[i][15] = dot.get("vehicleDrivingPermit") == null ? "" : String.valueOf(dot.get("vehicleDrivingPermit"));
                        content[i][16] = dot.get("assetSeller") == null ? "" : String.valueOf(dot.get("assetSeller"));
                        content[i][17] = dot.get("contractNumber") == null ? "" : String.valueOf(dot.get("contractNumber"));
                        content[i][18] = dot.get("invoiceNumber") == null ? "" : String.valueOf(dot.get("invoiceNumber"));
                        content[i][19] = dot.get("warrantyDeadline") == null ? "" : String.valueOf(dot.get("warrantyDeadline"));
                        content[i][20] = dot.get("compilationSituation") == null ? "" : String.valueOf(dot.get("compilationSituation"));
                        content[i][21] = dot.get("financialAppropriationComn") == null ? "" : String.valueOf(dot.get("financialAppropriationComn"));
                        content[i][22] = dot.get("nonFinancialAppropriationComn") == null ? "" : String.valueOf(dot.get("nonFinancialAppropriationComn"));
                        content[i][23] = dot.get("financialEntryStatusComn") == null ? "" : String.valueOf(dot.get("financialEntryStatusComn"));
                        content[i][24] = dot.get("valueTypeComn") == null ? "" : String.valueOf(dot.get("valueTypeComn"));
                        content[i][25] = dot.get("assetNumberComn") == null ? "" : String.valueOf(dot.get("assetNumberComn"));
                        content[i][26] = dot.get("assetWorthComn") == null ? "" : String.valueOf(dot.get("assetWorthComn"));
                        content[i][27] = dot.get("financialEntryDateComn") == null ? "" : String.valueOf(dot.get("financialEntryDateComn"));
                        content[i][28] = dot.get("accountEntryComn") == null ? "" : String.valueOf(dot.get("accountEntryComn"));
                        content[i][29] = dot.get("accountingVoucherNumberComn") == null ? "" : String.valueOf(dot.get("accountingVoucherNumberComn"));
                        content[i][30] = dot.get("usageStatusComn") == null ? "" : String.valueOf(dot.get("usageStatusComn"));
                        content[i][31] = dot.get("commissioningDateComn") == null ? "" : String.valueOf(dot.get("commissioningDateComn"));
                        content[i][32] = dot.get("useManagementComn") == null ? "" : String.valueOf(dot.get("useManagementComn"));
                        content[i][33] = dot.get("assetUser") == null ? "" : String.valueOf(dot.get("assetUser"));
                        content[i][34] = dot.get("accumulatedMileage") == null ? "" : String.valueOf(dot.get("accumulatedMileage"));
                        content[i][35] = dot.get("vehicleUse") == null ? "" : String.valueOf(dot.get("vehicleUse"));
                        content[i][36] = dot.get("depreciationStatusComn") == null ? "" : String.valueOf(dot.get("depreciationStatusComn"));
                        content[i][37] = dot.get("assetRemarksComn") == null ? "" : String.valueOf(dot.get("assetRemarksComn"));
                        content[i][38] = dot.get("assetHolder") == null ? "" : String.valueOf(dot.get("assetHolder"));
                        content[i][39] = dot.get("registrationDate") == null ? "" : String.valueOf(dot.get("registrationDate"));
                        content[i][40] = dot.get("seatNumber") == null ? "" : String.valueOf(dot.get("seatNumber"));
                        content[i][41] = dot.get("barCodeComn") == null ? "" : String.valueOf(dot.get("barCodeComn"));
                    }
                    wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

                } else if (AssetsTypeNode.HOUSE.getValue().equals(assetsExportVo.getAssetsType())) {
                    String[] title = {"卡片编号", "资产名称", "资产分类代码", "资产分类名称", "坐落位置", "预算项目编号", "取得日期", "取得方式", "产权形式",
                            "权属性质", "房屋权属证明", "权属年限", "权属证号", "权属人", "设计用途", "建筑结构", "建筑面积", "供暖面积", "竣工日期", "发证日期",
                            "财政拨款", "非财政拨款", "财务入账状态", "价值类型", "价值", "均价", "财务入账日期", "入账科目", "会计凭证号", "使用状况", "投入使用日期",
                            "使用/管理部门", "折旧状态", "危房面积", "自用价值", "出租价值", "出借价值", "闲置价值", "其他价值", "备注", "条形码"};

                    for (int i = 0; i < documentList.size(); i++) {
                        content[i] = new String[title.length];
                        Document dot = documentList.get(i);
                        content[i][0] = dot.get("cardId") == null ? "" : String.valueOf(dot.get("cardId"));
                        content[i][1] = dot.get("assetNameComn") == null ? "" : String.valueOf(dot.get("assetNameComn"));
                        content[i][2] = dot.get("categoryCodeComn") == null ? "" : String.valueOf(dot.get("categoryCodeComn"));
                        content[i][3] = dot.get("categoryName") == null ? "" : String.valueOf(dot.get("categoryName"));
                        content[i][4] = dot.get("seatingPosition") == null ? "" : String.valueOf(dot.get("seatingPosition"));
                        content[i][5] = dot.get("budgetItemNumberComn") == null ? "" : String.valueOf(dot.get("budgetItemNumberComn"));
                        content[i][6] = dot.get("acquireDateComn") == null ? "" : String.valueOf(dot.get("acquireDateComn"));
                        content[i][7] = dot.get("acquireWayComn") == null ? "" : String.valueOf(dot.get("acquireWayComn"));
                        content[i][8] = dot.get("propertyRight") == null ? "" : String.valueOf(dot.get("propertyRight"));
                        content[i][9] = dot.get("ownershipNature") == null ? "" : String.valueOf(dot.get("ownershipNature"));
                        content[i][10] = dot.get("ownershipCertificate") == null ? "" : String.valueOf(dot.get("ownershipCertificate"));
                        content[i][11] = dot.get("ownershipAge") == null ? "" : String.valueOf(dot.get("ownershipAge"));
                        content[i][12] = dot.get("ownershipId") == null ? "" : String.valueOf(dot.get("ownershipId"));
                        content[i][13] = dot.get("houseOwner") == null ? "" : String.valueOf(dot.get("houseOwner"));
                        content[i][14] = dot.get("engineNumber") == null ? "" : String.valueOf(dot.get("engineNumber"));
                        content[i][15] = dot.get("designPurpose") == null ? "" : String.valueOf(dot.get("designPurpose"));
                        content[i][16] = dot.get("buildingStructure") == null ? "" : String.valueOf(dot.get("buildingStructure"));
                        content[i][17] = dot.get("floorArea") == null ? "" : String.valueOf(dot.get("floorArea"));
                        content[i][18] = dot.get("heatingArea") == null ? "" : String.valueOf(dot.get("heatingArea"));
                        content[i][19] = dot.get("completionDate") == null ? "" : String.valueOf(dot.get("completionDate"));
                        content[i][20] = dot.get("issueDate") == null ? "" : String.valueOf(dot.get("issueDate"));
                        content[i][21] = dot.get("financialAppropriationComn") == null ? "" : String.valueOf(dot.get("financialAppropriationComn"));
                        content[i][22] = dot.get("nonFinancialAppropriationComn") == null ? "" : String.valueOf(dot.get("nonFinancialAppropriationComn"));
                        content[i][23] = dot.get("financialEntryStatusComn") == null ? "" : String.valueOf(dot.get("financialEntryStatusComn"));
                        content[i][24] = dot.get("valueTypeComn") == null ? "" : String.valueOf(dot.get("valueTypeComn"));
                        content[i][25] = dot.get("assetWorthComn") == null ? "" : String.valueOf(dot.get("assetWorthComn"));
                        content[i][26] = dot.get("averagePrice") == null ? "" : String.valueOf(dot.get("averagePrice"));
                        content[i][27] = dot.get("financialEntryDateComn") == null ? "" : String.valueOf(dot.get("financialEntryDateComn"));
                        content[i][28] = dot.get("accountEntryComn") == null ? "" : String.valueOf(dot.get("accountEntryComn"));
                        content[i][29] = dot.get("accountingVoucherNumberComn") == null ? "" : String.valueOf(dot.get("accountingVoucherNumberComn"));
                        content[i][30] = dot.get("usageStatusComn") == null ? "" : String.valueOf(dot.get("usageStatusComn"));
                        content[i][31] = dot.get("commissioningDateComn") == null ? "" : String.valueOf(dot.get("commissioningDateComn"));
                        content[i][32] = dot.get("useManagementComn") == null ? "" : String.valueOf(dot.get("useManagementComn"));
                        content[i][33] = dot.get("depreciationStatusComn") == null ? "" : String.valueOf(dot.get("depreciationStatusComn"));
                        content[i][34] = dot.get("dangerousArea") == null ? "" : String.valueOf(dot.get("dangerousArea"));
                        content[i][35] = dot.get("selfUseWorth") == null ? "" : String.valueOf(dot.get("selfUseWorth"));
                        content[i][36] = dot.get("rentalValue") == null ? "" : String.valueOf(dot.get("rentalValue"));
                        content[i][37] = dot.get("lendingValue") == null ? "" : String.valueOf(dot.get("lendingValue"));
                        content[i][38] = dot.get("idleValue") == null ? "" : String.valueOf(dot.get("idleValue"));
                        content[i][39] = dot.get("otherValue") == null ? "" : String.valueOf(dot.get("otherValue"));
                        content[i][40] = dot.get("assetRemarksComn") == null ? "" : String.valueOf(dot.get("assetRemarksComn"));
                        content[i][41] = dot.get("barCodeComn") == null ? "" : String.valueOf(dot.get("barCodeComn"));
                    }
                    wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

                } else if (AssetsTypeNode.STRUCTURE.getValue().equals(assetsExportVo.getAssetsType())) {
                    String[] title = {"卡片编号", "资产名称", "资产分类代码", "资产分类名称", "坐落位置", "预算项目编号", "取得日期", "取得方式", "权属性质",
                            "建筑结构", "竣工日期", "财政拨款", "非财政拨款", "财务入账状态", "价值类型", "价值", "财务入账日期", "入账科目", "会计凭证号", "使用状况",
                            "折旧状态", "投入使用日期", "数量", "计量单位", "使用/管理部门", "备注", "条形码"};

                    for (int i = 0; i < documentList.size(); i++) {
                        content[i] = new String[title.length];
                        Document dot = documentList.get(i);
                        content[i][0] = dot.get("cardId") == null ? "" : String.valueOf(dot.get("cardId"));
                        content[i][1] = dot.get("assetNameComn") == null ? "" : String.valueOf(dot.get("assetNameComn"));
                        content[i][2] = dot.get("categoryCodeComn") == null ? "" : String.valueOf(dot.get("categoryCodeComn"));
                        content[i][3] = dot.get("categoryName") == null ? "" : String.valueOf(dot.get("categoryName"));
                        content[i][4] = dot.get("seatingPosition") == null ? "" : String.valueOf(dot.get("seatingPosition"));
                        content[i][5] = dot.get("budgetItemNumberComn") == null ? "" : String.valueOf(dot.get("budgetItemNumberComn"));
                        content[i][6] = dot.get("acquireDateComn") == null ? "" : String.valueOf(dot.get("acquireDateComn"));
                        content[i][7] = dot.get("acquireWayComn") == null ? "" : String.valueOf(dot.get("acquireWayComn"));
                        content[i][8] = dot.get("ownershipNature") == null ? "" : String.valueOf(dot.get("ownershipNature"));
                        content[i][9] = dot.get("buildingStructure") == null ? "" : String.valueOf(dot.get("buildingStructure"));
                        content[i][10] = dot.get("completionDate") == null ? "" : String.valueOf(dot.get("completionDate"));
                        content[i][11] = dot.get("financialAppropriationComn") == null ? "" : String.valueOf(dot.get("financialAppropriationComn"));
                        content[i][12] = dot.get("nonFinancialAppropriationComn") == null ? "" : String.valueOf(dot.get("nonFinancialAppropriationComn"));
                        content[i][13] = dot.get("financialEntryStatusComn") == null ? "" : String.valueOf(dot.get("financialEntryStatusComn"));
                        content[i][14] = dot.get("valueTypeComn") == null ? "" : String.valueOf(dot.get("valueTypeComn"));
                        content[i][15] = dot.get("assetWorthComn") == null ? "" : String.valueOf(dot.get("assetWorthComn"));
                        content[i][16] = dot.get("financialEntryDateComn") == null ? "" : String.valueOf(dot.get("financialEntryDateComn"));
                        content[i][17] = dot.get("accountEntryComn") == null ? "" : String.valueOf(dot.get("accountEntryComn"));
                        content[i][18] = dot.get("accountingVoucherNumberComn") == null ? "" : String.valueOf(dot.get("accountingVoucherNumberComn"));
                        content[i][19] = dot.get("usageStatusComn") == null ? "" : String.valueOf(dot.get("usageStatusComn"));
                        content[i][20] = dot.get("depreciationStatusComn") == null ? "" : String.valueOf(dot.get("depreciationStatusComn"));
                        content[i][21] = dot.get("commissioningDateComn") == null ? "" : String.valueOf(dot.get("commissioningDateComn"));
                        content[i][22] = dot.get("assetNumberComn") == null ? "" : String.valueOf(dot.get("assetNumberComn"));
                        content[i][23] = dot.get("assetUnit") == null ? "" : String.valueOf(dot.get("assetUnit"));
                        content[i][24] = dot.get("useManagementComn") == null ? "" : String.valueOf(dot.get("useManagementComn"));
                        content[i][25] = dot.get("assetRemarksComn") == null ? "" : String.valueOf(dot.get("assetRemarksComn"));
                        content[i][26] = dot.get("barCodeComn") == null ? "" : String.valueOf(dot.get("barCodeComn"));
                    }
                    wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

                } else if (AssetsTypeNode.FURNITURE.getValue().equals(assetsExportVo.getAssetsType())) {
                    String[] title = {"卡片编号", "资产名称", "资产分类代码", "资产分类名称", "存放地点", "采购组织形式", "预算项目编号", "取得方式", "取得日期",
                            "品牌", "销售商", "保修截止日期", "数量", "财政拨款", "非财政拨款", "财务入账状态", "价值类型", "价值", "单价", "财务入账日期", "入账科目", "会计凭证号",
                            "使用状况", "使用/管理部门", "使用人", "投入使用日期", "规格型号", "折旧状态", "备注", "条形码"};

                    for (int i = 0; i < documentList.size(); i++) {
                        content[i] = new String[title.length];
                        Document dot = documentList.get(i);
                        content[i][0] = dot.get("cardId") == null ? "" : String.valueOf(dot.get("cardId"));
                        content[i][1] = dot.get("assetNameComn") == null ? "" : String.valueOf(dot.get("assetNameComn"));
                        content[i][2] = dot.get("categoryCodeComn") == null ? "" : String.valueOf(dot.get("categoryCodeComn"));
                        content[i][3] = dot.get("categoryName") == null ? "" : String.valueOf(dot.get("categoryName"));
                        content[i][4] = dot.get("storageLocation") == null ? "" : String.valueOf(dot.get("storageLocation"));
                        content[i][5] = dot.get("procurementModeComn") == null ? "" : String.valueOf(dot.get("procurementModeComn"));
                        content[i][6] = dot.get("budgetItemNumberComn") == null ? "" : String.valueOf(dot.get("budgetItemNumberComn"));
                        content[i][7] = dot.get("acquireWayComn") == null ? "" : String.valueOf(dot.get("acquireWayComn"));
                        content[i][8] = dot.get("acquireDateComn") == null ? "" : String.valueOf(dot.get("acquireDateComn"));
                        content[i][9] = dot.get("assetBrand") == null ? "" : String.valueOf(dot.get("assetBrand"));
                        content[i][10] = dot.get("assetSeller") == null ? "" : String.valueOf(dot.get("assetSeller"));
                        content[i][11] = dot.get("warrantyDeadline") == null ? "" : String.valueOf(dot.get("warrantyDeadline"));
                        content[i][12] = dot.get("assetNumberComn") == null ? "" : String.valueOf(dot.get("assetNumberComn"));
                        content[i][13] = dot.get("financialAppropriationComn") == null ? "" : String.valueOf(dot.get("financialAppropriationComn"));
                        content[i][14] = dot.get("nonFinancialAppropriationComn") == null ? "" : String.valueOf(dot.get("nonFinancialAppropriationComn"));
                        content[i][15] = dot.get("financialEntryStatusComn") == null ? "" : String.valueOf(dot.get("financialEntryStatusComn"));
                        content[i][16] = dot.get("valueTypeComn") == null ? "" : String.valueOf(dot.get("valueTypeComn"));
                        content[i][17] = dot.get("assetWorthComn") == null ? "" : String.valueOf(dot.get("assetWorthComn"));
                        content[i][18] = dot.get("unitPrice") == null ? "" : String.valueOf(dot.get("unitPrice"));
                        content[i][19] = dot.get("financialEntryDateComn") == null ? "" : String.valueOf(dot.get("financialEntryDateComn"));
                        content[i][20] = dot.get("accountEntryComn") == null ? "" : String.valueOf(dot.get("accountEntryComn"));
                        content[i][21] = dot.get("accountingVoucherNumberComn") == null ? "" : String.valueOf(dot.get("accountingVoucherNumberComn"));
                        content[i][22] = dot.get("usageStatusComn") == null ? "" : String.valueOf(dot.get("usageStatusComn"));
                        content[i][23] = dot.get("useManagementComn") == null ? "" : String.valueOf(dot.get("useManagementComn"));
                        content[i][24] = dot.get("assetUser") == null ? "" : String.valueOf(dot.get("assetUser"));
                        content[i][25] = dot.get("commissioningDateComn") == null ? "" : String.valueOf(dot.get("commissioningDateComn"));
                        content[i][26] = dot.get("specificationType") == null ? "" : String.valueOf(dot.get("specificationType"));
                        content[i][27] = dot.get("depreciationStatusComn") == null ? "" : String.valueOf(dot.get("depreciationStatusComn"));
                        content[i][28] = dot.get("assetRemarksComn") == null ? "" : String.valueOf(dot.get("assetRemarksComn"));
                        content[i][29] = dot.get("barCodeComn") == null ? "" : String.valueOf(dot.get("barCodeComn"));
                    }
                    wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

                } else if (AssetsTypeNode.GEQUIPMENT.getValue().equals(assetsExportVo.getAssetsType())) {
                    String[] title = {"卡片编号", "资产名称", "资产分类代码", "资产分类名称", "存放地点", "采购组织形式", "预算项目编号", "取得方式", "取得日期",
                            "设备用途", "品牌", "生产厂家", "规格型号", "销售商", "合同编号", "发票号", "保修截止日期", "财政拨款", "非财政拨款", "财务入账状态", "价值类型",
                            "数量", "价值", "财务入账日期", "入账科目", "会计凭证号", "使用状况", "投入使用日期", "折旧状态", "使用/管理部门", "使用人", "备注", "机身序列号", "条形码"};

                    for (int i = 0; i < documentList.size(); i++) {
                        content[i] = new String[title.length];
                        Document dot = documentList.get(i);
                        content[i][0] = dot.get("cardId") == null ? "" : String.valueOf(dot.get("cardId"));
                        content[i][1] = dot.get("assetNameComn") == null ? "" : String.valueOf(dot.get("assetNameComn"));
                        content[i][2] = dot.get("categoryCodeComn") == null ? "" : String.valueOf(dot.get("categoryCodeComn"));
                        content[i][3] = dot.get("categoryName") == null ? "" : String.valueOf(dot.get("categoryName"));
                        content[i][4] = dot.get("storageLocation") == null ? "" : String.valueOf(dot.get("storageLocation"));
                        content[i][5] = dot.get("procurementModeComn") == null ? "" : String.valueOf(dot.get("procurementModeComn"));
                        content[i][6] = dot.get("budgetItemNumberComn") == null ? "" : String.valueOf(dot.get("budgetItemNumberComn"));
                        content[i][7] = dot.get("acquireWayComn") == null ? "" : String.valueOf(dot.get("acquireWayComn"));
                        content[i][8] = dot.get("acquireDateComn") == null ? "" : String.valueOf(dot.get("acquireDateComn"));
                        content[i][9] = dot.get("equipmentUse") == null ? "" : String.valueOf(dot.get("equipmentUse"));
                        content[i][10] = dot.get("assetBrand") == null ? "" : String.valueOf(dot.get("assetBrand"));
                        content[i][11] = dot.get("assetManufacturer") == null ? "" : String.valueOf(dot.get("assetManufacturer"));
                        content[i][12] = dot.get("specificationType") == null ? "" : String.valueOf(dot.get("specificationType"));
                        content[i][13] = dot.get("assetSeller") == null ? "" : String.valueOf(dot.get("assetSeller"));
                        content[i][14] = dot.get("contractNumber") == null ? "" : String.valueOf(dot.get("contractNumber"));
                        content[i][15] = dot.get("invoiceNumber") == null ? "" : String.valueOf(dot.get("invoiceNumber"));
                        content[i][16] = dot.get("warrantyDeadline") == null ? "" : String.valueOf(dot.get("warrantyDeadline"));
                        content[i][17] = dot.get("financialAppropriationComn") == null ? "" : String.valueOf(dot.get("financialAppropriationComn"));
                        content[i][18] = dot.get("nonFinancialAppropriationComn") == null ? "" : String.valueOf(dot.get("nonFinancialAppropriationComn"));
                        content[i][19] = dot.get("financialEntryStatusComn") == null ? "" : String.valueOf(dot.get("financialEntryStatusComn"));
                        content[i][20] = dot.get("valueTypeComn") == null ? "" : String.valueOf(dot.get("valueTypeComn"));
                        content[i][21] = dot.get("assetNumberComn") == null ? "" : String.valueOf(dot.get("assetNumberComn"));
                        content[i][22] = dot.get("assetWorthComn") == null ? "" : String.valueOf(dot.get("assetWorthComn"));
                        content[i][23] = dot.get("financialEntryDateComn") == null ? "" : String.valueOf(dot.get("financialEntryDateComn"));
                        content[i][24] = dot.get("accountEntryComn") == null ? "" : String.valueOf(dot.get("accountEntryComn"));
                        content[i][25] = dot.get("accountingVoucherNumberComn") == null ? "" : String.valueOf(dot.get("accountingVoucherNumberComn"));
                        content[i][26] = dot.get("usageStatusComn") == null ? "" : String.valueOf(dot.get("usageStatusComn"));
                        content[i][27] = dot.get("commissioningDateComn") == null ? "" : String.valueOf(dot.get("commissioningDateComn"));
                        content[i][28] = dot.get("depreciationStatusComn") == null ? "" : String.valueOf(dot.get("depreciationStatusComn"));
                        content[i][29] = dot.get("useManagementComn") == null ? "" : String.valueOf(dot.get("useManagementComn"));
                        content[i][30] = dot.get("assetUser") == null ? "" : String.valueOf(dot.get("assetUser"));
                        content[i][31] = dot.get("assetRemarksComn") == null ? "" : String.valueOf(dot.get("assetRemarksComn"));
                        content[i][32] = dot.get("fuselageSerialNumber") == null ? "" : String.valueOf(dot.get("fuselageSerialNumber"));
                        content[i][33] = dot.get("barCodeComn") == null ? "" : String.valueOf(dot.get("barCodeComn"));
                    }
                    wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

                } else if (AssetsTypeNode.SEQUIPMENT.getValue().equals(assetsExportVo.getAssetsType())) {
                    String[] title = {"卡片编号", "资产名称", "资产分类代码", "资产分类名称", "存放地点", "采购组织形式", "预算项目编号", "取得方式", "取得日期",
                            "品牌", "生产厂家", "规格型号", "销售商", "合同编号", "发票号", "保修截止日期", "财政拨款", "非财政拨款", "财务入账状态", "价值类型",
                            "数量", "价值", "财务入账日期", "入账科目", "会计凭证号", "使用状况", "投入使用日期", "折旧状态", "使用/管理部门", "使用人", "设备用途", "备注", "机身序列号", "条形码"};

                    for (int i = 0; i < documentList.size(); i++) {
                        content[i] = new String[title.length];
                        Document dot = documentList.get(i);
                        content[i][0] = dot.get("cardId") == null ? "" : String.valueOf(dot.get("cardId"));
                        content[i][1] = dot.get("assetNameComn") == null ? "" : String.valueOf(dot.get("assetNameComn"));
                        content[i][2] = dot.get("categoryCodeComn") == null ? "" : String.valueOf(dot.get("categoryCodeComn"));
                        content[i][3] = dot.get("categoryName") == null ? "" : String.valueOf(dot.get("categoryName"));
                        content[i][4] = dot.get("storageLocation") == null ? "" : String.valueOf(dot.get("storageLocation"));
                        content[i][5] = dot.get("procurementModeComn") == null ? "" : String.valueOf(dot.get("procurementModeComn"));
                        content[i][6] = dot.get("budgetItemNumberComn") == null ? "" : String.valueOf(dot.get("budgetItemNumberComn"));
                        content[i][7] = dot.get("acquireWayComn") == null ? "" : String.valueOf(dot.get("acquireWayComn"));
                        content[i][8] = dot.get("acquireDateComn") == null ? "" : String.valueOf(dot.get("acquireDateComn"));
                        content[i][9] = dot.get("assetBrand") == null ? "" : String.valueOf(dot.get("assetBrand"));
                        content[i][10] = dot.get("assetManufacturer") == null ? "" : String.valueOf(dot.get("assetManufacturer"));
                        content[i][11] = dot.get("specificationType") == null ? "" : String.valueOf(dot.get("specificationType"));
                        content[i][12] = dot.get("assetSeller") == null ? "" : String.valueOf(dot.get("assetSeller"));
                        content[i][13] = dot.get("contractNumber") == null ? "" : String.valueOf(dot.get("contractNumber"));
                        content[i][14] = dot.get("invoiceNumber") == null ? "" : String.valueOf(dot.get("invoiceNumber"));
                        content[i][15] = dot.get("warrantyDeadline") == null ? "" : String.valueOf(dot.get("warrantyDeadline"));
                        content[i][16] = dot.get("financialAppropriationComn") == null ? "" : String.valueOf(dot.get("financialAppropriationComn"));
                        content[i][17] = dot.get("nonFinancialAppropriationComn") == null ? "" : String.valueOf(dot.get("nonFinancialAppropriationComn"));
                        content[i][18] = dot.get("financialEntryStatusComn") == null ? "" : String.valueOf(dot.get("financialEntryStatusComn"));
                        content[i][19] = dot.get("valueTypeComn") == null ? "" : String.valueOf(dot.get("valueTypeComn"));
                        content[i][20] = dot.get("assetNumberComn") == null ? "" : String.valueOf(dot.get("assetNumberComn"));
                        content[i][21] = dot.get("assetWorthComn") == null ? "" : String.valueOf(dot.get("assetWorthComn"));
                        content[i][22] = dot.get("financialEntryDateComn") == null ? "" : String.valueOf(dot.get("financialEntryDateComn"));
                        content[i][23] = dot.get("accountEntryComn") == null ? "" : String.valueOf(dot.get("accountEntryComn"));
                        content[i][24] = dot.get("accountingVoucherNumberComn") == null ? "" : String.valueOf(dot.get("accountingVoucherNumberComn"));
                        content[i][25] = dot.get("usageStatusComn") == null ? "" : String.valueOf(dot.get("usageStatusComn"));
                        content[i][26] = dot.get("commissioningDateComn") == null ? "" : String.valueOf(dot.get("commissioningDateComn"));
                        content[i][27] = dot.get("depreciationStatusComn") == null ? "" : String.valueOf(dot.get("depreciationStatusComn"));
                        content[i][28] = dot.get("useManagementComn") == null ? "" : String.valueOf(dot.get("useManagementComn"));
                        content[i][29] = dot.get("assetUser") == null ? "" : String.valueOf(dot.get("assetUser"));
                        content[i][30] = dot.get("equipmentUse") == null ? "" : String.valueOf(dot.get("equipmentUse"));
                        content[i][31] = dot.get("assetRemarksComn") == null ? "" : String.valueOf(dot.get("assetRemarksComn"));
                        content[i][32] = dot.get("fuselageSerialNumber") == null ? "" : String.valueOf(dot.get("fuselageSerialNumber"));
                        content[i][33] = dot.get("barCodeComn") == null ? "" : String.valueOf(dot.get("barCodeComn"));
                    }
                    wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);

                } else if (AssetsTypeNode.LAND.getValue().equals(assetsExportVo.getAssetsType())) {
                    String[] title = {"卡片编号", "资产名称", "资产分类代码", "资产分类名称", "坐落位置", "预算项目编号", "取得方式", "取得日期",
                            "土地产权形式", "权属性质", "土地权属证明", "权属年限", "土地证号", "地类(用途)", "使用权类型", "使用权面积", "其中：独用面积",
                            "分摊面积", "发证日期", "财政拨款", "非财政拨款", "财务入账状态", "价值类型", "价值", "均价", "财务入账日期", "土地级次", "会计凭证号",
                            "使用状况", "投入使用日期", "使用/管理部门", "自用面积", "出租面积", "出借面积", "闲置面积", "其他面积", "自用价值", "出租价值", "出借价值", "闲置价值", "其他价值", "折旧状态", "备注", "持证人"};

                    for (int i = 0; i < documentList.size(); i++) {
                        content[i] = new String[title.length];
                        Document dot = documentList.get(i);
                        content[i][0] = dot.get("cardId") == null ? "" : String.valueOf(dot.get("cardId"));
                        content[i][1] = dot.get("assetNameComn") == null ? "" : String.valueOf(dot.get("assetNameComn"));
                        content[i][2] = dot.get("categoryCodeComn") == null ? "" : String.valueOf(dot.get("categoryCodeComn"));
                        content[i][3] = dot.get("categoryName") == null ? "" : String.valueOf(dot.get("categoryName"));
                        content[i][4] = dot.get("seatingPosition") == null ? "" : String.valueOf(dot.get("seatingPosition"));
                        content[i][5] = dot.get("budgetItemNumberComn") == null ? "" : String.valueOf(dot.get("budgetItemNumberComn"));
                        content[i][6] = dot.get("acquireWayComn") == null ? "" : String.valueOf(dot.get("acquireWayComn"));
                        content[i][7] = dot.get("acquireDateComn") == null ? "" : String.valueOf(dot.get("acquireDateComn"));
                        content[i][8] = dot.get("propertyRight") == null ? "" : String.valueOf(dot.get("propertyRight"));
                        content[i][9] = dot.get("ownershipNature") == null ? "" : String.valueOf(dot.get("ownershipNature"));
                        content[i][10] = dot.get("ownershipCertificate") == null ? "" : String.valueOf(dot.get("ownershipCertificate"));
                        content[i][11] = dot.get("ownershipAge") == null ? "" : String.valueOf(dot.get("ownershipAge"));
                        content[i][12] = dot.get("ownershipId") == null ? "" : String.valueOf(dot.get("ownershipId"));
                        content[i][13] = dot.get("categoryUse") == null ? "" : String.valueOf(dot.get("categoryUse"));
                        content[i][14] = dot.get("typeUse") == null ? "" : String.valueOf(dot.get("typeUse"));
                        content[i][15] = dot.get("areaUse") == null ? "" : String.valueOf(dot.get("areaUse"));
                        content[i][16] = dot.get("soleArea") == null ? "" : String.valueOf(dot.get("soleArea"));
                        content[i][17] = dot.get("apportionmentArea") == null ? "" : String.valueOf(dot.get("apportionmentArea"));
                        content[i][18] = dot.get("issueDate") == null ? "" : String.valueOf(dot.get("issueDate"));
                        content[i][19] = dot.get("financialAppropriationComn") == null ? "" : String.valueOf(dot.get("financialAppropriationComn"));
                        content[i][20] = dot.get("nonFinancialAppropriationComn") == null ? "" : String.valueOf(dot.get("nonFinancialAppropriationComn"));
                        content[i][21] = dot.get("financialEntryStatusComn") == null ? "" : String.valueOf(dot.get("financialEntryStatusComn"));
                        content[i][22] = dot.get("valueTypeComn") == null ? "" : String.valueOf(dot.get("valueTypeComn"));
                        content[i][23] = dot.get("assetWorthComn") == null ? "" : String.valueOf(dot.get("assetWorthComn"));
                        content[i][24] = dot.get("averagePrice") == null ? "" : String.valueOf(dot.get("averagePrice"));
                        content[i][25] = dot.get("financialEntryDateComn") == null ? "" : String.valueOf(dot.get("financialEntryDateComn"));
                        content[i][26] = dot.get("landGradation") == null ? "" : String.valueOf(dot.get("landGradation"));
                        content[i][27] = dot.get("accountingVoucherNumberComn") == null ? "" : String.valueOf(dot.get("accountingVoucherNumberComn"));
                        content[i][28] = dot.get("usageStatusComn") == null ? "" : String.valueOf(dot.get("usageStatusComn"));
                        content[i][29] = dot.get("commissioningDateComn") == null ? "" : String.valueOf(dot.get("commissioningDateComn"));
                        content[i][30] = dot.get("useManagementComn") == null ? "" : String.valueOf(dot.get("useManagementComn"));
                        content[i][31] = dot.get("selfUseArea") == null ? "" : String.valueOf(dot.get("selfUseArea"));
                        content[i][32] = dot.get("rentalArea") == null ? "" : String.valueOf(dot.get("rentalArea"));
                        content[i][33] = dot.get("lendingArea") == null ? "" : String.valueOf(dot.get("lendingArea"));
                        content[i][34] = dot.get("idleArea") == null ? "" : String.valueOf(dot.get("idleArea"));
                        content[i][35] = dot.get("otherArea") == null ? "" : String.valueOf(dot.get("otherArea"));
                        content[i][36] = dot.get("selfUseWorth") == null ? "" : String.valueOf(dot.get("selfUseWorth"));
                        content[i][37] = dot.get("rentalValue") == null ? "" : String.valueOf(dot.get("rentalValue"));
                        content[i][38] = dot.get("lendingValue") == null ? "" : String.valueOf(dot.get("lendingValue"));
                        content[i][39] = dot.get("idleValue") == null ? "" : String.valueOf(dot.get("idleValue"));
                        content[i][40] = dot.get("otherValue") == null ? "" : String.valueOf(dot.get("otherValue"));
                        content[i][41] = dot.get("depreciationStatusComn") == null ? "" : String.valueOf(dot.get("depreciationStatusComn"));
                        content[i][42] = dot.get("assetRemarksComn") == null ? "" : String.valueOf(dot.get("assetRemarksComn"));
                        content[i][43] = dot.get("structureHolder") == null ? "" : String.valueOf(dot.get("structureHolder"));
                    }
                    wb = ExcelUtil.getHSSFWorkbook(sheetName, title, content, null);
                }

                // 响应到客户端
                setResponseHeader(response, fileName);
//                response.flushBuffer();
                OutputStream os = new BufferedOutputStream(response.getOutputStream());
                wb.write(os);
                os.flush();
                os.close();
                return true;
            }
        } catch (Exception ex) {
            log.error("exportAssetsExcelError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 发送响应流方法
     *
     * @param response
     * @param fileName
     */
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            // 设置在下载框默认显示的文件名
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("File", URLEncoder.encode(fileName, "UTF-8"));
        } catch (Exception ex) {
            log.error("setResponseHeaderError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 通过mongodb分页查询资产信息列表
     *
     * @param assetsComnVo
     * @return
     */
    @Override
    public Page<AssetsComnDto> pageList(AssetsComnVo assetsComnVo) {
        try {

            MongoCollection<Document> collection = mongoTemplate.getCollection("assert");
            DBObject groupFields = new BasicDBObject("_id", null);
            groupFields.put("count", new BasicDBObject("$sum", 1));
            BasicDBObject group = new BasicDBObject("$group", groupFields);
            List<BasicDBObject> aggreList = new ArrayList<BasicDBObject>();
            Query query = new Query();
            query.with(new Sort(Sort.Direction.DESC, "_id"));
            int skip = (assetsComnVo.getCurrentPage() - 1) * assetsComnVo.getPageSize();
            if (StringUtils.isNotBlank(assetsComnVo.getCategoryCodeComn())) {
                Pattern pattern = Pattern.compile("^.*" + assetsComnVo.getCategoryCodeComn() + ".*$", Pattern.CASE_INSENSITIVE);
                Criteria criteria = Criteria.where("categoryCodeComn").regex(pattern);
                query.addCriteria(criteria);

                DBObject matchFields = new BasicDBObject();
                matchFields.put("categoryCodeComn", new BasicDBObject("$regex", assetsComnVo.getCategoryCodeComn()));
                BasicDBObject match1 = new BasicDBObject("$match", matchFields);
                aggreList.add(match1);
            }
            if (StringUtils.isNotBlank(assetsComnVo.getAssetNameComn())) {
                Pattern pattern = Pattern.compile("^.*" + assetsComnVo.getAssetNameComn() + ".*$", Pattern.CASE_INSENSITIVE);
                Criteria criteria = Criteria.where("assetNameComn").regex(pattern);
                query.addCriteria(criteria);

                DBObject matchFields = new BasicDBObject();
                matchFields.put("assetNameComn", new BasicDBObject("$regex", assetsComnVo.getAssetNameComn()));
                BasicDBObject match2 = new BasicDBObject("$match", matchFields);
                aggreList.add(match2);
            }
            if (assetsComnVo.getAssetsType() != null) {
                Criteria criteria = Criteria.where("assetsType").is(assetsComnVo.getAssetsType());
                query.addCriteria(criteria);

                DBObject matchFields = new BasicDBObject("assetsType", assetsComnVo.getAssetsType());
                BasicDBObject match3 = new BasicDBObject("$match", matchFields);
                aggreList.add(match3);

            }
            aggreList.add(group);
            AggregateIterable<Document> output = collection.aggregate(aggreList);
            int total = 0;
            for (Document dbObject : output) {
                total = (int) dbObject.get("count");
            }
            //获取mongo中记录条数
//            MongoDbUtil mongoDbUtil = new MongoDbUtil();
//            Integer total = mongoDbUtil.getCount(mongoTemplate, "assert",null);
//            long total = mongoTemplate.count(query, AssetsComnDto.class);
            // 从那条记录开始
            query.skip(skip);
            // 取多少条记录
            query.limit(assetsComnVo.getPageSize());
            List<AssetsComnDto> items = mongoTemplate.find(query, AssetsComnDto.class);
            Page<AssetsComnDto> page = new Page<>(assetsComnVo.getCurrentPage(), assetsComnVo.getPageSize(), total);
            page.setRecords(items);
            return page;
        } catch (Exception ex) {
            log.error("pageListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 通过资产编号查询资产信息
     *
     * @param findByAssetIdVo
     * @return
     */
    @Override
    public Page<Document> findByAssetId(FindByAssetIdVo findByAssetIdVo) {
        try {
            Query query = new Query();
            Criteria criteria = Criteria.where("assetsIdComn").is(findByAssetIdVo.getAssetsIdComn());
            query.addCriteria(criteria);
            //mongoTemplate.count计算总数
            long total = mongoTemplate.count(query, AssetsComnDto.class);
            int skip = (findByAssetIdVo.getCurrentPage() - 1) * findByAssetIdVo.getPageSize();
            query.with(new Sort(Sort.Direction.DESC, "_id")).skip(skip).limit(findByAssetIdVo.getPageSize());
            List<Document> items = mongoTemplate.find(query, Document.class, "assert");
            Page<Document> page = new Page<>(findByAssetIdVo.getCurrentPage(), findByAssetIdVo.getPageSize(), total);
            page.setRecords(items);
            return page;
        } catch (Exception ex) {
            log.error("findByAssetIdError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询已经入库的资产信息
     *
     * @param assetsStorageVo
     * @return
     */
    @Override
    public Page<AssetsComnDto> storageAsset(AssetsStorageVo assetsStorageVo) {
        try {
            Query query = new Query();
            query.addCriteria(
                    new Criteria().andOperator(
                            Criteria.where("assetsType").is(assetsStorageVo.getAssetsType()),
                            Criteria.where("cardId").is(null)
                    )
            );
            //mongoTemplate.count计算总数
            long total = mongoTemplate.count(query, AssetsComnDto.class);
            int skip = (assetsStorageVo.getCurrentPage() - 1) * assetsStorageVo.getPageSize();
            query.with(new Sort(Sort.Direction.DESC, "_id")).skip(skip).limit(assetsStorageVo.getPageSize());
            List<AssetsComnDto> items = mongoTemplate.find(query, AssetsComnDto.class, "assert");
            Page<AssetsComnDto> page = new Page<>(assetsStorageVo.getCurrentPage(), assetsStorageVo.getPageSize(), total);
            page.setRecords(items);
            return page;
        } catch (Exception ex) {
            log.error("storageAssetError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 账面资产列表
     *
     * @return
     */
    @Override
    public Object selectAssets() {
        try {
            HttpResultDto resultDto = assetsDbService.selectAssets();
            if (resultDto.getCode() == 1) {
                return resultDto.getData();
            } else {
                throw new ServiceException(resultDto.getMsg());
            }
        } catch (Exception ex) {
            log.error("selectAssetsError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 账面资产详情
     *
     * @param assetsByIdVo
     * @return
     */
    @Override
    public Object selectPurchasingById(AssetsByIdVo assetsByIdVo) {
        try {
            HttpResultDto resultDto = assetsDbService.selectPurchasingById(assetsByIdVo.getPurchasingId());
            if (resultDto.getCode() == 1) {
                return resultDto.getData();
            } else {
                throw new ServiceException(resultDto.getMsg());
            }

        } catch (Exception ex) {
            log.error("selectPurchasingByIdError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 资产入库
     *
     * @param assetsDbVo
     * @return
     */
    @Override
    public List<String> add(AssetsDbVo assetsDbVo) {
        try {
            return this.addAssets(assetsDbVo);
        } catch (Exception ex) {
            log.error("addError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 资产修改
     *
     * @param assetsUpdateVo
     * @return
     */
    @Override
    public boolean updateAssert(AssetsUpdateVo assetsUpdateVo) {
        return false;
    }

    /**
     * 修改资产信息
     *
     * @param renewalAssetVo
     * @return
     */
    @Override
    public boolean renewal(RenewalAssetVo renewalAssetVo) {
        try {
            Query query = new Query();
            Criteria criteria = Criteria.where("_id").is(renewalAssetVo.getId());
            query.addCriteria(criteria);
            Update update = new Update();
            update.set("yardId", renewalAssetVo.getYardId());
            update.set("buildId", renewalAssetVo.getBuildId());
            update.set("floorName", renewalAssetVo.getFloorName());
            update.set("houseId", renewalAssetVo.getHouseId());
            UpdateResult result = mongoTemplate.updateMulti(query, update, "assert");
            return result.getModifiedCount() > 0;
        } catch (Exception ex) {
            log.error("renewalError", ex);
            throw new ServiceException("会计凭证号录入有误！");
        }
    }

    /**
     * 按操作时间所有操作类型统计
     *
     * @param assetsStatisticsVo
     * @return
     */
    @Override
    public List<Document> assertStatistics(AssetsStatisticsVo assetsStatisticsVo) {
        try {
            List<AggregationOperation> operations = new ArrayList<>();
            operations.add(Aggregation.match(Criteria.where("history.state").is(1)));
            GroupOperation groupOperation = Aggregation.group("assetsType")
                    .count().as("num")
                    .sum("$assetWorthComn").as("total")
                    .sum("financialAppropriationComn").as("financialAppropriationComn")
                    .sum("nonFinancialAppropriationComn").as("nonFinancialAppropriationComn")
                    .last("history.operationTime").as("operationTime");
            UnwindOperation unwindOperation = Aggregation.unwind("operationTime");
            operations.add(groupOperation);
            operations.add(unwindOperation);
            if (assetsStatisticsVo.getBeginDate() != null) {
                operations.add(Aggregation.match(Criteria.where("operationTime").gte(assetsStatisticsVo.getBeginDate())));
            }
            if (assetsStatisticsVo.getEndDate() != null) {
                Date date = DateUtils.addDays(assetsStatisticsVo.getEndDate(), 1);
                operations.add(Aggregation.match(Criteria.where("operationTime").lt(date)));
            }
            operations.add(Aggregation.sort(Sort.Direction.ASC, "_id"));


            Aggregation aggregation = Aggregation.newAggregation(operations);
            AggregationResults<Document> aggregationResults = mongoTemplate.aggregate(aggregation, "assert", Document.class);
            return aggregationResults.getMappedResults();
        } catch (Exception ex) {
            log.error("assertStatisticsError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

}
