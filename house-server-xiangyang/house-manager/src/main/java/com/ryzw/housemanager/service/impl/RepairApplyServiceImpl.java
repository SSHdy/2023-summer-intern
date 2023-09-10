package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.*;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.*;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IRepairApplyService;
import com.ryzw.housemanager.service.IRepairDbService;
import com.ryzw.housemanager.service.IRepairPaymentService;
import com.ryzw.housemanager.upload.FileOperateUtils;
import com.ryzw.housemanager.util.MongoDbUtil;
import com.ryzw.housemanager.vo.*;
import com.ryzw.houseprocess.RepairProcess;
import com.ryzw.houseprocess.RepairWorkFlow;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 维修申请表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-03-28
 */
@Service
@Slf4j
public class RepairApplyServiceImpl extends ServiceImpl<RepairApplyMapper, RepairApply> implements IRepairApplyService {

    @Autowired
    RepairApplyMapper repairApplyMapper;

    @Autowired
    RepairModuleMapper repairModuleMapper;

    @Autowired
    RepairImgMapper repairImgMapper;

    @Autowired
    RepairUnitMapper repairUnitMapper;

    @Autowired
    HouseMapper houseMapper;

    @Autowired
    RepairProcess repairProcess;

    @Autowired
    IRepairDbService repairDbService;

    @Autowired
    IAuthorityUnitService authorityUnitService;

    @Autowired
    IRepairPaymentService repairPaymentService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    DozerBeanMapper dozerBeanMapper;

    @Autowired
    RepairDicMapper repairDicMapper;

    @Autowired
    RepairPmMapper repairPmMapper;

    @Autowired
    RepairPartsMapper repairPartsMapper;

    @Autowired
    RepairRemindMapper repairRemindMapper;

    @Autowired
    HouseHistoryMapper houseHistoryMapper;

    @Value("${uploadUrl.url}")
    private String url;


    /**
     * 查询所有维修信息(列表)
     *
     * @param repairListVo
     * @return
     */
    @Override
    public Page<RepairApplyDto> selectAllRepair(RepairListVo repairListVo) {
        try {
            Page<RepairApplyDto> page = new Page<>(repairListVo.getCurrentPage(), repairListVo.getPageSize());
            Page<RepairApplyDto> repairApplyDtoPage = repairApplyMapper.selectAllRepairApply(page, repairListVo);
            for (RepairApplyDto repairApplyDto : repairApplyDtoPage.getRecords()) {
                Long repairApplyId = repairApplyDto.getRepairApplyId();
                List<String> unitNameList = repairApplyMapper.selectUnit(repairApplyId);
                repairApplyDto.setUnitNameList(unitNameList);
            }
            return repairApplyDtoPage;
        } catch (Exception ex) {
            log.error("selectAllRepairError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 新增维修申请
     *
     * @param repairAddVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRepairApply(RepairAddVo repairAddVo) {
        try {
            RepairApply repairApply = dozerBeanMapper.map(repairAddVo, RepairApply.class);
            //申请编号,当前年月日时分秒
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
            String msg = sdf.format(date);
            msg += repairAddVo.getRepairApplicantId();
            repairApply.setRepairApplyNo("WX" + msg);
            //远程调用getUserByToken
            Map<String, Object> userByToken = authorityUnitService.getUserByToken();
            Long userUnitId = Long.valueOf(String.valueOf(userByToken.get("unit_id")));
            repairApply.setUnitId(userUnitId);
            //维修申请时间
            repairApply.setRepairApplyDate(date);
            //当前流程,到了业务科室审批
            repairApply.setStep(StepNode.AODEPARTMENTS.getValue());
            //新增维修申请信息
            repairApplyMapper.insert(repairApply);
            //获取新增后的维修申请Id
            Long repairApplyId = repairApply.getRepairApplyId();
            if (CollectionUtils.isNotEmpty(repairAddVo.getUnitIdList())) {
                for (Long unitId : repairAddVo.getUnitIdList()) {
                    RepairUnit repairUnit = new RepairUnit();
                    repairUnit.setRepairApplyId(repairApplyId);
                    repairUnit.setUnitId(unitId);
                    //新增维修单位信息
                    repairUnitMapper.insert(repairUnit);
                }
            }
            List<RepairModuleVo> repairModuleList = repairAddVo.getRepairModuleList();
            if (CollectionUtils.isNotEmpty(repairModuleList)) {
                for (RepairModuleVo repairModuleVo : repairModuleList) {
                    //获取维修模块对象并设置维修申请Id
                    repairModuleVo.getRepairModule().setRepairApplyId(repairApplyId);
                    //设置是否维修状态默认为0
                    repairModuleVo.getRepairModule().setIsRepair(0);
                    //新增维修模块
                    repairModuleMapper.insert(repairModuleVo.getRepairModule());
                    //获取新增后的维修模块Id
                    Long repairModuleId = repairModuleVo.getRepairModule().getRepairModuleId();
                    if (CollectionUtils.isNotEmpty(repairModuleVo.getAddressList())) {
                        for (Map<String, Object> objMap : repairModuleVo.getAddressList()) {
                            String physicalAddress = String.valueOf(objMap.get("physicalAddress"));
                            String virtualAddress = String.valueOf(objMap.get("virtualAddress"));
                            Integer type = (Integer) objMap.get("type");
                            RepairImg repairImg = new RepairImg();
                            repairImg.setPhysicalAddress(physicalAddress);
                            repairImg.setVirtualAddress(virtualAddress);
                            repairImg.setRepairModuleId(repairModuleId);
                            repairImg.setType(type);
                            //新增维修模块照片
                            repairImgMapper.insert(repairImg);
                        }
                    }
                }
            }

            //按step查询用户id
            List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.AODEPARTMENTS.getValue());
            if (CollectionUtils.isNotEmpty(userIds)) {
                //启动工作流,获取流程实例ID
                String processInstanceId = repairProcess.startRepairProcess(repairApplyId, repairAddVo.getRepairApplicant(), repairAddVo.getRepairApplicantId(), userIds);
                //更新维修申请表里面的流程实例ID字段值
                repairApply.setProcessInstanceId(processInstanceId);
                repairApplyMapper.updateById(repairApply);
            } else {
                throw new ServiceException("没有指定业务科室审批办理人！");
            }
            return true;
        } catch (Exception ex) {
            log.error("addRepairApplyError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改维修申请下维修模块的面积
     *
     * @param repairModuleUpdateVo
     * @return
     */
    @Override
    public boolean updateRepairModule(RepairModuleUpdateVo repairModuleUpdateVo) {
        try {
            RepairModule repairModule = new RepairModule();
            //获取修改的面积并设置
            repairModule.setRepairModuleArea(repairModuleUpdateVo.getRepairModuleArea());
            repairModule.setRepairModuleId(repairModuleUpdateVo.getRepairModuleId());
            repairModuleMapper.updateById(repairModule);
            return true;
        } catch (Exception ex) {
            log.error("updateRepairModuleError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除维修模块的一条记录(将是否维修状态改为1,1代表删除)
     *
     * @param repairModuleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRepairModule(Long repairModuleId) {
        try {
            //查询要删除的维修模块对象
            RepairModule repairModule = repairModuleMapper.selectById(repairModuleId);
            //设置是否维修状态为1
            repairModule.setIsRepair(1);
            //删除维修模块（将是否维修状态改为1）
            repairModuleMapper.updateById(repairModule);
            return true;
        } catch (Exception ex) {
            log.error("deleteRepairModuleError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改维修申请信息(只有未审核才能修改)
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRepairApply(RepairApplyUpdateVo repairApplyUpdateVo) {
        try {
            Long repairApplyId = repairApplyUpdateVo.getRepairApplyId();
            List<RepairModuleVo> repairModuleList = repairApplyUpdateVo.getRepairModuleList();
            List<Long> unitIdList = repairApplyUpdateVo.getUnitIdList();
            //删除原有的维修单位
            repairUnitMapper.delete(new QueryWrapper<RepairUnit>().eq("repair_apply_id", repairApplyId));
            if (CollectionUtils.isNotEmpty(unitIdList)) {
                for (Long unitId : unitIdList) {
                    RepairUnit repairUnit = new RepairUnit();
                    repairUnit.setUnitId(unitId);
                    repairUnit.setRepairApplyId(repairApplyId);
                    //新增维修单位
                    repairUnitMapper.insert(repairUnit);
                }
            }
            if (CollectionUtils.isNotEmpty(repairModuleList)) {
                for (RepairModuleVo repairModuleVo : repairModuleList) {
                    //修改维修模块信息
                    repairModuleMapper.updateById(repairModuleVo.getRepairModule());
                    //获取维修模块Id
                    Long repairModuleId = repairModuleVo.getRepairModule().getRepairModuleId();
                    //获取删除维修照片id集合
                    List<Long> deleteRepairImgIdList = repairModuleVo.getDeleteRepairImgIdList();
                    if (CollectionUtils.isNotEmpty(deleteRepairImgIdList)) {
                        for (Long repairImgId : deleteRepairImgIdList) {
                            RepairImg repairImg = repairImgMapper.selectById(repairImgId);
                            //删除服务器上的维修照片
                            this.deleteRepairImg(repairImg);
                        }
                    }
                    if (CollectionUtils.isNotEmpty(deleteRepairImgIdList)) {
                        for (Long repairImgId : deleteRepairImgIdList) {
                            //删除原数据库上的维修照片
                            repairImgMapper.deleteById(repairImgId);
                        }
                    }
                    //获取要新增的维修图片路径
                    List<Map<String, Object>> addressList = repairModuleVo.getAddressList();
                    if (CollectionUtils.isNotEmpty(addressList)) {
                        for (Map<String, Object> objMap : addressList) {
                            String physicalAddress = String.valueOf(objMap.get("physicalAddress"));
                            String virtualAddress = String.valueOf(objMap.get("virtualAddress"));
                            RepairImg repairImg = new RepairImg();
                            repairImg.setRepairModuleId(repairModuleId);
                            repairImg.setPhysicalAddress(physicalAddress);
                            repairImg.setVirtualAddress(virtualAddress);
                            repairImgMapper.insert(repairImg);
                        }
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("updateRepairApplyError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 维修申请详情
     *
     * @param repairApplyId
     * @return
     */
    @Override
    public RepairApplyDetailDto selectRepairApplyDetail(Long repairApplyId) {
        try {
            //获取维修申请信息详情
            RepairApplyDetailDto repairApplyDetailDto = repairApplyMapper.selectRepairApplyDetail(repairApplyId);
            //查询历史批注信息
            List<Comment> commentList = repairProcess.findCommentByRepairId(repairApplyId);
            if (repairApplyDetailDto != null) {
                repairApplyDetailDto.setCommentList(commentList);
            }
            return repairApplyDetailDto;
        } catch (Exception ex) {
            log.error("selectRepairApplyDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 维修信息详情集合
     *
     * @param repairApplyId
     * @return
     */
    @Override
    public RepairApprovalDetailDto repairApprovalDetail(Long repairApplyId) {
        try {
            RepairApprovalDetailDto repairApprovalDetailDto = repairApplyMapper.selectRepairApprovalDetail(repairApplyId);
            return repairApprovalDetailDto;
        } catch (Exception ex) {
            log.error("repairApprovalDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 维修信息详情
     *
     * @param repairModuleId
     * @return
     */
    @Override
    public RepairMessageDetailDto repairMessageDetail(Long repairModuleId) {
        try {
            //获取维修照片集合
            List<RepairImg> repairImgList = repairImgMapper.selectList(new QueryWrapper<RepairImg>().eq("repair_module_id", repairModuleId));
            RepairMessageDetailDto repairMessageDetailDto = repairApplyMapper.selectRepairMessageDetail(repairModuleId);
            Integer repairPartQuality = repairMessageDetailDto.getRepairPartQuality();
            //获取维修申请Id
            Long repairApplyId = repairMessageDetailDto.getRepairApplyId();
            RepairApply repairApply = repairApplyMapper.selectById(repairApplyId);
            Date completionTime = repairApply.getCompletionTime();
            if (completionTime != null) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(completionTime);
                calendar.add(Calendar.YEAR, repairPartQuality);
                Date newDate = calendar.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String format = dateFormat.format(newDate);
                //设置竣工后的质保期
                repairMessageDetailDto.setCompletionQuality(format);
            }
            repairMessageDetailDto.setRepairImgList(repairImgList);
            return repairMessageDetailDto;
        } catch (Exception ex) {
            log.error("repairMessageDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询相关维修记录
     *
     * @param repairModuleId
     * @return
     */
    @Override
    public List<RepairRecordDetailDto> selectRepairRecord(Long repairModuleId) {
        try {
            RepairModule repairModule = repairModuleMapper.selectById(repairModuleId);
            List<RepairRecordDetailDto> repairRecordDetailDtoList = new ArrayList<>();
            List<RepairRecordDetailDto> recordDetailDtoList = null;
            if (repairModule != null) {
                //获取楼座Id
                Long buildId = repairModule.getBuildId();
                //获取房间Id
                String houseId = repairModule.getHouseId();
                HouseHistory house = houseHistoryMapper.selectById(houseId);
                Integer floorName = null;
                if (house != null) {
                    //获取楼层号
                    floorName = house.getFloorName();
                }
                //获取院落Id
                Long yardId = repairModule.getYardId();
                Long repairPartId = repairModule.getRepairPartId();
                recordDetailDtoList = repairApplyMapper.selectRepairRecord(yardId, buildId, houseId, floorName, repairPartId);
                if (CollectionUtils.isNotEmpty(recordDetailDtoList)) {
                    for (RepairRecordDetailDto recordDetailDtos : recordDetailDtoList) {
                        Date completionTime = recordDetailDtos.getCompletionTime();
                        if (completionTime != null) {
                            Integer repairPartQuality = recordDetailDtos.getRepairPartQuality();
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(completionTime);
                            calendar.add(Calendar.YEAR, repairPartQuality);
                            Date newDate = calendar.getTime();
                            //设置竣工后的质保期
                            recordDetailDtos.setCompletionQuality(newDate);
                        }
                        if (recordDetailDtos.getRepairModuleId() != repairModuleId) {
                            repairRecordDetailDtoList.add(recordDetailDtos);
                        }
                    }
                    return repairRecordDetailDtoList;
                }
            }
            return recordDetailDtoList;
        } catch (Exception ex) {
            log.error("selectRepairRecordError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询某楼栋相关的维修模块记录
     *
     * @param buildId
     * @return
     */
    @Override
    public RepairBuildRecordDto selectRepairBuildRecord(Long buildId) {
        try {
            return repairApplyMapper.selectRepairBuildRecord(buildId);
        } catch (Exception ex) {
            log.error("selectRepairBuildRecordError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 文件下载
     *
     * @param downLoadFiletVo
     * @return
     */
    @Override
    public List<DownLoadDto> downLoad(DownLoadFiletVo downLoadFiletVo) {
        try {
            List<AdressAndNameVo> addressList = downLoadFiletVo.getAddressList();
            List<DownLoadDto> addressListDto = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(addressList)) {
                for (AdressAndNameVo adressAndNameVo : addressList) {
                    if (addressList.size() > 1) {
                        DownLoadDto downLoadDto = new DownLoadDto();
                        downLoadDto.setDownLoadName(adressAndNameVo.getFileName());
                        FileOperateUtils fileOperateUtils = new FileOperateUtils();
                        //判断是否是网路路径
                        if (fileOperateUtils.isUrl(adressAndNameVo.getAddress())) {
                            downLoadDto.setPath(adressAndNameVo.getAddress());
                        } else {
                            downLoadDto.setPath("file:///" + adressAndNameVo.getAddress());
                        }
                        addressListDto.add(downLoadDto);
                    } else {
                        DownLoadDto downLoadDto = new DownLoadDto();
                        downLoadDto.setDownLoadName(adressAndNameVo.getFileName());
                        downLoadDto.setPath(adressAndNameVo.getAddress());
                        addressListDto.add(downLoadDto);
                    }
                }
            }
            return addressListDto;
        } catch (Exception ex) {
            log.error("downLoadError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询项目评审信息
     *
     * @param projectReviewtVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectReviewtDto projectReviewDetail(ProjectReviewtVo projectReviewtVo) {
        try {
            RepairApply repairApply = repairApplyMapper.selectById(projectReviewtVo.getRepairApplyId());
            //设置参数
            ProjectReviewtDto projectReviewtDto = dozerBeanMapper.map(repairApply, ProjectReviewtDto.class);
            if (Optional.ofNullable(projectReviewtVo.getLeaderState()).orElse(1).equals(1)) {
                List<AggregationOperation> operations = new ArrayList<>();
                operations.add(Aggregation.unwind("detailDesignDb.repairApprovalDataList"));
                operations.add(Aggregation.unwind("projectBudgetDb.repairApprovalDataList"));
                operations.add(Aggregation.match(Criteria.where("_id").is(projectReviewtVo.getRepairApplyId())
                        .and("detailDesignDb.repairApprovalDataList.type").in(1, 3)));
                GroupOperation groupOperation = Aggregation.group("_id")
                        .addToSet("detailDesignDb.repairApprovalDataList").as("address")
                        .addToSet("projectBudgetDb.repairApprovalDataList").as("repairFileName");
                operations.add(groupOperation);
                operations.add(Aggregation.project("_id", "address", "repairFileName"));
                Aggregation aggregation = Aggregation.newAggregation(operations);
                AggregationResults<Document> aggregationResults = mongoTemplate.aggregate(aggregation, "applyExtend", Document.class);
                projectReviewtDto.setDesignFileList(aggregationResults.getMappedResults());
            } else if (Optional.ofNullable(projectReviewtVo.getLeaderState()).orElse(1).equals(2)) {
                List<AggregationOperation> operations = new ArrayList<>();
                operations.add(Aggregation.unwind("detailDesignDb.repairApprovalDataList"));
                operations.add(Aggregation.unwind("projectBudgetDb.repairApprovalDataList"));
                operations.add(Aggregation.unwind("projectReviewDb.decreaseAmount"));
                operations.add(Aggregation.unwind("projectReviewDb.repairApprovalDataList"));
                operations.add(Aggregation.match(Criteria.where("_id").is(projectReviewtVo.getRepairApplyId())
                        .and("detailDesignDb.repairApprovalDataList.type").in(1, 3)));
                GroupOperation groupOperation = Aggregation.group("_id")
                        .addToSet("detailDesignDb.repairApprovalDataList").as("address")
                        .addToSet("projectBudgetDb.repairApprovalDataList").as("repairFileName")
                        .addToSet("projectReviewDb.decreaseAmount").as("decAmount")
                        .addToSet("projectReviewDb.repairApprovalDataList").as("approval");
                operations.add(groupOperation);
                operations.add(Aggregation.project("_id", "address", "repairFileName", "decAmount", "approval"));
                Aggregation aggregation = Aggregation.newAggregation(operations);
                AggregationResults<Document> aggregationResults = mongoTemplate.aggregate(aggregation, "applyExtend", Document.class);
                projectReviewtDto.setDesignFileList(aggregationResults.getMappedResults());
            }
            return projectReviewtDto;
        } catch (Exception ex) {
            log.error("projectReviewDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询项目预算信息
     *
     * @param projectReviewtVo
     * @return
     */
    @Override
    public ProjectBudgetDto projectbudgetDetail(ProjectReviewtVo projectReviewtVo) {
        try {
            Long repairApplyId = projectReviewtVo.getRepairApplyId();
            RepairApply repairApply = repairApplyMapper.selectById(repairApplyId);
            //设置参数
            ProjectBudgetDto projectBudgetDto = dozerBeanMapper.map(repairApply, ProjectBudgetDto.class);
            if (Optional.ofNullable(projectReviewtVo.getLeaderState()).orElse(1).equals(1)) {
                List<AggregationOperation> operations = new ArrayList<>();
                operations.add(Aggregation.unwind("detailDesignDb.repairApprovalDataList"));
                operations.add(Aggregation.match(Criteria.where("_id").is(repairApplyId)
                        .and("detailDesignDb.repairApprovalDataList.type").in(1, 3)));
                GroupOperation groupOperation = Aggregation.group("_id")
                        .addToSet("detailDesignDb.repairApprovalDataList").as("address");
                operations.add(groupOperation);
                operations.add(Aggregation.project("_id", "address"));
                Aggregation aggregation = Aggregation.newAggregation(operations);
                AggregationResults<Document> aggregationResults = mongoTemplate.aggregate(aggregation, "applyExtend", Document.class);
                projectBudgetDto.setDesignFileList(aggregationResults.getMappedResults());
            } else if (Optional.ofNullable(projectReviewtVo.getLeaderState()).orElse(1).equals(2)) {
                List<AggregationOperation> operations = new ArrayList<>();
                operations.add(Aggregation.unwind("detailDesignDb.repairApprovalDataList"));
                operations.add(Aggregation.unwind("projectBudgetDb.repairApprovalDataList"));
                operations.add(Aggregation.match(Criteria.where("_id").is(repairApplyId)
                        .and("detailDesignDb.repairApprovalDataList.type").in(1, 3)));
                GroupOperation groupOperation = Aggregation.group("_id")
                        .addToSet("detailDesignDb.repairApprovalDataList").as("address")
                        .addToSet("projectBudgetDb.repairApprovalDataList").as("budget");
                operations.add(groupOperation);
                operations.add(Aggregation.project("_id", "address", "budget"));
                Aggregation aggregation = Aggregation.newAggregation(operations);
                AggregationResults<Document> aggregationResults = mongoTemplate.aggregate(aggregation, "applyExtend", Document.class);
                projectBudgetDto.setDesignFileList(aggregationResults.getMappedResults());
            }
            return projectBudgetDto;
        } catch (Exception ex) {
            log.error("projectbudgetDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 维修审批列表
     *
     * @param repairApprovalVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<RepairApprovalDto> repairApprovalList(RepairApprovalVo repairApprovalVo) {
        try {
            Page<RepairApprovalDto> page = new Page<>(repairApprovalVo.getCurrentPage(), repairApprovalVo.getPageSize());
            List<RepairWorkFlow> repairList = repairProcess.findTaskListById(repairApprovalVo.getUserId());
            List<Long> repairIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(repairList)) {
                for (RepairWorkFlow repairWorkFlow : repairList) {
                    RepairApply repair = new RepairApply();
                    repair.setRepairApplyId(repairWorkFlow.getRepairId());
                    repair.setTaskId(repairWorkFlow.getTaskId());
                    repairApplyMapper.updateById(repair);
                    repairIds.add(repairWorkFlow.getRepairId());
                }
                repairApprovalVo.setRepairIds(repairIds);
                Page<RepairApprovalDto> repairApprovalPage = repairApplyMapper.selectRepairApproval(page, repairApprovalVo);
                return repairApprovalPage;
            } else {
                return new Page<>(repairApprovalVo.getCurrentPage(), repairApprovalVo.getPageSize());
            }

        } catch (Exception ex) {
            log.error("repairApprovalListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 查询对应流程下的文件路径
     *
     * @param projectFileVo
     * @return
     */
    @Override
    public List<Document> projectFileAddress(ProjectFileVo projectFileVo) {
        try {
            List<Document> applyExtend = null;
            Query query = new Query();
            if (projectFileVo.getStep().equals(StepNode.UMSCHEME.getValue())) {
                query.fields().include("repairPlanDb.repairApprovalDataList.physicalAddress")
                        .include("repairPlanDb.repairApprovalDataList.repairFileName").exclude("_id");
                query.addCriteria(Criteria.where("_id").is(projectFileVo.getRepairApplyId()));
                //获取mongodb自费维修上传的设计图纸集合
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");

            } else if (projectFileVo.getStep().equals(StepNode.USMAINTENANCE.getValue())) {
                query.fields().include("ysRepairPlanDb.repairApprovalDataList.physicalAddress")
                        .include("ysRepairPlanDb.repairApprovalDataList.repairFileName").exclude("_id");
                query.addCriteria(Criteria.where("_id").is(projectFileVo.getRepairApplyId()));
                //获取mongodb维修预算上传的设计图纸集合
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");

            } else if (projectFileVo.getStep().equals(StepNode.FSDESIGN.getValue())) {
                query.fields().include("researchApprovalDb.repairApprovalDataList.physicalAddress")
                        .include("researchApprovalDb.repairApprovalDataList.repairFileName").exclude("_id");
                query.addCriteria(Criteria.where("_id").is(projectFileVo.getRepairApplyId()));
                //获取mongodb可行性研究的设计图纸集合
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");

            } else if (projectFileVo.getStep().equals(StepNode.CNACCEPTANCE.getValue())) {
                query.fields().include("constructManageDb.auditReport.physicalAddress")
                        .include("constructManageDb.auditReport.repairFileName")
                        .include("constructManageDb.buildDrawings.physicalAddress")
                        .include("constructManageDb.buildDrawings.repairFileName").exclude("_id");
                query.addCriteria(Criteria.where("_id").is(projectFileVo.getRepairApplyId()));
                //获取mongodb竣工验收
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");
            }
            return applyExtend;
        } catch (Exception ex) {
            log.error("projectFileAddressError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询维修类型列表
     *
     * @param repairApplyId
     * @return
     */
    @Override
    public RepairStepDto selectRepairType(Long repairApplyId) {
        try {
            RepairStepDto repairStepDto = new RepairStepDto();
            RepairApply repairApply = repairApplyMapper.selectById(repairApplyId);
            //获取当前流程
            Integer step = repairApply.getStep();
            //获取维修类型
            Integer repairType = repairApply.getRepairType();
            //获取维修概算
            BigDecimal budgetEstimate = repairApply.getBudgetEstimate();
            Integer flag = null;
            List<RepairTypeDto> repairTypeDtoList = new ArrayList<>();
            //刚走完科室审批或是零星维修
            if ((repairType == null && step.equals(StepNode.EXAPPROVAL.getValue())) || (repairType != null && repairType.equals(1))) {
                RepairTypeDto repairTypeDto = new RepairTypeDto();
                repairTypeDto.setStepName(SelectStepNode.AODEPARTMENTS.getName());
                repairTypeDto.setStep(SelectStepNode.AODEPARTMENTS.getFlag().longValue());
                repairTypeDto.setStepValue(SelectStepNode.AODEPARTMENTS.getValue());
                repairTypeDtoList.add(repairTypeDto);
                repairStepDto.setRepairTypeDtoList(repairTypeDtoList);
                repairStepDto.setState(0);
                //是否走内控项目事前审批
                if (step.equals(StepNode.POPROJECTS.getValue())) {
                    RepairTypeDto repairTypeDto2 = new RepairTypeDto();
                    repairTypeDto2.setStepName(StepNode.POPROJECTS.getName());
                    repairTypeDto2.setStep(StepNode.POPROJECTS.getValue().longValue());
                    repairTypeDto2.setStepValue(StepNode.POPROJECTS.getValue());
                    repairTypeDtoList.add(repairTypeDto2);
                }
                return repairStepDto;
            }
            //刚提出申请
            if (repairType == null) {
                return repairStepDto;
            }
            List<StepNodeTypeDto> stepNodeList = Arrays.stream(SelectStepNode.values())
                    .map(p -> new StepNodeTypeDto(p.getValue(), p.getName(), p.getType(), p.getFlag())).collect(Collectors.toList());
            for (StepNodeTypeDto stepNodeTypeDto : stepNodeList) {
                if (stepNodeTypeDto.getValue().equals(step)) {
                    flag = stepNodeTypeDto.getFlag();
                    break;
                }
            }
            for (StepNodeTypeDto stepNodeTypeDto : stepNodeList) {
                if ((stepNodeTypeDto.getType().equals(repairType) || stepNodeTypeDto.getType().equals(3)) && stepNodeTypeDto.getFlag() < flag) {
                    if (budgetEstimate != null && budgetEstimate.compareTo(new BigDecimal(2000000)) > 0) {
                        //维修概算大于两百万,走发改委,状态为1
                        repairStepDto.setState(1);
                    } else {
                        repairStepDto.setState(0);
                    }
                    //添加对应维修类型
                    RepairTypeDto repairTypeDto = new RepairTypeDto();
                    repairTypeDto.setStep(stepNodeTypeDto.getFlag().longValue());
                    repairTypeDto.setStepName(stepNodeTypeDto.getKey());
                    repairTypeDto.setStepValue(stepNodeTypeDto.getValue());
                    repairTypeDtoList.add(repairTypeDto);
                }
            }
            repairStepDto.setRepairTypeDtoList(repairTypeDtoList);
            return repairStepDto;
        } catch (Exception ex) {
            log.error("selectRepairTypeError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 审核申请审批
     *
     * @param repairFlowVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveSubmitRepair(@Valid RepairFlowVo repairFlowVo) {
        try {
            RepairWorkFlow repairWorkFlow = dozerBeanMapper.map(repairFlowVo, RepairWorkFlow.class);

            RepairApply repair = new RepairApply();
            repair.setRepairApplyId(repairFlowVo.getRepairId());
            if (StringUtils.isNotBlank(repairFlowVo.getResponsiblePerson())) {
                repair.setResponsiblePerson(repairFlowVo.getResponsiblePerson());
            }
            if (StringUtils.isNotBlank(repairFlowVo.getRepairUnitName())) {
                repair.setRepairUnitName(repairFlowVo.getRepairUnitName());
            }

            //3.业务科室审批
            if (repairFlowVo.getStep().equals(StepNode.AODEPARTMENTS.getValue())) {
                this.busApproval(repairWorkFlow, repairFlowVo, repair);
            }


            //4.线下党组会审批
            if (repairFlowVo.getStep().equals(StepNode.EXAPPROVAL.getValue())) {
                this.dzhApproval(repairWorkFlow, repairFlowVo, repair);
            }


            //5.自费维修上传维修方案
            if (repairFlowVo.getStep().equals(StepNode.UMSCHEME.getValue())) {
                this.repairPlan(repairWorkFlow, repairFlowVo, repair);
            }


            //6.审批维修方案
            if (repairFlowVo.getStep().equals(StepNode.EXMPAPPROVE.getValue())) {
                this.approvalRepairPlan(repairWorkFlow, repairFlowVo, repair);
            }

            //19.自费维修上传预算方案
            if (repairFlowVo.getStep().equals(StepNode.USMAINTENANCE.getValue())) {
                this.ysRepairPlan(repairWorkFlow, repairFlowVo, repair);
            }

            //20.审批预算方案
            if (repairFlowVo.getStep().equals(StepNode.BPAPPROVAL.getValue())) {
                this.approvalYsPlan(repairWorkFlow, repairFlowVo, repair);
            }

            //8.可行性研究审批
            if (repairFlowVo.getStep().equals(StepNode.FSDESIGN.getValue())) {
                this.researchApproval(repairWorkFlow, repairFlowVo, repair);
            }

            //9.机关管理局费用维修领导审批
            if (repairFlowVo.getStep().equals(StepNode.LEADERAPPROVAL.getValue())) {
                this.leaderApproval(repairWorkFlow, repairFlowVo, repair);
            }

            //21.报政府或发改委
            if (repairFlowVo.getStep().equals(StepNode.GOVERNMENT.getValue())) {
                this.governmentApproval(repairWorkFlow, repairFlowVo, repair);
            }

            //22.项目施工设计
            if (repairFlowVo.getStep().equals(StepNode.CONSTRUCTIONDESIGN.getValue())) {
                this.detailDesign(repairWorkFlow, repairFlowVo, repair);
            }

            //23.维修预算
            if (repairFlowVo.getStep().equals(StepNode.PROJECTBUDGET.getValue())) {
                this.projectBudget(repairWorkFlow, repairFlowVo, repair);
            }

            //23.预算评审
            if (repairFlowVo.getStep().equals(StepNode.PROJECTREVIEW.getValue())) {
                this.projectReview(repairWorkFlow, repairFlowVo, repair);
            }

            //23.指定项目负责人
            if (repairFlowVo.getStep().equals(StepNode.PROJECTLEADER.getValue())) {
                this.projectLeader(repairWorkFlow, repairFlowVo, repair);
            }

            return true;
        } catch (Exception ex) {
            log.error("saveSubmitRepairError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 业务科室审批
     *
     * @param repairWorkFlow
     * @param repairFlowVo
     * @param repair
     */
    @Transactional(rollbackFor = Exception.class)
    public void busApproval(RepairWorkFlow repairWorkFlow, RepairFlowVo repairFlowVo, RepairApply repair) {
        try {

            //实施专项维修
            if (repairFlowVo.getAuditState().equals(1)) {
                repair.setStep(StepNode.EXAPPROVAL.getValue());
                List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.EXAPPROVAL.getValue());
                //设置线下党组会审批权限用户
                if (CollectionUtils.isNotEmpty(userIds)) {
                    repairWorkFlow.setPwUserIds(userIds);
                } else {
                    throw new ServiceException("没有指定线下党组会审批办理人！");
                }
            }

            //零星维修，调内控接口，触发内控走采购流程
            if (repairFlowVo.getAuditState().equals(3)) {
                RepairHttpResultVo repairHttpResultVo;
                if (repairFlowVo.getRepairHttpResultVo() != null) {
                    repairHttpResultVo = dozerBeanMapper.map(repairFlowVo.getRepairHttpResultVo(), RepairHttpResultVo.class);
                    repairHttpResultVo.setName(repairFlowVo.getName());
                    repairHttpResultVo.setPhoneNum(repairFlowVo.getPhoneNum());
                    repairHttpResultVo.setEtype("A");
                } else {
                    throw new ServiceException("维修内控审批流程传输的参数不能为空！");
                }

                String title = StringUtils.isBlank(repairHttpResultVo.getTitle()) == true ? null : repairHttpResultVo.getTitle();
                String detailid = StringUtils.isBlank(repairHttpResultVo.getDetailid()) == true ? null : repairHttpResultVo.getDetailid();
                String description = StringUtils.isBlank(repairHttpResultVo.getDescription()) == true ? null : repairHttpResultVo.getDescription();
                String common_name = StringUtils.isBlank(repairHttpResultVo.getCommon_name()) == true ? null : repairHttpResultVo.getCommon_name();
                String common_price = StringUtils.isBlank(repairHttpResultVo.getCommon_price()) == true ? null : repairHttpResultVo.getCommon_price();
                String common_quantity = StringUtils.isBlank(repairHttpResultVo.getCommon_quantity()) == true ? null : repairHttpResultVo.getCommon_quantity();
                String common_unit = StringUtils.isBlank(repairHttpResultVo.getCommon_unit()) == true ? null : repairHttpResultVo.getCommon_unit();
                String common_total = StringUtils.isBlank(repairHttpResultVo.getCommon_total()) == true ? null : repairHttpResultVo.getCommon_total();

                HttpResultDto resultDto = repairDbService.auditDbRepair(repairHttpResultVo.getName(), repairHttpResultVo.getPhoneNum(),
                        title, detailid, repairHttpResultVo.getEtype(), description, common_name, common_price, common_quantity, common_unit, common_total);
                if (resultDto.getCode() == 1) {
                    //成功，将内控的审批单号记录下来与我们的维修申请号绑定
                    if (resultDto.getData() != null) {
                        Map repairDbMap = (Map) resultDto.getData();
                        if (repairDbMap.get("sano") != null) {
                            repair.setSano(String.valueOf(repairDbMap.get("sano")));
                        }
                    }

                } else {
                    throw new ServiceException("内控：" + resultDto.getMsg());
                }

                repair.setStep(StepNode.POPROJECTS.getValue());
                repair.setRepairType(RepairTypeEnum.SPORADIC.getValue());
            }

            ProcessInstance pi = repairProcess.saveThreeSubmitTask(repairWorkFlow);

            if (pi == null) {
                //审批不通过
                if (repairFlowVo.getAuditState().equals(2)) {
                    repair.setApplyStatus(ApplyStatusEnum.AUDITFAILED.getValue());
                }
            } else {
                //审核中
                repair.setApplyStatus(ApplyStatusEnum.INAUDIT.getValue());
            }
            repairApplyMapper.updateById(repair);

            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(repairFlowVo.getRepairId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("busApprovalAuditState", repairFlowVo.getAuditState());
            commentMap.put("busApprovalUser", repairFlowVo.getUserName());
            commentMap.put("busApprovalTime", new Date());
            if (StringUtils.isNotBlank(repairFlowVo.getComment())) {
                commentMap.put("busApprovalComment", repairFlowVo.getComment());
            }
            commentMap.put("userId", repairFlowVo.getUserId());
            commentMap.put("step", StepNode.AODEPARTMENTS.getValue());
            update.set("busApprovalCommentDb", commentMap);
            mongoTemplate.upsert(query, update, "applyExtend");
        } catch (Exception ex) {
            log.error("busApprovalError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 零星维修 内控采购流程审批
     *
     * @param processHttpResultVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean procurementApproval(ProcessHttpResultVo processHttpResultVo) {
        try {
            RepairWorkFlow repairWorkFlow = new RepairWorkFlow();
            RepairApply repair = this.getOne(new QueryWrapper<RepairApply>().eq("sano", processHttpResultVo.getSano()));

            if (repair != null) {
                //内控回调，只有是零星维修时，才做相关操作
                if (repair.getRepairType().equals(1)) {
                    repairWorkFlow.setProcessInstanceId(repair.getProcessInstanceId());
                    repairWorkFlow.setRepairId(repair.getRepairApplyId());

                    repairWorkFlow.setAuditState(processHttpResultVo.getAuditState());
                    if (StringUtils.isNotBlank(processHttpResultVo.getCurrentUser())) {
                        repairWorkFlow.setCurrentUser(processHttpResultVo.getCurrentUser());
                    }
                    if (StringUtils.isNotBlank(processHttpResultVo.getComment())) {
                        repairWorkFlow.setComment(processHttpResultVo.getComment());
                    }

                    ProcessInstance pi = repairProcess.saveEighteenSubmitTask(repairWorkFlow);
                    if (pi == null) {
                        //审批通过
                        if (processHttpResultVo.getAuditState().equals(1)) {
                            repair.setApplyStatus(ApplyStatusEnum.AUDITPASS.getValue());
                            //审批不通过
                        } else if (processHttpResultVo.getAuditState().equals(2)) {
                            repair.setApplyStatus(ApplyStatusEnum.AUDITFAILED.getValue());
                        }
                    }
                    repairApplyMapper.updateById(repair);
                }
            } else {
                throw new ServiceException("所传事前审批单据号不存在！");
            }
            return true;
        } catch (Exception ex) {
            log.error("procurementApprovalError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 线下党组会审批
     *
     * @param repairWorkFlow
     * @param repairFlowVo
     * @param repair
     */
    @Transactional(rollbackFor = Exception.class)
    public void dzhApproval(RepairWorkFlow repairWorkFlow, RepairFlowVo repairFlowVo, RepairApply repair) {
        try {
            //自费维修上传维修方案
            if (repairFlowVo.getAuditState().equals(1)) {
                repair.setStep(StepNode.UMSCHEME.getValue());
                repair.setRepairType(RepairTypeEnum.EXPENSE.getValue());

                List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.UMSCHEME.getValue());
                //设置自费维修上传维修方案审批人
                if (CollectionUtils.isNotEmpty(userIds)) {
                    repairWorkFlow.setRepairPlanUserIds(userIds);
                } else {
                    throw new ServiceException("没有指定自费维修上传维修方案办理人！");
                }
            }


            //机关管理局费用维修
            if (repairFlowVo.getAuditState().equals(3)) {
                repair.setStep(StepNode.FSDESIGN.getValue());
                repair.setRepairType(RepairTypeEnum.AUTHORITY.getValue());
                List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.FSDESIGN.getValue());
                //设置目方案设计可行性研究办理人
                if (CollectionUtils.isNotEmpty(userIds)) {
                    repairWorkFlow.setResearchUserIds(userIds);
                } else {
                    throw new ServiceException("没有指定目方案设计可行性研究办理人！");
                }
            }

            ProcessInstance pi = repairProcess.saveFourSubmitTask(repairWorkFlow);

            if (pi == null) {
                //审批不通过
                if (repairFlowVo.getAuditState().equals(2)) {
                    repair.setApplyStatus(ApplyStatusEnum.AUDITFAILED.getValue());
                }
            } else {
                //审核中
                repair.setApplyStatus(ApplyStatusEnum.INAUDIT.getValue());
            }
            repairApplyMapper.updateById(repair);


            //党组会上传审批原件
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(repairFlowVo.getRepairId()));
            Update update = new Update();
            update.set("dzhApprovalDb", repairFlowVo.getBasicDBObject());
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("dzhApprovalAuditState", repairFlowVo.getAuditState());
            commentMap.put("dzhApprovalUser", repairFlowVo.getUserName());
            commentMap.put("dzhApprovalTime", new Date());
            if (StringUtils.isNotBlank(repairFlowVo.getComment())) {
                commentMap.put("dzhApprovalComment", repairFlowVo.getComment());
            }
            commentMap.put("userId", repairFlowVo.getUserId());
            commentMap.put("step", StepNode.EXAPPROVAL.getValue());
            update.set("dzhApprovalCommentDb", commentMap);
            mongoTemplate.upsert(query, update, "applyExtend");
        } catch (Exception ex) {
            log.error("dzhApprovalError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 自费维修上传维修方案
     *
     * @param repairWorkFlow
     * @param repairFlowVo
     * @param repair
     */
    @Transactional(rollbackFor = Exception.class)
    public void repairPlan(RepairWorkFlow repairWorkFlow, RepairFlowVo repairFlowVo, RepairApply repair) {
        try {
            repair.setStep(StepNode.EXMPAPPROVE.getValue());
            List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.EXMPAPPROVE.getValue());
            //设置审批维修方案办理人
            if (CollectionUtils.isNotEmpty(userIds)) {
                repairWorkFlow.setApprovalRepairUserIds(userIds);
            } else {
                throw new ServiceException("没有指定审批维修方案办理人！");
            }

            repairProcess.saveFiveSubmitTask(repairWorkFlow);
            repairApplyMapper.updateById(repair);


            //自费维修上传审批原件
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(repairFlowVo.getRepairId()));
            Update update = new Update();
            update.set("repairPlanDb", repairFlowVo.getBasicDBObject());
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("userId", repairFlowVo.getUserId());
            commentMap.put("step", StepNode.UMSCHEME.getValue());
            update.set("repairPlanCommentDb", commentMap);
            mongoTemplate.upsert(query, update, "applyExtend");

            //删除服务器上的文件
            if (CollectionUtils.isNotEmpty(repairFlowVo.getPhysicalAddressList())) {
                for (String physicalAddress : repairFlowVo.getPhysicalAddressList()) {
                    this.deleteRepairData(physicalAddress);
                }
            }
        } catch (Exception ex) {
            log.error("repairPlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 审批维修方案
     *
     * @param repairWorkFlow
     * @param repairFlowVo
     * @param repair
     */
    @Transactional(rollbackFor = Exception.class)
    public void approvalRepairPlan(RepairWorkFlow repairWorkFlow, RepairFlowVo repairFlowVo, RepairApply repair) {
        try {
            //同意自费维修上传维修方案
            if (repairFlowVo.getAuditState().equals(1)) {
                repair.setStep(StepNode.USMAINTENANCE.getValue());
                List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.USMAINTENANCE.getValue());
                //设置自费维修上传预算方案办理人
                if (CollectionUtils.isNotEmpty(userIds)) {
                    repairWorkFlow.setBudgetPlanUserIds(userIds);
                } else {
                    throw new ServiceException("没有指定自费维修上传预算方案办理人！");
                }
            }


            //不同意自费维修上传维修方案
            if (repairFlowVo.getAuditState().equals(2)) {
                repair.setStep(StepNode.UMSCHEME.getValue());
            }

            repairProcess.saveSixSubmitTask(repairWorkFlow);
            repairApplyMapper.updateById(repair);


            //自费维修审批将不同意批注信息存到mongodb
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(repairFlowVo.getRepairId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("aRepairPlanAuditState", repairFlowVo.getAuditState());
            commentMap.put("aRepairPlanUser", repairFlowVo.getUserName());
            commentMap.put("aRepairPlanTime", new Date());
            if (StringUtils.isNotBlank(repairFlowVo.getComment())) {
                commentMap.put("aRepairPlanComment", repairFlowVo.getComment());
            }
            commentMap.put("userId", repairFlowVo.getUserId());
            commentMap.put("step", StepNode.EXMPAPPROVE.getValue());
            update.set("aRepairPlanCommentDb", commentMap);
            mongoTemplate.upsert(query, update, "applyExtend");
        } catch (Exception ex) {
            log.error("approvalRepairPlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 自费维修上传预算方案
     *
     * @param repairWorkFlow
     * @param repairFlowVo
     * @param repair
     */
    @Transactional(rollbackFor = Exception.class)
    public void ysRepairPlan(RepairWorkFlow repairWorkFlow, RepairFlowVo repairFlowVo, RepairApply repair) {
        try {
            repair.setStep(StepNode.BPAPPROVAL.getValue());
            List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.BPAPPROVAL.getValue());
            //设置审批预算方案办理人
            if (CollectionUtils.isNotEmpty(userIds)) {
                repairWorkFlow.setApprovalBudgetUserIds(userIds);
            } else {
                throw new ServiceException("没有指定审批预算方案办理人！");
            }

            repairProcess.saveNineteenSubmitTask(repairWorkFlow);
            repairApplyMapper.updateById(repair);


            //自费维修上传预算方案原件
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(repairFlowVo.getRepairId()));
            Update update = new Update();
            update.set("ysRepairPlanDb", repairFlowVo.getBasicDBObject());
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("userId", repairFlowVo.getUserId());
            commentMap.put("step", StepNode.USMAINTENANCE.getValue());
            update.set("ysRepairPlanCommentDb", commentMap);
            mongoTemplate.upsert(query, update, "applyExtend");

            //删除服务器上的文件
            if (CollectionUtils.isNotEmpty(repairFlowVo.getPhysicalAddressList())) {
                for (String physicalAddress : repairFlowVo.getPhysicalAddressList()) {
                    this.deleteRepairData(physicalAddress);
                }
            }
        } catch (Exception ex) {
            log.error("ysRepairPlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 审批预算方案
     *
     * @param repairWorkFlow
     * @param repairFlowVo
     * @param repair
     */
    @Transactional(rollbackFor = Exception.class)
    public void approvalYsPlan(RepairWorkFlow repairWorkFlow, RepairFlowVo repairFlowVo, RepairApply repair) {
        try {
            //同意自费维修上传预算方案
            if (repairFlowVo.getAuditState().equals(1)) {
                repair.setStep(StepNode.UDAMAINTENANCE.getValue());
            }


            //不同意自费维修上传预算方案
            if (repairFlowVo.getAuditState().equals(2)) {
                repair.setStep(StepNode.USMAINTENANCE.getValue());
            }

            ProcessInstance pi = repairProcess.saveTwentySubmitTask(repairWorkFlow);

            if (pi == null) {
                //审批通过
                if (repairFlowVo.getAuditState().equals(1)) {
                    repair.setApplyStatus(ApplyStatusEnum.AUDITPASS.getValue());
                }
            } else {
                //审核中
                repair.setApplyStatus(ApplyStatusEnum.INAUDIT.getValue());
            }
            repairApplyMapper.updateById(repair);

            //自费预算方案审批将不同意批注信息存到mongodb
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(repairFlowVo.getRepairId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("ysPlanAuditState", repairFlowVo.getAuditState());
            commentMap.put("ysPlanUser", repairFlowVo.getUserName());
            commentMap.put("ysPlanTime", new Date());
            if (StringUtils.isNotBlank(repairFlowVo.getComment())) {
                commentMap.put("ysPlanComment", repairFlowVo.getComment());
            }
            commentMap.put("userId", repairFlowVo.getUserId());
            commentMap.put("step", StepNode.BPAPPROVAL.getValue());
            update.set("ysPlanCommentDb", commentMap);
            mongoTemplate.upsert(query, update, "applyExtend");
        } catch (Exception ex) {
            log.error("approvalYsPlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 可行性研究审批
     *
     * @param repairWorkFlow
     * @param repairFlowVo
     * @param repair
     */
    @Transactional(rollbackFor = Exception.class)
    public void researchApproval(RepairWorkFlow repairWorkFlow, RepairFlowVo repairFlowVo, RepairApply repair) {
        try {
            List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.LEADERAPPROVAL.getValue());
            //设置审批预算方案办理人
            if (CollectionUtils.isNotEmpty(userIds)) {
                repairWorkFlow.setLeaderApprovalUserIds(userIds);
            } else {
                throw new ServiceException("没有指定领导审批办理人！");
            }
            if (StringUtils.isNotBlank(repairFlowVo.getRepairProject())) {
                repair.setRepairProject(repairFlowVo.getRepairProject());
            }

            if (repairFlowVo.getBudgetEstimate() != null) {
                repair.setBudgetEstimate(repairFlowVo.getBudgetEstimate());
            }

            if (repairFlowVo.getUnitAreaPrice() != null) {
                repair.setUnitAreaPrice(repairFlowVo.getUnitAreaPrice());
            }

            repair.setStep(StepNode.LEADERAPPROVAL.getValue());
            repairProcess.saveEightSubmitTask(repairWorkFlow);
            repairApplyMapper.updateById(repair);


            //可行性研究审批相关字段
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(repairFlowVo.getRepairId()));
            Update update = new Update();
            update.set("researchApprovalDb", repairFlowVo.getBasicDBObject());
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("userId", repairFlowVo.getUserId());
            commentMap.put("step", StepNode.FSDESIGN.getValue());
            update.set("rACommentDb", commentMap);
            mongoTemplate.upsert(query, update, "applyExtend");

            //删除服务器上的文件
            if (CollectionUtils.isNotEmpty(repairFlowVo.getPhysicalAddressList())) {
                for (String physicalAddress : repairFlowVo.getPhysicalAddressList()) {
                    this.deleteRepairData(physicalAddress);
                }
            }
        } catch (Exception ex) {
            log.error("researchApprovalError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 领导审批
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void leaderApproval(RepairWorkFlow repairWorkFlow, RepairFlowVo repairFlowVo, RepairApply repair) {
        try {
            if (repairFlowVo.getAuditState().equals(1)) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("repair_dic_key", "repair_dic_budget");
                RepairDic repairDic = repairDicMapper.selectOne(queryWrapper);
                RepairApply repairApply = repairApplyMapper.selectById(repairFlowVo.getRepairId());
                //维修概算超过200W,报政府或发改委
                if (StringUtils.isNotBlank(repairDic.getRepairDicValue())) {
                    if (repairApply.getBudgetEstimate() != null) {
                        if (repairApply.getBudgetEstimate().compareTo(new BigDecimal(repairDic.getRepairDicValue())) >= 0) {
                            repairWorkFlow.setAuditState(1);
                            repair.setStep(StepNode.GOVERNMENT.getValue());
                            List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.GOVERNMENT.getValue());
                            //设置报政府或发改委办理人
                            if (CollectionUtils.isNotEmpty(userIds)) {
                                repairWorkFlow.setGtUserIds(userIds);
                            } else {
                                throw new ServiceException("没有指定报政府或发改委办理人！");
                            }
                        }
                        //维修概算超过200W,走项目施工设计
                        if (repairApply.getBudgetEstimate().compareTo(new BigDecimal(repairDic.getRepairDicValue())) < 0) {
                            repairWorkFlow.setAuditState(4);
                            repair.setStep(StepNode.CONSTRUCTIONDESIGN.getValue());
                            List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.CONSTRUCTIONDESIGN.getValue());
                            //设置项目施工设计办理人
                            if (CollectionUtils.isNotEmpty(userIds)) {
                                repairWorkFlow.setPcdUserIds(userIds);
                            } else {
                                throw new ServiceException("没有指定项目施工设计办理人！");
                            }
                        }
                    }
                }
            }

            //不同意修改预算，返回到可行性研究审批
            if (repairFlowVo.getAuditState().equals(3)) {
                repair.setStep(StepNode.FSDESIGN.getValue());

                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(repairFlowVo.getRepairId()));
                Update update = new Update();
                Map<String, Object> commentMap = new HashMap<>();
                if (StringUtils.isNotBlank(repairFlowVo.getComment())) {
                    commentMap.put("rhaComment", repairFlowVo.getComment());
                    commentMap.put("rhaAuditState", repairFlowVo.getAuditState());
                }
                update.set("rhaCommentDb", commentMap);
                mongoTemplate.upsert(query, update, "applyExtend");
            }


            ProcessInstance pi = repairProcess.saveNineSubmitTask(repairWorkFlow);
            if (pi == null) {
                //审批不通过
                if (repairWorkFlow.getAuditState() == 2) {
                    repair.setApplyStatus(ApplyStatusEnum.AUDITFAILED.getValue());
                }
            } else {
                //审核中
                repair.setApplyStatus(ApplyStatusEnum.INAUDIT.getValue());
            }

            repairApplyMapper.updateById(repair);

            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(repairFlowVo.getRepairId()));
            Update update = new Update();
            update.set("ldaDb", repairFlowVo.getBasicDBObject());
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("ldaAuditState", repairFlowVo.getAuditState());
            commentMap.put("ldaUser", repairFlowVo.getUserName());
            commentMap.put("ldaTime", new Date());
            if (StringUtils.isNotBlank(repairFlowVo.getComment())) {
                commentMap.put("ldaComment", repairFlowVo.getComment());
            }
            commentMap.put("userId", repairFlowVo.getUserId());
            commentMap.put("step", StepNode.LEADERAPPROVAL.getValue());
            update.set("ldaCommentDb", commentMap);
            mongoTemplate.upsert(query, update, "applyExtend");
            //删除服务器上的文件
            if (CollectionUtils.isNotEmpty(repairFlowVo.getPhysicalAddressList())) {
                for (String physicalAddress : repairFlowVo.getPhysicalAddressList()) {
                    this.deleteRepairData(physicalAddress);
                }
            }
        } catch (Exception ex) {
            log.error("leaderApprovalError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 政府或发改委审批
     *
     * @param repairWorkFlow
     * @param repairFlowVo
     * @param repair
     */
    @Transactional(rollbackFor = Exception.class)
    public void governmentApproval(RepairWorkFlow repairWorkFlow, RepairFlowVo repairFlowVo, RepairApply repair) {
        try {
            //政府或发改委同意维修
            if (repairFlowVo.getAuditState().equals(1)) {
                repair.setStep(StepNode.CONSTRUCTIONDESIGN.getValue());
                List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.CONSTRUCTIONDESIGN.getValue());
                //设置项目施工设计办理人
                if (CollectionUtils.isNotEmpty(userIds)) {
                    repairWorkFlow.setPcdUserIds(userIds);
                } else {
                    throw new ServiceException("没有指定项目施工设计办理人！");
                }
            }

            ProcessInstance pi = repairProcess.saveTwentyOneSubmitTask(repairWorkFlow);

            if (pi == null) {
                //审批不通过
                if (repairFlowVo.getAuditState().equals(2)) {
                    repair.setApplyStatus(ApplyStatusEnum.AUDITFAILED.getValue());
                }
            } else {
                //审核中
                repair.setApplyStatus(ApplyStatusEnum.INAUDIT.getValue());
            }
            repairApplyMapper.updateById(repair);

            //发改委审批批示文件
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(repairFlowVo.getRepairId()));
            Update update = new Update();
            update.set("governmentApprovalDb", repairFlowVo.getBasicDBObject());
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("galAuditState", repairFlowVo.getAuditState());
            commentMap.put("galUser", repairFlowVo.getUserName());
            commentMap.put("galTime", new Date());
            if (StringUtils.isNotBlank(repairFlowVo.getComment())) {
                commentMap.put("galComment", repairFlowVo.getComment());
            }
            commentMap.put("userId", repairFlowVo.getUserId());
            commentMap.put("step", StepNode.GOVERNMENT.getValue());
            update.set("galCommentDb", commentMap);
            mongoTemplate.upsert(query, update, "applyExtend");
        } catch (Exception ex) {
            log.error("governmentApprovalError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 项目施工设计
     *
     * @param repairWorkFlow
     * @param repairFlowVo
     * @param repair
     */
    @Transactional(rollbackFor = Exception.class)
    public void detailDesign(RepairWorkFlow repairWorkFlow, RepairFlowVo repairFlowVo, RepairApply repair) {
        try {
            repair.setStep(StepNode.PROJECTBUDGET.getValue());
            if (StringUtils.isNotBlank(repairFlowVo.getDesignUnit())) {
                repair.setDesignUnit(repairFlowVo.getDesignUnit());
            }
            if (StringUtils.isNotBlank(repairFlowVo.getPlanningPermit())) {
                repair.setPlanningPermit(repairFlowVo.getPlanningPermit());
            }

            List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.PROJECTBUDGET.getValue());
            //设置维修预算办理人
            if (CollectionUtils.isNotEmpty(userIds)) {
                repairWorkFlow.setPdUserIds(userIds);
            } else {
                throw new ServiceException("没有指定维修预算办理人！");
            }

            repairProcess.saveTwentyTwoSubmitTask(repairWorkFlow);
            repairApplyMapper.updateById(repair);

            //发改委审批批示文件
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(repairFlowVo.getRepairId()));
            Update update = new Update();
            update.set("detailDesignDb", repairFlowVo.getBasicDBObject());
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("userId", repairFlowVo.getUserId());
            commentMap.put("step", StepNode.CONSTRUCTIONDESIGN.getValue());
            update.set("detailDesignCommentDb", commentMap);
            mongoTemplate.upsert(query, update, "applyExtend");
        } catch (Exception ex) {
            log.error("detailDesignError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 维修预算
     *
     * @param repairWorkFlow
     * @param repairFlowVo
     * @param repair
     */
    @Transactional(rollbackFor = Exception.class)
    public void projectBudget(RepairWorkFlow repairWorkFlow, RepairFlowVo repairFlowVo, RepairApply repair) {
        try {
            repair.setStep(StepNode.PROJECTREVIEW.getValue());
            if (StringUtils.isNotBlank(repairFlowVo.getAuditUnit())) {
                repair.setAuditUnit(repairFlowVo.getAuditUnit());
            }
            if (repairFlowVo.getTotalBudgetPrice() != null) {
                repair.setTotalBudgetPrice(repairFlowVo.getTotalBudgetPrice());
            }
            List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.PROJECTREVIEW.getValue());
            //设置预算评审办理人
            if (CollectionUtils.isNotEmpty(userIds)) {
                repairWorkFlow.setPrUserIds(userIds);
            } else {
                throw new ServiceException("没有指定预算评审办理人！");
            }

            repairProcess.saveTwentyThreeSubmitTask(repairWorkFlow);
            repairApplyMapper.updateById(repair);

            //发改委审批批示文件
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(repairFlowVo.getRepairId()));
            Update update = new Update();
            update.set("projectBudgetDb", repairFlowVo.getBasicDBObject());
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("userId", repairFlowVo.getUserId());
            commentMap.put("step", StepNode.PROJECTBUDGET.getValue());
            update.set("projectBudgetCommentDb", commentMap);
            mongoTemplate.upsert(query, update, "applyExtend");
        } catch (Exception ex) {
            log.error("projectBudgetError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 预算评审
     *
     * @param repairWorkFlow
     * @param repairFlowVo
     * @param repair
     */
    @Transactional(rollbackFor = Exception.class)
    public void projectReview(RepairWorkFlow repairWorkFlow, RepairFlowVo repairFlowVo, RepairApply repair) {
        try {
            repair.setStep(StepNode.ICPROCESS.getValue());
            if (StringUtils.isNotBlank(repairFlowVo.getAccreditationUnit())) {
                repair.setAccreditationUnit(repairFlowVo.getAccreditationUnit());
            }
            if (repairFlowVo.getFinalSum() != null) {
                repair.setFinalSum(repairFlowVo.getFinalSum());
            }

            RepairHttpResultVo repairHttpResultVo;
            if (repairFlowVo.getRepairHttpResultVo() != null) {
                repairHttpResultVo = dozerBeanMapper.map(repairFlowVo.getRepairHttpResultVo(), RepairHttpResultVo.class);
                repairHttpResultVo.setName(repairFlowVo.getName());
                repairHttpResultVo.setPhoneNum(repairFlowVo.getPhoneNum());
                repairHttpResultVo.setEtype("A");
            } else {
                throw new ServiceException("维修内控审批流程传输的参数不能为空！");
            }

            String title = StringUtils.isBlank(repairHttpResultVo.getTitle()) == true ? null : repairHttpResultVo.getTitle();
            String detailid = StringUtils.isBlank(repairHttpResultVo.getDetailid()) == true ? null : repairHttpResultVo.getDetailid();
            String description = StringUtils.isBlank(repairHttpResultVo.getDescription()) == true ? null : repairHttpResultVo.getDescription();
            String common_name = StringUtils.isBlank(repairHttpResultVo.getCommon_name()) == true ? null : repairHttpResultVo.getCommon_name();
            String common_price = StringUtils.isBlank(repairHttpResultVo.getCommon_price()) == true ? null : repairHttpResultVo.getCommon_price();
            String common_quantity = StringUtils.isBlank(repairHttpResultVo.getCommon_quantity()) == true ? null : repairHttpResultVo.getCommon_quantity();
            String common_unit = StringUtils.isBlank(repairHttpResultVo.getCommon_unit()) == true ? null : repairHttpResultVo.getCommon_unit();
            String common_total = StringUtils.isBlank(repairHttpResultVo.getCommon_total()) == true ? null : repairHttpResultVo.getCommon_total();

            //调内控采购流程接口
            HttpResultDto resultDto = repairDbService.auditDbRepair(repairHttpResultVo.getName(), repairHttpResultVo.getPhoneNum(),
                    title, detailid, repairHttpResultVo.getEtype(), description, common_name, common_price, common_quantity, common_unit, common_total);
            if (resultDto.getCode() == 1) {
                //成功，将内控的审批单号记录下来与我们的维修申请号绑定
                if (resultDto.getData() != null) {
                    Map repairDbMap = (Map) resultDto.getData();
                    if (repairDbMap.get("sano") != null) {
                        repair.setSano(String.valueOf(repairDbMap.get("sano")));
                    }
                }
            } else {
                throw new ServiceException("内控：" + resultDto.getMsg());
            }

            repairProcess.saveTwentyFourSubmitTask(repairWorkFlow);
            repairApplyMapper.updateById(repair);

            //发改委审批批示文件
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(repairFlowVo.getRepairId()));
            Update update = new Update();
            update.set("projectReviewDb", repairFlowVo.getBasicDBObject());
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("userId", repairFlowVo.getUserId());
            commentMap.put("step", StepNode.PROJECTREVIEW.getValue());
            update.set("projectReviewCommentDb", commentMap);
            mongoTemplate.upsert(query, update, "applyExtend");
        } catch (Exception ex) {
            log.error("projectReviewError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 机关管理局 内控采购流程审批
     *
     * @param repairHttpResultVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean cgApproval(ProcessPayHttpResultVo repairHttpResultVo) {
        try {
            RepairWorkFlow repairWorkFlow = new RepairWorkFlow();
            RepairApply repairApply = this.getOne(new QueryWrapper<RepairApply>().eq("sano", repairHttpResultVo.getSano()));

            if (repairApply != null) {
                repairWorkFlow.setRepairId(repairApply.getRepairApplyId());
                repairWorkFlow.setProcessInstanceId(repairApply.getProcessInstanceId());
            } else {
                throw new ServiceException("所传事前审批单据号不存在！");
            }
            repairApply.setStep(StepNode.PROJECTLEADER.getValue());

            repairWorkFlow.setAuditState(repairHttpResultVo.getAuditState());
            repairWorkFlow.setCurrentUser(repairHttpResultVo.getCurrentUser());

            List<String> userIds = authorityUnitService.selectRepairByStep(StepNode.PROJECTLEADER.getValue());
            //设置项目负责办理人
            if (CollectionUtils.isNotEmpty(userIds)) {
                repairWorkFlow.setLeaderUserIds(userIds);
            } else {
                throw new ServiceException("没有指定项目负责办理人！");
            }

            repairProcess.saveElevenSubmitTask(repairWorkFlow);
            repairApplyMapper.updateById(repairApply);

            //第二步，审批流程结束，调用付款接口,添加付款信息

            if (CollectionUtils.isNotEmpty(repairHttpResultVo.getRepairPaymentNodes())) {
                List<RepairPayment> paymentNodeVoList = repairHttpResultVo.getRepairPaymentNodes().stream().map(paymentNodeVo -> {
                    RepairPayment repairPayment = dozerBeanMapper.map(paymentNodeVo, RepairPayment.class);
                    repairPayment.setRepairPaymentState(PayMentState.NOTPAY.getValue());
                    repairPayment.setRepairApplyId(repairApply.getRepairApplyId());
                    return repairPayment;
                }).collect(Collectors.toList());
                repairPaymentService.saveBatch(paymentNodeVoList);
            }
            return true;
        } catch (Exception ex) {
            log.error("cgApprovalError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 指定项目负责人
     *
     * @param repairWorkFlow
     * @param repairFlowVo
     * @param repair
     */
    @Transactional(rollbackFor = Exception.class)
    public void projectLeader(RepairWorkFlow repairWorkFlow, RepairFlowVo repairFlowVo, RepairApply repair) {
        try {
            repair.setStep(StepNode.PCMANAGEMENT.getValue());
            ProcessInstance pi = repairProcess.saveTwentyFiveSubmitTask(repairWorkFlow);
            if (pi == null) {
                //审批通过，流程结束
                repair.setApplyStatus(ApplyStatusEnum.AUDITPASS.getValue());
            }
            repairApplyMapper.updateById(repair);

            if (CollectionUtils.isNotEmpty(repairFlowVo.getProjectLeaders())) {
                for (Long projectLeader : repairFlowVo.getProjectLeaders()) {
                    RepairPm repairPm = new RepairPm();
                    repairPm.setRepairApplyId(repair.getRepairApplyId()).setRepairPmUserid(projectLeader);
                    repairPmMapper.insert(repairPm);
                }
            } else {
                throw new ServiceException("请指定项目负责办理人！");
            }
        } catch (Exception ex) {
            log.error("projectLeaderError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除公共服务器上的维修图片方法
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteRepairImg(RepairImg repairImg) throws Exception {
        try {
            repairImgMapper.deleteById(repairImg);
            String repairImgUrl = repairImg.getPhysicalAddress();
            // 图片的文件路径
            String imagePath = repairImgUrl;
            if (StringUtils.isNotEmpty(imagePath)) {
                synchronized (this) {
                    Files.deleteIfExists(Paths.get(imagePath));
                }
            }
        } catch (Exception ex) {
            log.error("deleteRepairImgError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除公共服务器上的维修申请资料方法
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteRepairData(String physicalAddress) throws Exception {
        try {
            // 文件路径
            String dataPath = url + physicalAddress;
            if (StringUtils.isNotEmpty(dataPath)) {
                synchronized (this) {
                    Files.deleteIfExists(Paths.get(dataPath));
                }
            }
        } catch (Exception ex) {
            log.error("deleteRepairDataError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 维修项目详情
     *
     * @param id
     * @return
     */
    @Override
    public List<RepairProjectDto> projectDetail(Long id) {
        return repairApplyMapper.projectDetail(id);
    }


    /**
     * 领导审批
     *
     * @param leaderReviewVo
     * @return
     */
    @Override
    public List<Document> leaderReviewDetail(LeaderReviewVo leaderReviewVo) {
        try {
            //流程号
            Integer step = leaderReviewVo.getStep();
            //维修申请Id
            Long repairApplyId = leaderReviewVo.getRepairApplyId();
            List<Document> applyExtend = null;
            Query query = new Query();

            //业务科室审批
            if (step.equals(SelectStepNode.AODEPARTMENTS.getValue())) {
                query.fields().include("busApprovalCommentDb.busApprovalComment")
                        .include("busApprovalCommentDb.busApprovalUser")
                        .include("busApprovalCommentDb.busApprovalTime")
                        .include("busApprovalCommentDb.busApprovalAuditState")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(repairApplyId));
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");

                //线下党组会审批
            } else if (step.equals(SelectStepNode.EXAPPROVAL.getValue())) {
                query.fields().include("dzhApprovalDb.repairApprovalDataList.physicalAddress")
                        .include("dzhApprovalDb.repairApprovalDataList.repairFileName")
                        .include("dzhApprovalDb.repairApprovalDataList.type")
                        .include("dzhApprovalCommentDb.dzhApprovalComment")
                        .include("dzhApprovalCommentDb.dzhApprovalTime")
                        .include("dzhApprovalCommentDb.dzhApprovalUser")
                        .include("dzhApprovalCommentDb.dzhApprovalAuditState")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(repairApplyId));
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");

                //自费维修上传维修方案
            } else if (step.equals(SelectStepNode.UMSCHEME.getValue())) {
                query.fields().include("repairPlanDb.repairApprovalDataList.physicalAddress")
                        .include("repairPlanDb.repairApprovalDataList.repairFileName")
                        .include("repairPlanDb.repairApprovalDataList.type")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(repairApplyId));
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");

                //审批维修方案
            } else if (step.equals(SelectStepNode.EXMPAPPROVE.getValue())) {
                query.fields().include("aRepairPlanCommentDb.aRepairPlanComment")
                        .include("aRepairPlanCommentDb.aRepairPlanUser")
                        .include("aRepairPlanCommentDb.aRepairPlanTime")
                        .include("aRepairPlanCommentDb.aRepairPlanAuditState")
                        .include("repairPlanDb.repairApprovalDataList.physicalAddress")
                        .include("repairPlanDb.repairApprovalDataList.repairFileName")
                        .include("repairPlanDb.repairApprovalDataList.type")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(repairApplyId));
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");

                //维修完成上传资料 ,竣工验收
            } else if (step.equals(SelectStepNode.UDAMAINTENANCE.getValue()) || step.equals(StepNode.CNACCEPTANCE.getValue())) {
                query.fields().include("constructManageDb.comment")
                        .include("constructManageDb.buildDrawings.physicalAddress")
                        .include("constructManageDb.buildDrawings.virtualAddress")
                        .include("constructManageDb.buildDrawings.repairFileName")
                        .include("constructManageDb.buildDrawings.enclosureType")
                        .include("constructManageDb.auditReport.physicalAddress")
                        .include("constructManageDb.auditReport.virtualAddress")
                        .include("constructManageDb.auditReport.repairFileName")
                        .include("constructManageDb.auditReport.enclosureType")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(repairApplyId));
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");

                //项目方案设计可行性研究
            } else if (step.equals(SelectStepNode.FSDESIGN.getValue())) {
                query.fields().include("researchApprovalDb.repairApprovalDataList.physicalAddress")
                        .include("researchApprovalDb.repairApprovalDataList.virtualAddress")
                        .include("researchApprovalDb.repairApprovalDataList.repairFileName")
                        .include("researchApprovalDb.repairApprovalDataList.name")
                        .include("researchApprovalDb.repairApprovalDataList.type")
                        .include("researchApprovalDb.projectIntroduction")
                        .include("rhaCommentDb.rhaComment")
                        .include("rhaCommentDb.rhaAuditState")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(repairApplyId));
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");

                //领导审批
            } else if (step.equals(SelectStepNode.LEADERAPPROVAL.getValue())) {
                query.fields().include("ldaDb.repairApprovalDataList.physicalAddress")
                        .include("ldaDb.repairApprovalDataList.virtualAddress")
                        .include("ldaDb.repairApprovalDataList.repairFileName")
                        .include("ldaDb.repairApprovalDataList.name")
                        .include("ldaDb.repairApprovalDataList.type")
                        .include("ldaCommentDb.ldaComment")
                        .include("ldaCommentDb.ldaAuditState")
                        .include("ldaCommentDb.ldaTime")
                        .include("ldaCommentDb.ldaUser")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(repairApplyId));
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");

                //进度付款()
            } else if (step.equals(SelectStepNode.PROGRESSPAYMENT.getValue())) {
                query.fields().include("repairPlanDb.repairApprovalDataList.physicalAddress").exclude("_id");
                query.addCriteria(Criteria.where("_id").is(repairApplyId));
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");

                //自费维修上传预算方案
            } else if (step.equals(SelectStepNode.USMAINTENANCE.getValue())) {
                query.fields().include("ysRepairPlanDb.repairApprovalDataList.physicalAddress")
                        .include("ysRepairPlanDb.repairApprovalDataList.repairFileName")
                        .include("ysRepairPlanDb.repairApprovalDataList.type")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(repairApplyId));
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");

                //审批预算方案
            } else if (step.equals(SelectStepNode.BPAPPROVAL.getValue())) {
                query.fields().include("ysRepairPlanDb.repairApprovalDataList.physicalAddress")
                        .include("ysRepairPlanDb.repairApprovalDataList.repairFileName")
                        .include("ysRepairPlanDb.repairApprovalDataList.type")
                        .include("ysPlanCommentDb.ysPlanComment")
                        .include("ysPlanCommentDb.ysPlanUser")
                        .include("ysPlanCommentDb.ysPlanTime")
                        .include("ysPlanCommentDb.ysPlanAuditState").exclude("_id");
                query.addCriteria(Criteria.where("_id").is(repairApplyId));
                applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");
            }
            return MongoDbUtil.DocumentToMap(applyExtend).stream().allMatch(MapUtils::isEmpty) ? null : applyExtend;
        } catch (Exception ex) {
            log.error("leaderReviewDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询政府或发改委审批
     *
     * @param repairApplyId
     * @return
     */
    @Override
    public List<Document> goverReviewDetail(Long repairApplyId) {
        try {
            Query query = new Query();
            query.fields().include("galCommentDb.galComment")
                    .include("galCommentDb.galUser")
                    .include("galCommentDb.galTime")
                    .include("galCommentDb.galAuditState")
                    .include("governmentApprovalDb.repairApprovalDataList.physicalAddress")
                    .include("governmentApprovalDb.repairApprovalDataList.repairFileName")
                    .include("governmentApprovalDb.repairApprovalDataList.type")
                    .exclude("_id");
            query.addCriteria(Criteria.where("_id").is(repairApplyId));
            List<Document> applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");
            return MongoDbUtil.DocumentToMap(applyExtend).stream().allMatch(MapUtils::isEmpty) ? null : applyExtend;
        } catch (Exception ex) {
            log.error("goverReviewDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询项目施工设计
     *
     * @param repairApplyId
     * @return
     */
    @Override
    public DesignReviewDto designReviewDetail(Long repairApplyId) {
        try {
            DesignReviewDto designReviewDto = new DesignReviewDto();
            RepairApply repairApply = repairApplyMapper.selectById(repairApplyId);
            //设计单位
            String designUnit = repairApply.getDesignUnit();
            //规划许可证
            String planningPermit = repairApply.getPlanningPermit();
            Query query = new Query();
            query.fields().include("detailDesignDb.repairApprovalDataList.physicalAddress")
                    .include("detailDesignDb.repairApprovalDataList.virtualAddress")
                    .include("detailDesignDb.repairApprovalDataList.repairFileName")
                    .include("detailDesignDb.repairApprovalDataList.type")
                    .include("detailDesignDb.repairApprovalDataList.name")
                    .exclude("_id");
            query.addCriteria(Criteria.where("_id").is(repairApplyId));
            List<Document> applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");
            designReviewDto.setDocumentList(applyExtend);
            designReviewDto.setDesignUnit(designUnit);
            designReviewDto.setPlanningPermit(planningPermit);
            return designReviewDto;
        } catch (Exception ex) {
            log.error("designReviewDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询竣工验收信息
     *
     * @param repairApplyId
     * @return
     */
    @Override
    public CompletedProjectDto completedProject(Long repairApplyId) {
        try {
            CompletedProjectDto completedProjectDto = new CompletedProjectDto();
            List<RepairProjectDto> repairProjectDtos = this.projectDetail(repairApplyId);
            completedProjectDto.setRepairProjectDtoList(repairProjectDtos);
            Query query = new Query();
            query.fields().include("constructManageDb.comment")
                    .include("constructManageDb.constructionUnit")
                    .include("constructManageDb.applicantUserName")
                    .include("constructManageDb.phone")
                    .include("constructManageDb.startTime")
                    .include("constructManageDb.buildDrawings.physicalAddress")
                    .include("constructManageDb.buildDrawings.virtualAddress")
                    .include("constructManageDb.buildDrawings.repairFileName")
                    .include("constructManageDb.buildDrawings.enclosureType")
                    .include("constructManageDb.auditReport.physicalAddress")
                    .include("constructManageDb.auditReport.virtualAddress")
                    .include("constructManageDb.auditReport.repairFileName")
                    .include("constructManageDb.auditReport.enclosureType")
                    .exclude("_id");
            query.addCriteria(Criteria.where("_id").is(repairApplyId));
            List<Document> applyExtend = mongoTemplate.find(query, Document.class, "applyExtend");
            completedProjectDto.setDocumentList(applyExtend);
            return completedProjectDto;
        } catch (Exception ex) {
            log.error("completedProjectError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 更新用户维修权限
     *
     * @param activityAuthorityVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRepairAuthority(ActivityAuthorityVo activityAuthorityVo) {
        try {
            //向组任务中添加成员
            if (CollectionUtils.isNotEmpty(activityAuthorityVo.getSteps())) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.in("step", activityAuthorityVo.getSteps());
                List<RepairApply> repairApplyList = repairApplyMapper.selectList(queryWrapper);
                if (CollectionUtils.isNotEmpty(repairApplyList)) {
                    for (RepairApply repairApply : repairApplyList) {
                        if (StringUtils.isNotBlank(repairApply.getProcessInstanceId())) {
                            repairProcess.addGroupUser(repairApply.getProcessInstanceId(), activityAuthorityVo.getUserId());
                        }
                    }
                }
            }

            //从组任务中删除成员
            if (CollectionUtils.isNotEmpty(activityAuthorityVo.getRemoveSteps())) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.in("step", activityAuthorityVo.getRemoveSteps());
                List<RepairApply> repairApplyList = repairApplyMapper.selectList(queryWrapper);
                if (CollectionUtils.isNotEmpty(repairApplyList)) {
                    for (RepairApply repairApply : repairApplyList) {
                        if (StringUtils.isNotBlank(repairApply.getProcessInstanceId())) {
                            repairProcess.deleteGroupUser(repairApply.getProcessInstanceId(), activityAuthorityVo.getUserId());
                        }
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("updateRepairAuthorityError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 维修项目信息添加
     *
     * @param constructManageVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean constructManage(ConstructManageVo constructManageVo) {
        try {
            Long repairApplyId = constructManageVo.getRepairApplyId();
            List<RepairModule> repairModuleList = repairModuleMapper.selectList(new QueryWrapper<RepairModule>().eq("repair_apply_id", repairApplyId));
            RepairModule repairModule = new RepairModule();
            repairModule.setIsRepair(2);
            UpdateWrapper<RepairModule> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("repair_apply_id", repairApplyId);
            updateWrapper.eq("is_repair", 0);
            //更新维修模块,把是否维修状态改为已维修
            repairModuleMapper.update(repairModule, updateWrapper);
            RepairApply repairApply = new RepairApply();
            repairApply.setRepairApplyId(repairApplyId);
            //走机关管理局
            if (repairApplyMapper.selectById(repairApplyId).getRepairType().equals(2)) {
                //完成流程竣工验收--到17
                repairApply.setStep(StepNode.QWPERIOD.getValue());
                repairApply.setCompletionTime(new Date());
                //更新维修申请流程号,竣工时间
                repairApplyMapper.updateById(repairApply);
            }
            if (CollectionUtils.isNotEmpty(repairModuleList)) {
                for (RepairModule module : repairModuleList) {
                    Long repairModuleId = module.getRepairModuleId();
                    Long repairPartId = module.getRepairPartId();
                    //查询部位
                    RepairParts repairParts = repairPartsMapper.selectById(repairPartId);
                    RepairModule repairModu = new RepairModule();
                    repairModu.setRepairModuleId(repairModuleId);
                    //获取部位质保期
                    Integer repairPartQuality = repairParts.getRepairPartQuality();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    //质保期加当前日期,即质保日期
                    calendar.add(Calendar.YEAR, repairPartQuality);
                    Date newDate = calendar.getTime();
                    repairModu.setRepairQuality(newDate);
                    //修改维修模块质保日期
                    repairModuleMapper.updateById(repairModu);
                    //设置参数到维修提醒中
                    RepairRemind repairRemind = dozerBeanMapper.map(module, RepairRemind.class);
                    repairRemind.setPosition(module.getPositionStr());
                    repairRemind.setRepairPart(repairPartId.intValue());
                    repairRemind.setRepairGoods(module.getRepairModuleTarget());
                    repairRemind.setRemaindTime(newDate);
                    repairRemind.setRemaindHandle(1);
                    //新增维修提醒
                    repairRemindMapper.insert(repairRemind);
                }
            }
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(repairApplyId));
            Update update = new Update();
            update.set("constructManageDb", constructManageVo.getConstructObject());
            mongoTemplate.upsert(query, update, "applyExtend");
            return true;
        } catch (Exception ex) {
            log.error("constructManageError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 维修类型报表查询
     *
     * @param repairReportVo
     * @return
     */
    @Override
    public List<RepairReportDto> typeReport(RepairReportVo repairReportVo) {
        return repairApplyMapper.typeReport(repairReportVo);
    }

    /**
     * 查询某个用户所有审批记录（该用户审批过的维修申请）
     *
     * @param approvalRecordVo
     * @return
     */
    @Override
    public Page<RepairApplyDto> selectApprovalRecord(ApprovalRecordVo approvalRecordVo) {
        try {
            List<Long> repairApplyIdList = new ArrayList<>();
            //查询mongo中所有数据
            List<Document> applyExtend = mongoTemplate.findAll(Document.class, "applyExtend");
            if (CollectionUtils.isNotEmpty(applyExtend)) {
                for (Document document : applyExtend) {
                    Set<String> keySet = document.keySet();
                    for (String key : keySet) {
                        if (!(key.equals("_id"))) {
                            //获取key对应值
                            Map<String, Object> map = (Map<String, Object>) document.get(key);
                            Long userId = null;
                            if (map != null && map.containsKey("userId")) {
                                userId = (Long) map.get("userId");
                            }
                            if (userId != null && userId.equals(approvalRecordVo.getUserId())) {
                                //获取维修申请Id
                                Long repairApplyId = (Long) document.get("_id");
                                repairApplyIdList.add(repairApplyId);
                            }
                        }
                    }
                }
            }
            Page<RepairApplyDto> page = new Page<>(approvalRecordVo.getCurrentPage(), approvalRecordVo.getPageSize());
            return repairApplyMapper.selectApprovalRecord(page, approvalRecordVo.getUserId(), repairApplyIdList);
        } catch (Exception ex) {
            log.error("selectApprovalRecordError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 维修查看列表
     *
     * @param repairListVo
     * @return
     */
    @Override
    public Page<RepairApplyDto> leaderReviewList(RepairListVo repairListVo) {
        try {
            Page<RepairApplyDto> page = new Page<>(repairListVo.getCurrentPage(), repairListVo.getPageSize());
            Page<RepairApplyDto> repairApplyDtoPage = repairApplyMapper.leaderReviewList(page, repairListVo);
            List<RepairApplyDto> records = repairApplyDtoPage.getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                for (RepairApplyDto repairApplyDto : records) {
                    Long repairApplyId = repairApplyDto.getRepairApplyId();
                    List<String> repairTargetList = repairApplyMapper.selectTargetList(repairApplyId);
                    repairApplyDto.setRepairTargetList(repairTargetList);
                }
            }
            return repairApplyDtoPage;
        } catch (Exception ex) {
            log.error("leaderReviewListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询某个用户审批详情(这条记录我审批过的步骤)
     *
     * @param approvalDetailVo
     * @return
     */
    @Override
    public List<StepNodeDto> selectApprovalDetail(ApprovalDetailVo approvalDetailVo) {
        try {
            List<Integer> approvalIdList = new ArrayList<>();
            List<StepNodeDto> stepNodelist = new ArrayList<>();
            Document document = mongoTemplate.findById(approvalDetailVo.getRepairApplyId(), Document.class, "applyExtend");
            if (document != null) {
                Set<String> keySet = document.keySet();
                for (String key : keySet) {
                    if (!(key.equals("_id"))) {
                        //获取key对应值
                        Map<String, Object> map = (Map<String, Object>) document.get(key);
                        if (map != null && map.containsKey("userId")) {
                            Long userId1 = (Long) map.get("userId");
                            if (userId1.equals(approvalDetailVo.getUserId())) {
                                Integer step = (Integer) map.get("step");
                                approvalIdList.add(step);
                            }
                        }
                    }
                }
            }
            List<StepNodeDto> stepNodeList = Arrays.stream(StepNode.values()).map(p -> new StepNodeDto(p.getValue(), p.getName())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(approvalIdList)) {
                for (Integer approvalId : approvalIdList) {
                    for (StepNodeDto stepNodeDto : stepNodeList) {
                        if (stepNodeDto.getValue().equals(approvalId)) {
                            stepNodelist.add(stepNodeDto);
                        }
                    }
                }
            }
            return stepNodelist;
        } catch (Exception ex) {
            log.error("selectApprovalDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询某栋楼关联的维修申请
     *
     * @param buildRepairVo
     * @return
     */
    @Override
    public List<BuildRepairApplyDto> buildRepairApplyList(BuildRepairVo buildRepairVo) {
        try {

            return repairApplyMapper.buildRepairApplyList(buildRepairVo);
        } catch (Exception ex) {
            log.error("buildRepairApplyListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询某条维修申请的所有审批文件
     *
     * @param repairApplyId
     * @return
     */
    @Override
    public List<RepairApplyFilesDto> repairApplyFiles(Long repairApplyId) {
        try {
            List<RepairApplyFilesDto> resList = new ArrayList<>();
            Document document = mongoTemplate.findById(repairApplyId, Document.class, "applyExtend");
            if (document != null) {
                Set<String> keySet = document.keySet();
                for (String key : keySet) {
                    if (!(key.equals("_id") || key.equals("constructManageDb"))) {
                        //获取key对应值
                        Map<String, Object> map = (Map<String, Object>) document.get(key);
                        RepairApplyFilesDto repairApplyFilesDto = new RepairApplyFilesDto();
                        if (map != null && map.containsKey("repairApprovalDataList")) {
                            List<Object> fileMap = (List<Object>) map.get("repairApprovalDataList");
                            //设置文件集合对应key
                            repairApplyFilesDto.setFileKey(key);
                            repairApplyFilesDto.setFileList(fileMap);
                            resList.add(repairApplyFilesDto);
                        }
                    } else if (key.equals("constructManageDb")) {

                        Map<String, Object> map = (Map<String, Object>) document.get(key);
                        if (map != null && (map.containsKey("buildDrawings") || map.containsKey("auditReport"))) {
                            List<Object> fileMap1 = (List<Object>) map.get("buildDrawings");
                            List<Object> fileMap2 = (List<Object>) map.get("auditReport");
                            if (CollectionUtils.isNotEmpty(fileMap1)) {
                                RepairApplyFilesDto repairApplyFilesDto = new RepairApplyFilesDto();
                                repairApplyFilesDto.setFileKey("buildDrawings");
                                repairApplyFilesDto.setFileList(fileMap1);
                                resList.add(repairApplyFilesDto);
                            }
                            if (CollectionUtils.isNotEmpty(fileMap2)) {
                                RepairApplyFilesDto repairApplyFilesDto = new RepairApplyFilesDto();
                                repairApplyFilesDto.setFileKey("auditReport");
                                repairApplyFilesDto.setFileList(fileMap2);
                                resList.add(repairApplyFilesDto);
                            }
                        }
                    }
                }
            }
            return resList;
        } catch (Exception ex) {
            log.error("repairApplyFilesError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询维修档案列表
     *
     * @param buildDto
     * @return
     */
    @Override
    public Page<RepairFilesDto> repairFilesList(BuildAndYardVo buildAndYardVo) {
        try {
            Page<RepairFilesDto> page = new Page<>(buildAndYardVo.getCurrentPage(), buildAndYardVo.getPageSize());
            return repairApplyMapper.repairFilesList(page, buildAndYardVo);
        } catch (Exception ex) {
            log.error("repairFilesListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 获取内控采购流程完成步骤集合
     *
     * @param repairApplyId
     * @return
     */
    @Override
    public List<LinkedHashMap<String, Object>> getStepList(Long repairApplyId) {
        try {
            //获取内控审批单号
            String sano = getSano(repairApplyId);
            if (StringUtils.isNotEmpty(sano)) {

                HttpResultDto resultDto = repairDbService.getStepList(sano);
                if (resultDto.getCode() == 0) {
                    throw new ServiceException("内控：" + resultDto.getMsg());
                }
                //获取内控返回的data数据
                List<LinkedHashMap<String, Object>> data = (List<LinkedHashMap<String, Object>>) resultDto.getData();
                List<LinkedHashMap<String, Object>> resList = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(data)) {
                    //遍历出符合条件的步骤
                    for (LinkedHashMap<String, Object> linkedHashMap : data) {
                        String stepTags = (String) linkedHashMap.get("stepTags");
                        if (stepTags.equals("PREREPORT_APPROVAL") || stepTags.equals("PURCHASE_APPROVAL") || stepTags.equals("PURCHASE_RECORD") ||
                                stepTags.equals("PURCHASE_UPFILE") || stepTags.equals("PURCHASE_RESULT") || stepTags.equals("CONTRACT_RECORD")) {
                            resList.add(linkedHashMap);
                        }
                    }
                }
                return resList;
            } else {
                return null;
            }
        } catch (Exception ex) {
            log.error("getStepListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 获取内控采购流程步骤详情
     *
     * @param dbStepDetailVo
     * @return
     */
    @Override
    public Object dbStepDetail(DbStepDetailVo dbStepDetailVo) {
        try {
            //获取内控审批单号
            String sano = getSano(dbStepDetailVo.getRepairApplyId());
            String stepTags = dbStepDetailVo.getStepTags();
            if (StringUtils.isNotEmpty(sano)) {
                HttpResultDto stepInfo = repairDbService.getStepInfo(sano, stepTags);
                if (stepInfo.getCode() == 0) {
                    throw new ServiceException("内控：" + stepInfo.getMsg());
                }
                return stepInfo.getData();
            } else {
                return null;
            }
        } catch (Exception ex) {
            log.error("dbStepDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 获取内控采购流程所有审批文件
     *
     * @param dbStepDetailVo
     * @return
     */
    @Override
    public Object dbAllFiles(Long repairApplyId) {
        try {
            //获取内控审批单号
            String sano = getSano(repairApplyId);
            if (StringUtils.isNotEmpty(sano)) {
                HttpResultDto allFile = repairDbService.getAllFile(sano);
                if (allFile.getCode() == 0) {
                    throw new ServiceException("内控：" + allFile.getMsg());
                }
                return allFile.getData();
            } else {
                return null;
            }
        } catch (Exception ex) {
            log.error("dbAllFilesError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 获取内控预算事项科室预算明细
     *
     * @param budgetDetailsVo
     * @return
     */
    @Override
    public List<Object> getSectionList(BudgetDetailsVo budgetDetailsVo) {
        try {
            List<Object> sectionList = new ArrayList<>();
            String name = budgetDetailsVo.getName();
            String phoneNum = budgetDetailsVo.getPhoneNum();
            String search = StringUtils.isNoneBlank(budgetDetailsVo.getSearch()) == false ? null : budgetDetailsVo.getSearch();
            HttpResultDto resultDto = repairDbService.getSectionList(name, phoneNum, search);
            if (resultDto.getCode() == 1) {
                if (resultDto.getData() != null) {
                    sectionList = (List<Object>) resultDto.getData();
                }
            } else {
                throw new ServiceException("内控：" + resultDto.getMsg());
            }
            return sectionList;
        } catch (Exception ex) {
            log.error("getSectionListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 获取内控审批单号公共方法
     *
     * @param repairApplyId
     * @return
     */
    private String getSano(Long repairApplyId) {
        RepairApply repairApply = repairApplyMapper.selectById(repairApplyId);
        if (repairApply == null) {
            throw new ServiceException("该维修申请不存在！");
        }
        return repairApply.getSano();
    }
}
