package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.*;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.*;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IConfigureService;
import com.ryzw.housemanager.service.IRepairDbService;
import com.ryzw.housemanager.util.MongoDbUtil;
import com.ryzw.housemanager.vo.*;
import com.ryzw.houseprocess.ConfigureProcess;
import com.ryzw.houseprocess.ConfigureWorkFlow;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 配置表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
@Service
@Slf4j
public class ConfigureServiceImpl extends ServiceImpl<ConfigureMapper, Configure> implements IConfigureService {

    @Autowired
    private ConfigureMapper configureMapper;

    @Autowired
    private UnitMapper unitMapper;

    @Autowired
    private IAuthorityUnitService authorityUnitService;

    @Autowired
    private LeadingOfficeMapper leadingOfficeMapper;

    @Autowired
    private ConfigureProcess configureProcess;

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Autowired
    private ConfigureRegionMapper configureRegionMapper;

    @Autowired
    private ConfigureRoomMapper configureRoomMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IRepairDbService repairDbService;

    @Autowired
    private ConfigurePlanDataMapper configurePlanDataMapper;

    @Autowired
    private ConfigurePlanOldUnitMapper configurePlanOldUnitMapper;

    @Autowired
    private ConfigureEnclosureMapper configureEnclosureMapper;

    @Value("${uploadUrl.url}")
    private String url;

    @Autowired
    private YardServiceImpl yardService;

    @Autowired
    private BuildServiceImpl buildService;

    @Autowired
    private ConfigureYardMapper configureYardMapper;

    @Autowired
    private ConfigureBuildMapper configureBuildMapper;

    @Autowired
    private ConfigurePlanChangeDataMapper configurePlanChangeDataMapper;


    /**
     * 新增配置申请
     *
     * @param configureAddVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addConfigure(ConfigureAddVo configureAddVo) {
        try {
            Configure configure = dozerBeanMapper.map(configureAddVo, Configure.class);
            //远程调用getUserByToken
            Map<String, Object> userByToken = authorityUnitService.getUserByToken();
            Integer unitId = (Integer) userByToken.get("unit_id");
            Unit unit = unitMapper.selectById(unitId);
            //配置编号
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
            String msg = sdf.format(date);
            msg += configureAddVo.getConfigureApplicantId();
            configure.setConfigureNo("PZ" + msg);
            //配置申请时间
            configure.setConfigureDate(date);
            //单位编号
            configure.setUnitId(unitId);
            //当前流程
            configure.setStep(ConfigureStepNode.UNITREVIEW.getValue());
            //申请单位名称
            configure.setUnitName(unit.getUnitName());
            //新增配置申请
            configureMapper.insert(configure);
            //获取新增后的配置编号
            Long configureId = configure.getConfigureId();
            if (CollectionUtils.isNotEmpty(configureAddVo.getEnclosureList())) {
                for (ConfigureEnclosure configureEnclosure : configureAddVo.getEnclosureList()) {
                    configureEnclosure.setConfigureId(configureId);
                    //新增配置附件
                    configureEnclosureMapper.insert(configureEnclosure);
                }
            }
            //按step查询用户id
            List<String> userIds = authorityUnitService.selectConfigureByStep(ConfigureStepNode.UNITREVIEW.getValue());
            if (CollectionUtils.isNotEmpty(userIds)) {
                //启动工作流,获取流程实例ID
                String processInstanceId = configureProcess.startConfigureProcess(configure.getConfigureId(), configureAddVo.getConfigureApplicant(), configureAddVo.getConfigureApplicantId(), userIds);
                //更新维修申请表里面的流程实例ID字段值
                configure.setProcessInstanceId(processInstanceId);
                configureMapper.updateById(configure);
            } else {
                throw new ServiceException("没有指定机关管理局用房情况审查办理人！");
            }
            return true;
        } catch (Exception ex) {
            log.error("addConfigureError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询配置申请基础数据
     *
     * @param configureId
     * @return
     */
    @Override
    public ConfigureDto basicConfigure(Long configureId) {
        try {
            ConfigureDto configureDto = new ConfigureDto();
            Integer unitId = null;
            if (configureId != null) {
                Configure configure = configureMapper.selectById(configureId);
                unitId = configure.getUnitId();
                Unit unit = unitMapper.selectById(unitId);
                if (unit != null) {
                    if (StringUtils.isNotBlank(configure.getUnitName())) {
                        String unitName = configure.getUnitName();
                        unit.setUnitName(unitName);
                    }
                    configureDto = dozerBeanMapper.map(unit, ConfigureDto.class);
                }


            } else {
                //远程调用getUserByToken
                Map<String, Object> userByToken = authorityUnitService.getUserByToken();
                unitId = (Integer) userByToken.get("unit_id");
                Unit unit = unitMapper.selectById(unitId);
                //第一次提申请,设置参数
                if (unit != null) {
                    configureDto = dozerBeanMapper.map(unit, ConfigureDto.class);
                }
            }
            //查询实有人数
            Integer acuNum = leadingOfficeMapper.selectCount(new QueryWrapper<LeadingOffice>().eq("unit_id", unitId));
            configureDto.setActualNumber(acuNum);
            //查询总面积
            Float totalArea = configureMapper.selectTotalArea(unitId);
            configureDto.setTotalArea(totalArea);
            return configureDto;
        } catch (Exception ex) {
            log.error("basicConfigureError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改配置申请
     *
     * @param configureAddVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfigure(ConfigureAddVo configureAddVo) {
        try {
            Long configureId = configureAddVo.getConfigureId();
            if (configureId != null) {
                Configure configure = dozerBeanMapper.map(configureAddVo, Configure.class);
                configure.setConfigureDate(new Date());
                //修改配置申请
                configureMapper.updateById(configure);
                if (CollectionUtils.isNotEmpty(configureAddVo.getDeleteEnclosure())) {
                    //删除服务器上附件
                    this.deleteConfigureEnclosure(configureAddVo.getDeleteEnclosure());
                    //删除原数据库配置附件
                    configureEnclosureMapper.delete(new QueryWrapper<ConfigureEnclosure>().in("configure_enclosure_id", configureAddVo.getDeleteEnclosure()));
                }
                if (CollectionUtils.isNotEmpty(configureAddVo.getEnclosureList())) {
                    for (ConfigureEnclosure configureEnclosure : configureAddVo.getEnclosureList()) {
                        configureEnclosure.setConfigureId(configureId);
                        //新增配置附件
                        configureEnclosureMapper.insert(configureEnclosure);
                    }
                }
                return true;
            } else {
                throw new ServiceException("处置ID不能为空！");
            }
        } catch (Exception ex) {
            log.error("updateConfigureError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 配置申请列表
     *
     * @param configureVo
     * @return
     */
    @Override
    public Page<ConfigureDto> configureList(ConfigureVo configureVo) {
        try {
            Page<ConfigureDto> page = new Page<>(configureVo.getCurrentPage(), configureVo.getPageSize());
            Page<ConfigureDto> configureDtoPage = configureMapper.configureList(page, configureVo);
            List<ConfigureDto> records = configureDtoPage.getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                for (ConfigureDto configureDto : records) {
                    Configure configure = configureMapper.selectById(configureDto.getConfigureId());
                    Integer unitId = configure.getUnitId();
                    //查询实有人数
                    Integer acuNum = leadingOfficeMapper.selectCount(new QueryWrapper<LeadingOffice>().eq("unit_id", unitId));
                    configureDto.setActualNumber(acuNum);
                }
            }
            return configureDtoPage;
        } catch (Exception ex) {
            log.error("configureListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 更新用户配置权限
     *
     * @param activityAuthorityVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfigureAuthority(@Valid ActivityAuthorityVo activityAuthorityVo) {
        try {
            //向组任务中添加成员
            if (CollectionUtils.isNotEmpty(activityAuthorityVo.getSteps())) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.in("step", activityAuthorityVo.getSteps());
                if (activityAuthorityVo.getSteps().contains(ConfigureStepNode.UNITREVIEW.getValue())) {
                    activityAuthorityVo.getSteps().add(ConfigureStepNode.IMPROVINGDATA.getValue());
                    queryWrapper.in("step", activityAuthorityVo.getSteps());
                }
                List<Configure> configureList = configureMapper.selectList(queryWrapper);
                if (CollectionUtils.isNotEmpty(configureList)) {
                    for (Configure configure : configureList) {
                        if (StringUtils.isNotBlank(configure.getProcessInstanceId())) {
                            configureProcess.addGroupUser(configure.getProcessInstanceId(), activityAuthorityVo.getUserId());
                        }
                    }
                }
            }

            //从组任务中删除成员
            if (CollectionUtils.isNotEmpty(activityAuthorityVo.getRemoveSteps())) {
                QueryWrapper queryWrapper = new QueryWrapper();
                if (activityAuthorityVo.getRemoveSteps().contains(ConfigureStepNode.UNITREVIEW.getValue())) {
                    activityAuthorityVo.getRemoveSteps().add(ConfigureStepNode.IMPROVINGDATA.getValue());
                }
                queryWrapper.in("step", activityAuthorityVo.getRemoveSteps());
                List<Configure> configureList = configureMapper.selectList(queryWrapper);
                if (CollectionUtils.isNotEmpty(configureList)) {
                    for (Configure configure : configureList) {
                        if (StringUtils.isNotBlank(configure.getProcessInstanceId())) {
                            configureProcess.deleteGroupUser(configure.getProcessInstanceId(), activityAuthorityVo.getUserId());
                        }
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("updateConfigureAuthorityError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 配置审批列表
     *
     * @param configureVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<ConfigureDto> configureApprovalList(ConfigureVo configureVo) {
        try {
            Page<ConfigureDto> page = new Page<>(configureVo.getCurrentPage(), configureVo.getPageSize());
            List<ConfigureWorkFlow> configureList = configureProcess.findTaskListById(configureVo.getUserId());
            List<Long> configureIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(configureList)) {
                for (ConfigureWorkFlow configureWorkFlow : configureList) {
                    Configure configure = new Configure();
                    configure.setConfigureId(configureWorkFlow.getConfigureId());
                    configure.setTaskId(configureWorkFlow.getTaskId());
                    configureMapper.updateById(configure);
                    configureIds.add(configureWorkFlow.getConfigureId());
                }
                configureVo.setUserId(null);
                configureVo.setConfigureIdList(configureIds);
                Page<ConfigureDto> configureDtoPage = configureMapper.configureList(page, configureVo);
                List<ConfigureDto> records = configureDtoPage.getRecords();

                if (CollectionUtils.isNotEmpty(records)) {
                    for (ConfigureDto configureDto : records) {
                        Configure configure = configureMapper.selectById(configureDto.getConfigureId());
                        Integer unitId = null;
                        if (configure.getUnitId() != null) {
                            unitId = configure.getUnitId();
                            Float totalArea = configureMapper.selectTotalArea(unitId);
                            configureDto.setTotalArea(totalArea);
                        }

                        Unit unit = unitMapper.selectById(unitId);
                        if (unit != null && unit.getUnitComplement() != null) {
                            configureDto.setUnitComplement(unit.getUnitComplement());
                        }

                        //查询实有人数
                        Integer acuNum = leadingOfficeMapper.selectCount(new QueryWrapper<LeadingOffice>().eq("unit_id", unitId));
                        configureDto.setActualNumber(acuNum);
                        if (configureDto.getTotalArea() != null && configureDto.getActualNumber() != null) {
                            if (!configureDto.getTotalArea().equals(0) && !configureDto.getActualNumber().equals(0)) {
                                configureDto.setPerArea((float) (Math.round(configureDto.getTotalArea() / configureDto.getActualNumber() * 100)) / 100);
                            }
                        }
                    }
                }
                return configureDtoPage;
            } else {
                return page;
            }
        } catch (Exception ex) {
            log.error("configureApprovalListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 审核申请审批
     *
     * @param configureFlowVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveSubmitConfigure(@Valid ConfigureFlowVo configureFlowVo) {
        try {
            ConfigureWorkFlow configureWorkFlow = dozerBeanMapper.map(configureFlowVo, ConfigureWorkFlow.class);

            Configure configure = new Configure();
            configure.setConfigureId(configureFlowVo.getConfigureId());

            //2.机关管理局用房情况审查
            if (configureFlowVo.getStep().equals(ConfigureStepNode.UNITREVIEW.getValue())) {
                this.unitReview(configureWorkFlow, configureFlowVo, configure);
            }

            //3.拟定分配方案
            if (configureFlowVo.getStep().equals(ConfigureStepNode.DUADPLAN.getValue())) {
                this.drawPlan(configureWorkFlow, configureFlowVo, configure);
            }

            //4.领导审批
            if (configureFlowVo.getStep().equals(ConfigureStepNode.LEADERAPPROVAL.getValue())) {
                this.configureLeadApproval(configureWorkFlow, configureFlowVo, configure);
            }

            //5.完善信息
            if (configureFlowVo.getStep().equals(ConfigureStepNode.IMPROVINGDATA.getValue())) {
                this.improvingData(configureWorkFlow, configureFlowVo, configure);
            }

            return true;
        } catch (Exception ex) {
            log.error("saveSubmitConfigureError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 机关管理局用房情况审查
     *
     * @param configureWorkFlow
     * @param configureFlowVo
     * @param configure
     */
    @Transactional(rollbackFor = Exception.class)
    public void unitReview(ConfigureWorkFlow configureWorkFlow, ConfigureFlowVo configureFlowVo, Configure configure) {
        try {
            //申请初审
            if (configureFlowVo.getAuditState().equals(1)) {
                configure.setStep(ConfigureStepNode.DUADPLAN.getValue());
                List<String> userIds = authorityUnitService.selectConfigureByStep(ConfigureStepNode.DUADPLAN.getValue());
                //设置拟定分配方案权限用户
                if (CollectionUtils.isNotEmpty(userIds)) {
                    configureWorkFlow.setDuPlanUserIds(userIds);
                } else {
                    throw new ServiceException("没有指定拟定分配方案办理人！");
                }

                //配置区域
                if (CollectionUtils.isNotEmpty(configureFlowVo.getRegionIds())) {
                    for (Integer regionId : configureFlowVo.getRegionIds()) {
                        ConfigureRegion configureRegion = new ConfigureRegion();
                        configureRegion.setConfigureId(configureFlowVo.getConfigureId()).setRegionId(regionId);
                        configureRegionMapper.insert(configureRegion);
                    }
                }

                //电梯台数
                if (configureFlowVo.getElevatorsNumber() != null) {
                    configure.setElevatorsNumber(configureFlowVo.getElevatorsNumber());
                }

                //重新分配单位
                if (configureFlowVo.getRedistributionUnitId() != null) {
                    configure.setRedistributionUnitId(configureFlowVo.getRedistributionUnitId());
                }

                //房间信息
                Float roomAreaSum = 0.00F;
                Float roomAreaSumMax = 0.00F;
                if (CollectionUtils.isNotEmpty(configureFlowVo.getConfigureRooms())) {
                    for (int i = 0; i < configureFlowVo.getConfigureRooms().size(); i++) {
                        ConfigureRoom configureRoom = configureFlowVo.getConfigureRooms().get(i);
                        configureRoom.setConfigureId(configureFlowVo.getConfigureId());
                        if (configureRoom.getRoomNumber() == null) {
                            throw new ServiceException("房间数量不能为空！");
                        }
                        if (configureRoom.getRoomAreaMin() == null) {
                            throw new ServiceException("房间面积最小值不能为空！");
                        }
                        if (configureRoom.getRoomAreaMax() == null) {
                            throw new ServiceException("房间面积最大值不能为空！");
                        }
                        if (configureRoom.getRoomAreaMax() <= configureRoom.getRoomAreaMin()) {
                            throw new ServiceException("房间面积范围的最大值不能小于等于最小值！");
                        }

                        if (i == 0) {
                            roomAreaSumMax = configureRoom.getRoomAreaMin() + configureRoom.getRoomAreaMax();
                        } else {
                            ConfigureRoom configureRoomOld = configureFlowVo.getConfigureRooms().get(i - 1);
                            roomAreaSumMax += configureRoomOld.getRoomAreaMax() + configureRoom.getRoomAreaMax();
                        }
                        roomAreaSum += configureRoom.getRoomAreaMin() + configureRoom.getRoomAreaMax();
                        configureRoomMapper.insert(configureRoom);
                    }
                    if (roomAreaSum < roomAreaSumMax) {
                        throw new ServiceException("房间面积范围不能重复！");
                    }
                } else {
                    throw new ServiceException("配置房间表信息不能为空！");
                }
            }

            ProcessInstance pi = configureProcess.saveTwoSubmitTask(configureWorkFlow);
            if (pi == null) {
                //审批不通过
                if (configureFlowVo.getAuditState().equals(2)) {
                    configure.setApplyStatus(ApplyStatusEnum.AUDITFAILED.getValue());
                }
            } else {
                //审核中
                configure.setApplyStatus(ApplyStatusEnum.INAUDIT.getValue());
            }
            configureMapper.updateById(configure);

            //单位审查
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(configureFlowVo.getConfigureId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("unitReviewAuditState", configureFlowVo.getAuditState());
            commentMap.put("unitReviewUser", configureFlowVo.getUserName());
            commentMap.put("unitReviewTime", new Date());
            if (StringUtils.isNotBlank(configureFlowVo.getComment())) {
                commentMap.put("unitReviewComment", configureFlowVo.getComment());
            } else {
                if (configureFlowVo.getAuditState().equals(2) && StringUtils.isBlank(configureFlowVo.getComment())) {
                    throw new ServiceException("审批意见不能为空！");
                }
            }
            commentMap.put("userId", configureFlowVo.getUserId());
            commentMap.put("step", ConfigureStepNode.UNITREVIEW.getValue());
            update.set("unitReviewDb", commentMap);
            mongoTemplate.upsert(query, update, "configure");
        } catch (Exception ex) {
            log.error("unitReviewError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 拟定分配方案
     *
     * @param configureWorkFlow
     * @param configureFlowVo
     * @param configure
     */
    @Transactional(rollbackFor = Exception.class)
    public void drawPlan(ConfigureWorkFlow configureWorkFlow, ConfigureFlowVo configureFlowVo, Configure configure) {
        try {
            if (configureFlowVo.getAllocationPlan() != null) {
                configure.setAllocationPlan(configureFlowVo.getAllocationPlan());

                //调剂
                if (configureFlowVo.getAllocationPlan().equals(1)) {
                    if (configureFlowVo.getSelectionPlan() != null) {
                        configure.setSelectionPlan(configureFlowVo.getSelectionPlan());
                        //智能分配
                        if (configureFlowVo.getSelectionPlan().equals(1)) {
                            if (CollectionUtils.isNotEmpty(configureFlowVo.getConfigurePlanDataList())) {
                                for (ConfigurePlanData configurePlanData : configureFlowVo.getConfigurePlanDataList()) {
                                    configurePlanData.setConfigureId(configureFlowVo.getConfigureId()).setPlanType(1);
                                    configurePlanDataMapper.insert(configurePlanData);

                                    if (CollectionUtils.isNotEmpty(configurePlanData.getConfigurePlanOldUnits())) {
                                        for (ConfigurePlanOldUnit configurePlanOldUnit : configurePlanData.getConfigurePlanOldUnits()) {
                                            configurePlanOldUnit.setConfigurePlanDataId(configurePlanData.getConfigurePlanDataId());
                                            configurePlanOldUnitMapper.insert(configurePlanOldUnit);
                                        }
                                    }
                                }
                            } else {
                                throw new ServiceException("请选择要智能分配的房间！");
                            }
                        }

                        //人工分配
                        if (configureFlowVo.getSelectionPlan().equals(2)) {
                            if (configureFlowVo.getPlanId() != null) {
                                configure.setPlanId(configureFlowVo.getPlanId());
                            } else {
                                throw new ServiceException("方案编号不能为空！");
                            }
                        }

                    } else {
                        throw new ServiceException("选择方案不能为空！");
                    }
                }
            } else {
                throw new ServiceException("分配方案不能为空！");
            }

            List<String> userIds = authorityUnitService.selectConfigureByStep(ConfigureStepNode.LEADERAPPROVAL.getValue());
            //设置领导审批权限用户
            if (CollectionUtils.isNotEmpty(userIds)) {
                configureWorkFlow.setLeaderUserIds(userIds);
            } else {
                throw new ServiceException("没有指定领导审批办理人！");
            }

            configure.setStep(ConfigureStepNode.LEADERAPPROVAL.getValue());
            configureProcess.saveThreeSubmitTask(configureWorkFlow);
            configureMapper.updateById(configure);

            //拟定分配方案
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(configureFlowVo.getConfigureId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("drawPlanUser", configureFlowVo.getUserName());
            commentMap.put("drawPlanTime", new Date());
            commentMap.put("userId", configureFlowVo.getUserId());
            commentMap.put("step", ConfigureStepNode.DUADPLAN.getValue());
            update.set("drawPlanDb", commentMap);
            mongoTemplate.upsert(query, update, "configure");
        } catch (Exception ex) {
            log.error("drawPlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 领导审批
     *
     * @param configureWorkFlow
     * @param configureFlowVo
     * @param configure
     */
    @Transactional(rollbackFor = Exception.class)
    public void configureLeadApproval(ConfigureWorkFlow configureWorkFlow, ConfigureFlowVo configureFlowVo, Configure configure) {
        try {
            if (configureFlowVo.getAllocationPlan() != null) {
                //置换、租用、建设
                if (configureFlowVo.getAllocationPlan().equals(2) || configureFlowVo.getAllocationPlan().equals(3) || configureFlowVo.getAllocationPlan().equals(4)) {
                    configureWorkFlow.setAllocationPlan(5);

                    if (configureFlowVo.getAuditState().equals(1)) {
                        List<String> userIds = authorityUnitService.selectConfigureByStep(ConfigureStepNode.UNITREVIEW.getValue());
                        //设置完善信息权限用户
                        if (CollectionUtils.isNotEmpty(userIds)) {
                            configureWorkFlow.setInGdataUserIds(userIds);
                        } else {
                            throw new ServiceException("没有指定完善信息办理人！");
                        }
                        configure.setStep(ConfigureStepNode.IMPROVINGDATA.getValue());
                    }
                }

            } else {
                throw new ServiceException("分配方案不能为空！");
            }

            if (configureFlowVo.getAuditState().equals(2)) {
                configure.setStep(ConfigureStepNode.DUADPLAN.getValue());
                if (StringUtils.isBlank(configureFlowVo.getComment())) {
                    throw new ServiceException("审批不同意时，审批意见不能为空！");
                }

                //删除掉配置方案数据以及关联的数据
                configurePlanDataMapper.deleteByConfigureId(configureFlowVo.getConfigureId());
            }

            ProcessInstance pi = configureProcess.saveFourSubmitTasks(configureWorkFlow);
            if (pi == null) {
                //审批通过
                configure.setApplyStatus(ApplyStatusEnum.AUDITPASS.getValue());
            }
            configureMapper.updateById(configure);

            //领导审批
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(configure.getConfigureId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("leaderApprovalUser", configureFlowVo.getUserName());
            commentMap.put("leaderApprovalTime", new Date());
            commentMap.put("ldaAuditState", configureFlowVo.getAuditState());
            commentMap.put("userId", configureFlowVo.getUserId());
            if (StringUtils.isNotBlank(configureFlowVo.getComment())) {
                commentMap.put("ldaComment", configureFlowVo.getComment());
            }
            commentMap.put("step", ConfigureStepNode.LEADERAPPROVAL.getValue());
            update.set("leaderApprovalDb", commentMap);
            mongoTemplate.upsert(query, update, "configure");
        } catch (Exception ex) {
            log.error("configureLeadApprovalError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 根据示意图来修改和删除配置相关数据
     *
     * @param configureIds
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateConfigureDate(List<Long> configureIds) {
        try {
            Configure configure = new Configure();
            configure.setStep(ConfigureStepNode.DUADPLAN.getValue());
            ConfigureWorkFlow configureWorkFlow = new ConfigureWorkFlow();
            //回退到拟定分配方案
            configureWorkFlow.setAuditState(2);
            Map<String, Object> userByToken = authorityUnitService.getUserByToken();
            if (userByToken.get("user_id") != null) {
                configureWorkFlow.setUserId(Long.valueOf(String.valueOf(userByToken.get("user_id"))));
            }
            if (userByToken.get("username") != null) {
                configureWorkFlow.setCurrentUser(String.valueOf(userByToken.get("username")));
            }
            if (CollectionUtils.isNotEmpty(configureIds)) {
                for (Long configureId : configureIds) {
                    //删除掉配置方案数据以及关联的数据
                    configurePlanDataMapper.deleteByConfigureId(configureId);
                    Configure configureApply = configureMapper.selectById(configureId);
                    if (configureApply.getStep().equals(ConfigureStepNode.LEADERAPPROVAL.getValue())) {
                        if (configureApply.getPlanId() != null) {
                            configurePlanChangeDataMapper.delete(new QueryWrapper<ConfigurePlanChangeData>().eq("plan_id", configureApply.getPlanId()));
                        }

                        configureWorkFlow.setTaskId(configureApply.getTaskId());
                        configureWorkFlow.setProcessInstanceId(configureApply.getProcessInstanceId());
                        configureProcess.saveFourSubmitTasks(configureWorkFlow);
                        configure.setConfigureId(configureId);
                        configure.setPlanId(null);
                        configureMapper.updateById(configure);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("updateConfigureDateError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 完善信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void improvingData(ConfigureWorkFlow configureWorkFlow, ConfigureFlowVo configureFlowVo, Configure configure) {
        try {
            Long yardId = null;
            if (configureFlowVo.getYardAddVo() != null) {
                yardId = yardService.addConfigureYard(configureFlowVo.getYardAddVo());
                ConfigureYard configureYard = new ConfigureYard();
                configureYard.setConfigureId(configureFlowVo.getConfigureId()).setYardId(yardId);
                configureYardMapper.insert(configureYard);
            }

            if (CollectionUtils.isNotEmpty(configureFlowVo.getBuildVoList())) {
                for (BuildVo buildVo : configureFlowVo.getBuildVoList()) {
                    if (buildVo.getYardId() == null) {
                        buildVo.setYardId(yardId);
                    }
                    Build build = dozerBeanMapper.map(buildVo, Build.class);
                    Configure configureApply = configureMapper.selectById(configureFlowVo.getConfigureId());
                    if (configureApply != null && configureApply.getAllocationPlan() != null) {
                        //选择的是租用，就将楼栋关联合同
                        if (configureApply.getAllocationPlan().equals(3)) {
                            build.setBuildContract(1);
                        }
                    }

                    Long buildId = buildService.addConfigureBuild(build, buildVo.getNewFloorImgUrlList(), buildVo.getOutRent(), buildVo.getFloorDrawingList());
                    ConfigureBuild configureBuild = new ConfigureBuild();
                    configureBuild.setConfigureId(configureFlowVo.getConfigureId()).setBuildId(buildId);
                    configureBuildMapper.insert(configureBuild);
                }
            } else {
                throw new ServiceException("楼座信息不能为空！");
            }

            ProcessInstance pi = configureProcess.saveFiveSubmitTask(configureWorkFlow);
            if (pi == null) {
                //审批通过
                configure.setApplyStatus(ApplyStatusEnum.AUDITPASS.getValue());
            }
            configureMapper.updateById(configure);

            //完善信息
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(configure.getConfigureId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("improvingDataUser", configureFlowVo.getUserName());
            commentMap.put("improvingDataTime", new Date());
            commentMap.put("userId", configureFlowVo.getUserId());
            commentMap.put("step", ConfigureStepNode.IMPROVINGDATA.getValue());
            commentMap.put("improvingDataRRC", configureFlowVo.getImprovingDataRRC());
            update.set("improvingDataDb", commentMap);
            mongoTemplate.upsert(query, update, "configure");
        } catch (Exception ex) {
            log.error("improvingDataError", ex);
            throw new ServiceException(ex.getMessage());
        }

    }

    /**
     * 返回给内控的配置相关信息
     *
     * @param ConfigureId
     * @return
     */
    public ConfigureHttpDto getConfigureInfo(Long ConfigureId) {
        try {
            ConfigureHttpDto configureHttpDto = new ConfigureHttpDto();
            ConfigureDto configureDto = this.basicConfigure(ConfigureId);
            ConfigureBasicHttpDto configureBasicHttpDto = dozerBeanMapper.map(configureDto, ConfigureBasicHttpDto.class);
            configureBasicHttpDto.setConfigureId(ConfigureId);
            QueryWrapper<Configure> wrapper = new QueryWrapper<>();
            wrapper.eq("configure_id", ConfigureId);
            wrapper.select("apply_reason", "contact_phone", "contacts", "unit_property", "unit_address");
            Configure configure = configureMapper.selectOne(wrapper);
            if (configure != null) {
                if (StringUtils.isNotBlank(configure.getContactPhone())) {
                    configureBasicHttpDto.setContactPhone(configure.getContactPhone());
                }
                if (StringUtils.isNotBlank(configure.getContacts())) {
                    configureBasicHttpDto.setContacts(configure.getContacts());
                }
                if (StringUtils.isNotBlank(configure.getUnitProperty())) {
                    configureBasicHttpDto.setUnitProperty(configure.getUnitProperty());
                }
                if (StringUtils.isNotBlank(configure.getUnitAddress())) {
                    configureBasicHttpDto.setUnitAddress(configure.getUnitAddress());
                }
                if (StringUtils.isNotBlank(configure.getApplyReason())) {
                    configureBasicHttpDto.setApplyReason(configure.getApplyReason());
                }
            }
            configureHttpDto.setConfigureBasicHttpDto(configureBasicHttpDto);
            List<ConfigureDistributionHttpDto> configureDistributionHttpDtos = configurePlanDataMapper.selectConfigureDbo(ConfigureId);
            if (CollectionUtils.isNotEmpty(configureDistributionHttpDtos)) {
                for (ConfigureDistributionHttpDto configureDistributionHttpDto : configureDistributionHttpDtos) {
                    List<String> roomLocations = new ArrayList<>();
                    if (StringUtils.isNotBlank(configureDistributionHttpDto.getYardName())) {
                        roomLocations.add(configureDistributionHttpDto.getYardName());
                    }
                    if (StringUtils.isNotBlank(configureDistributionHttpDto.getBuildName())) {
                        roomLocations.add(configureDistributionHttpDto.getBuildName());
                    }
                    if (StringUtils.isNotBlank(configureDistributionHttpDto.getHouseNumber())) {
                        roomLocations.add(configureDistributionHttpDto.getHouseNumber());
                    }

                    if (CollectionUtils.isNotEmpty(roomLocations)) {
                        configureDistributionHttpDto.setRoomLocation(String.join("-", roomLocations));
                    }

                    if (CollectionUtils.isNotEmpty(configureDistributionHttpDto.getConfigureOldUnitHttpDtoList())) {
                        List<String> oldUserUnits = new ArrayList<>();
                        for (ConfigureOldUnitHttpDto configureOldUnitHttpDto : configureDistributionHttpDto.getConfigureOldUnitHttpDtoList()) {
                            if (StringUtils.isNotBlank(configureOldUnitHttpDto.getOldUseUnitName())) {
                                oldUserUnits.add(configureOldUnitHttpDto.getOldUseUnitName());
                            }
                        }
                        if (CollectionUtils.isNotEmpty(oldUserUnits)) {
                            configureDistributionHttpDto.setOldUserUnits(String.join(",", oldUserUnits));
                        }
                    }

                    if (CollectionUtils.isNotEmpty(configureDistributionHttpDto.getConfigureNewUnitHttpDtoList())) {
                        List<String> oldUserUnits = new ArrayList<>();
                        for (ConfigureNewUnitHttpDto configureNewUnitHttpDto : configureDistributionHttpDto.getConfigureNewUnitHttpDtoList()) {
                            if (StringUtils.isNotBlank(configureNewUnitHttpDto.getNewUseUnitName())) {
                                oldUserUnits.add(configureNewUnitHttpDto.getNewUseUnitName());
                            }
                        }
                        if (CollectionUtils.isNotEmpty(oldUserUnits)) {
                            configureDistributionHttpDto.setOldUserUnits(String.join(",", oldUserUnits));
                        }
                    }
                }
                configureHttpDto.setConfigureDistributionHttps(configureDistributionHttpDtos);
            }
            return configureHttpDto;
        } catch (Exception ex) {
            log.error("getConfigureInfoError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 内控 领导审批
     *
     * @param processHttpResultVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean leaderApproval(ProcessHttpResultVo processHttpResultVo) {
        try {
            Configure configure = new Configure();
            configure.setConfigureId(processHttpResultVo.getApplyId());
            ConfigureWorkFlow configureWorkFlow = new ConfigureWorkFlow();

            Configure configureApply = configureMapper.selectById(processHttpResultVo.getApplyId());
            if (configureApply != null) {
                configureWorkFlow.setProcessInstanceId(configureApply.getProcessInstanceId());
            } else {
                throw new ServiceException("所传配置ID不符！");
            }

            configureWorkFlow.setConfigureId(processHttpResultVo.getApplyId());
            configureWorkFlow.setAuditState(processHttpResultVo.getAuditState());
            configureWorkFlow.setCurrentUser(processHttpResultVo.getCurrentUser());

            if (processHttpResultVo.getAuditState().equals(2)) {
                configure.setStep(ConfigureStepNode.DUADPLAN.getValue());
                if (StringUtils.isBlank(processHttpResultVo.getComment())) {
                    throw new ServiceException("审批不同意时，审批意见不能为空！");
                }

                //删除掉配置方案数据以及关联的数据
                configurePlanDataMapper.deleteByConfigureId(processHttpResultVo.getApplyId());
            }

            ProcessInstance pi = configureProcess.saveFourSubmitTask(configureWorkFlow);
            if (pi == null) {
                //审批通过
                configure.setApplyStatus(ApplyStatusEnum.AUDITPASS.getValue());
            }
            configureMapper.updateById(configure);

            //内控 领导审批
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(configure.getConfigureId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("leaderApprovalUser", processHttpResultVo.getCurrentUser());
            commentMap.put("leaderApprovalTime", new Date());
            commentMap.put("ldaAuditState", processHttpResultVo.getAuditState());
            if (StringUtils.isNotBlank(processHttpResultVo.getComment())) {
                commentMap.put("ldaComment", processHttpResultVo.getComment());
            }
            commentMap.put("step", ConfigureStepNode.LEADERAPPROVAL.getValue());
            update.set("leaderApprovalDb", commentMap);
            mongoTemplate.upsert(query, update, "configure");
            return true;
        } catch (Exception ex) {
            log.error("leaderApprovalError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询配置步骤集合
     *
     * @param configureId
     * @return
     */
    @Override
    public List<StepNodeDto> selectConfigureStep(Long configureId) {
        try {
            List<Document> applyExtend = null;
            Query query = new Query();
            List<StepNodeDto> stepNodeDtoList = new ArrayList<>();
            Configure configure = configureMapper.selectById(configureId);
            //获取当前流程
            Integer step = configure.getStep();

            List<StepNodeDto> stepNodeList = Arrays.stream(ConfigureStepNode.values())
                    .map(p -> new StepNodeDto(p.getValue(), p.getName())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(stepNodeList)) {
                for (StepNodeDto stepNodeDto : stepNodeList) {
                    if (stepNodeDto.getValue() < step) {
                        stepNodeDtoList.add(stepNodeDto);
                    }
                }
            }
            //判断有没有走完领导审批这一步,4
            if (step.equals(ConfigureStepNode.LEADERAPPROVAL.getValue())) {
                query.fields().include("leaderApprovalDb")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(configure.getConfigureId()));
                applyExtend = mongoTemplate.find(query, Document.class, "configure");
                List<Document> documentList = MongoDbUtil.DocumentToMap(applyExtend).stream().allMatch(MapUtils::isEmpty) ? null : applyExtend;
                if (CollectionUtils.isNotEmpty(documentList)) {
                    StepNodeDto stepNodeDto = new StepNodeDto(4, "领导审批");
                    stepNodeDtoList.add(stepNodeDto);
                }
            }

            //判断有没有走完完善资料这一步,5
            if (step.equals(ConfigureStepNode.IMPROVINGDATA.getValue())) {
                query.fields().include("improvingDataDb")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(configure.getConfigureId()));
                applyExtend = mongoTemplate.find(query, Document.class, "configure");
                List<Document> documentList = MongoDbUtil.DocumentToMap(applyExtend).stream().allMatch(MapUtils::isEmpty) ? null : applyExtend;
                if (CollectionUtils.isNotEmpty(documentList)) {
                    StepNodeDto stepNodeDto = new StepNodeDto(5, "完善资料");
                    stepNodeDtoList.add(stepNodeDto);
                }
            }
            return stepNodeDtoList;
        } catch (Exception ex) {
            log.error("selectConfigureStepError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 领导查看配置流程
     *
     * @param leaderReviewVo
     * @return
     */
    @Override
    public ConLeaderDto leaderReviewDetail(LeaderReviewVo leaderReviewVo) {
        try {
            ConLeaderDto conLeaderDto = new ConLeaderDto();
            //流程号
            Integer step = leaderReviewVo.getStep();
            //配置编号
            Long id = leaderReviewVo.getRepairApplyId();

            //查询配置附件集合
            List<ConfigureEnclosure> enclosureList = configureEnclosureMapper.selectList(new QueryWrapper<ConfigureEnclosure>().eq("configure_id", id));
            conLeaderDto.setEnclosureList(enclosureList);
            List<Document> applyExtend = null;
            Query query = new Query();

            //机关管理局用房情况审查
            if (step.equals(ConfigureStepNode.UNITREVIEW.getValue())) {
                query.fields().include("unitReviewDb.unitReviewUser")
                        .include("unitReviewDb.unitReviewTime")
                        .include("unitReviewDb.userId")
                        .include("unitReviewDb.unitReviewAuditState")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(id));
                applyExtend = mongoTemplate.find(query, Document.class, "configure");
                List<Document> documentList = MongoDbUtil.DocumentToMap(applyExtend).stream().allMatch(MapUtils::isEmpty) ? null : applyExtend;
                Configure configure = configureMapper.selectById(id);
                Long redistributionUnitId = configure.getRedistributionUnitId();
                if (redistributionUnitId != null) {
                    //查询用房情况审查数据
                    String unitName = configureMapper.selectData(id);
                    conLeaderDto.setNewUnitName(unitName);
                }
                //查询区域集合
                List<String> regionList = configureMapper.selectRegionList(id);
                conLeaderDto.setRegionList(regionList);
                conLeaderDto.setDocumentList(documentList);
                conLeaderDto.setElevatorsNumber(configure.getElevatorsNumber());
                //房间信息集合
                List<ConfigureRoom> configureRoomList = configureRoomMapper.selectList(new QueryWrapper<ConfigureRoom>().eq("configure_id", id).orderByDesc("configure_room_id"));
                conLeaderDto.setConfigureRoomList(configureRoomList);

                //拟定分配方案
            } else if (step.equals(ConfigureStepNode.DUADPLAN.getValue())) {
                query.fields().include("drawPlanDb.drawPlanTime")
                        .include("drawPlanDb.drawPlanUser")
                        .include("drawPlanDb.userId")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(id));
                applyExtend = mongoTemplate.find(query, Document.class, "configure");
                List<Document> documentList = MongoDbUtil.DocumentToMap(applyExtend).stream().allMatch(MapUtils::isEmpty) ? null : applyExtend;
                conLeaderDto.setDocumentList(documentList);
                Configure configure = configureMapper.selectById(id);
                //分配方案
                conLeaderDto.setAllocationPlan(configure.getAllocationPlan());
                //选择方案
                conLeaderDto.setSelectionPlan(configure.getSelectionPlan());
                //方案编号
                conLeaderDto.setPlanId(configure.getPlanId());

                //领导审批
            } else if (step.equals(ConfigureStepNode.LEADERAPPROVAL.getValue())) {
                query.fields().include("leaderApprovalDb.leaderApprovalTime")
                        .include("leaderApprovalDb.ldaComment")
                        .include("leaderApprovalDb.ldaAuditState")
                        .include("leaderApprovalDb.step")
                        .include("leaderApprovalDb.leaderApprovalUser")
                        .include("leaderApprovalDb.userId")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(id));
                applyExtend = mongoTemplate.find(query, Document.class, "configure");
                List<Document> documentList = MongoDbUtil.DocumentToMap(applyExtend).stream().allMatch(MapUtils::isEmpty) ? null : applyExtend;
                conLeaderDto.setDocumentList(documentList);
            }

            //完善资料
            else if (step.equals(ConfigureStepNode.IMPROVINGDATA.getValue())) {
                query.fields().include("improvingDataDb.improvingDataUser")
                        .include("improvingDataDb.improvingDataTime")
                        .include("improvingDataDb.userId")
                        .include("improvingDataDb.step")
                        .include("improvingDataDb.improvingDataRRC")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(id));
                applyExtend = mongoTemplate.find(query, Document.class, "configure");
                List<Document> documentList = MongoDbUtil.DocumentToMap(applyExtend).stream().allMatch(MapUtils::isEmpty) ? null : applyExtend;

                conLeaderDto.setDocumentList(documentList);
                //完善院落
                ConfigureYard configureYard = configureYardMapper.selectOne(new QueryWrapper<ConfigureYard>().eq("configure_id", id));
                if (configureYard != null) {
                    Yard yard = yardService.getById(configureYard.getYardId());
                    conLeaderDto.setYard(yard);
                }
                //完善楼栋
                List<ConfigureBuild> configureBuildList = configureBuildMapper.selectList(new QueryWrapper<ConfigureBuild>().eq("configure_id", id));
                if (CollectionUtils.isNotEmpty(configureBuildList)) {
                    ArrayList<BuildDetailDto> buildDetailDtoArrayList = new ArrayList<>();
                    for (ConfigureBuild configureBuild : configureBuildList) {
                        Long buildId = configureBuild.getBuildId();
                        BuildDetailDto buildDetailDto = (BuildDetailDto) buildService.detail(buildId);
                        if (buildDetailDto != null) {
                            buildDetailDtoArrayList.add(buildDetailDto);
                        }
                    }
                    conLeaderDto.setBuildDetailDtoList(buildDetailDtoArrayList);
                }
            }
            return conLeaderDto;
        } catch (Exception ex) {
            log.error("leaderReviewDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查看分配方案数据
     *
     * @param planDataVo
     * @return
     */
    @Override
    public Page<ConPlanDataDto> planDataDetail(PlanDataVo planDataVo) {
        try {
            Page<ConPlanDataDto> page = new Page<>(planDataVo.getCurrentPage(), planDataVo.getPageSize());
            return configureMapper.selectPlanData(page, planDataVo);
        } catch (Exception ex) {
            log.error("planDataDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询某个用户所有审批配置记录
     *
     * @param approvalRecordVo
     * @return
     */
    @Override
    public Page<ConfigureDto> selectConfigureRecord(ApprovalRecordVo approvalRecordVo) {
        try {
            List<Long> congigureIdList = new ArrayList<>();
            //查询mongo中所有数据
            List<Document> applyExtend = mongoTemplate.findAll(Document.class, "configure");
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
                                congigureIdList.add(repairApplyId);
                            }
                        }
                    }
                }
            }
            Page<ConfigureDto> page = new Page<>(approvalRecordVo.getCurrentPage(), approvalRecordVo.getPageSize());
            Page<ConfigureDto> configureDtoPage = configureMapper.selectConfigureRecord(page, approvalRecordVo.getUserId(), congigureIdList);
            List<ConfigureDto> records = configureDtoPage.getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                for (ConfigureDto configureDto : records) {
                    Configure configure = configureMapper.selectById(configureDto.getConfigureId());
                    Integer unitId = configure.getUnitId();
                    //查询实有人数
                    Integer acuNum = leadingOfficeMapper.selectCount(new QueryWrapper<LeadingOffice>().eq("unit_id", unitId));
                    configureDto.setActualNumber(acuNum);
                }
            }
            return configureDtoPage;
        } catch (Exception ex) {
            log.error("selectConfigureRecordError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询某个用户审批配置详情列表
     *
     * @param approvalDetailVo
     * @return
     */
    @Override
    public List<StepNodeDto> selectApprovalDetail(ApprovalConfigureVo approvalConfigureVo) {
        try {
            List<Integer> approvalIdList = new ArrayList<>();
            List<StepNodeDto> stepNodelist = new ArrayList<>();
            Document document = mongoTemplate.findById(approvalConfigureVo.getConfigureId(), Document.class, "configure");
            if (document != null) {
                Set<String> keySet = document.keySet();
                for (String key : keySet) {
                    if (!(key.equals("_id"))) {
                        //获取key对应值
                        Map<String, Object> map = (Map<String, Object>) document.get(key);
                        if (map != null && map.containsKey("userId")) {
                            Long userId1 = (Long) map.get("userId");
                            if (userId1.equals(approvalConfigureVo.getUserId())) {
                                Integer step = (Integer) map.get("step");
                                approvalIdList.add(step);
                            }
                        }
                    }
                }
            }
            List<StepNodeDto> stepNodeList = Arrays.stream(ConfigureStepNode.values()).map(p -> new StepNodeDto(p.getValue(), p.getName())).collect(Collectors.toList());
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
     * 查看分配方案数据(人工)
     *
     * @param configureId
     * @return
     */
    @Override
    public String planData(Long configureId) {
        try {
            return configureMapper.planData(configureId);
        } catch (Exception ex) {
            log.error("planDataError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 判断领导审批是否被打回
     *
     * @param configureId
     * @return
     */
    @Override
    public String isLeaderApproval(Long configureId) {
        try {
            String ldaComment = "";
            //查询指定document
            Document document = mongoTemplate.findById(configureId, Document.class, "configure");
            if (document != null) {
                Map<String, Object> map = (Map<String, Object>) document.get("leaderApprovalDb");
                if (map != null) {
                    Integer ldaAuditState = (Integer) map.get("ldaAuditState");
                    if (ldaAuditState.equals(2)) {
                        ldaComment = (String) map.get("ldaComment");
                    }
                }
            }
            return ldaComment;
        } catch (Exception ex) {
            log.error("isLeaderApprovalError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 显示相关联的配置申请(houseIdList)
     *
     * @param houseListVo
     * @return
     */
    @Override
    public Page<Configure> configureListByHouseId(HouseListVo houseListVo) {
        try {
            Page<Configure> page = new Page<>(houseListVo.getCurrentPage(), houseListVo.getPageSize());

            return configureMapper.configureListByHouseIdOrFloor(page, houseListVo);
        } catch (Exception ex) {
            log.error("configureListByHouseIdError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 显示相关联的配置申请(build+floorNameList)
     *
     * @param houseListVo
     * @return
     */
    @Override
    public Page<Configure> configureListByFloor(HouseListVo houseListVo) {
        try {
            Page<Configure> page = new Page<>(houseListVo.getCurrentPage(), houseListVo.getPageSize());
            return configureMapper.configureListByHouseIdOrFloor(page, houseListVo);
        } catch (Exception ex) {
            log.error("configureListByFloorError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 获取相关联的配置申请Id集合
     *
     * @param houseListVo
     * @return
     */
    @Override
    public List<Long> configureIdList(HouseListVo houseListVo) {
        try {
            return configureMapper.selectConfigureIdList(houseListVo);
        } catch (Exception ex) {
            log.error("configureIdListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除公共服务器上配置附件
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteConfigureEnclosure(List<Long> physicalAddressIdList) throws Exception {
        try {
            if (CollectionUtils.isNotEmpty(physicalAddressIdList)) {
                for (Long physicalAddressId : physicalAddressIdList) {
                    //查询配置附件
                    ConfigureEnclosure configureEnclosure = configureEnclosureMapper.selectById(physicalAddressId);
                    // 文件路径
                    String dataPath = configureEnclosure.getEnclosurePhysicsUrl();
                    if (StringUtils.isNotEmpty(dataPath)) {
                        synchronized (this) {
                            Files.deleteIfExists(Paths.get(dataPath));
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("deleteConfigureEnclosureError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


}
