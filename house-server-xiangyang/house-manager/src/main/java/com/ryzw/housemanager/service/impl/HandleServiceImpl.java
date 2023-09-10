package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.*;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.HandleMapper;
import com.ryzw.housemanager.mapper.HandleModuleMapper;
import com.ryzw.housemanager.mapper.HandleWayMapper;
import com.ryzw.housemanager.mapper.UnitMapper;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IHandleService;
import com.ryzw.housemanager.util.MongoDbUtil;
import com.ryzw.housemanager.vo.*;
import com.ryzw.houseprocess.HandleProcess;
import com.ryzw.houseprocess.HandleWorkFlow;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 处置表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-05-24
 */
@Service
@Slf4j
public class HandleServiceImpl extends ServiceImpl<HandleMapper, Handle> implements IHandleService {

    @Autowired
    private HandleMapper handleMapper;

    @Autowired
    private HandleModuleMapper handleModuleMapper;

    @Autowired
    private IAuthorityUnitService authorityUnitService;

    @Autowired
    private HandleProcess handleProcess;

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UnitMapper unitMapper;

    @Autowired
    private HandleWayMapper handleWayMapper;


    /**
     * 新增处置申请
     *
     * @param handleAddVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addHandle(HandleAddVo handleAddVo) {
        try {
            Handle handle = dozerBeanMapper.map(handleAddVo, Handle.class);
            //处置编号
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmss");
            String msg = sdf.format(date);
            msg += handleAddVo.getHandleApplicantId();
            handle.setHandleNo("CZ" + msg);
            //处置申请时间
            handle.setHandleDate(date);
            //当前流程,到了申请初审
            handle.setStep(HandleStepNode.INITIALAPPLY.getValue());
            //远程调用getUserByToken
            Map<String, Object> userByToken = authorityUnitService.getUserByToken();
            Long unitId = Long.valueOf(String.valueOf(userByToken.get("unit_id")));
            Unit unit = unitMapper.selectById(unitId);
            //申请单位名称
            handle.setUnitName(unit.getUnitName());
            //申请单位编号
            handle.setUnitId(unitId.longValue());
            //新增处置申请
            handleMapper.insert(handle);
            Long handleId = handle.getHandleId();
            List<HandleModule> handleModuleList = handleAddVo.getHandleModuleList();
            if (CollectionUtils.isNotEmpty(handleModuleList)) {
                for (HandleModule handleModule : handleModuleList) {
                    handleModule.setHandleId(handleId);
                    //新增处置模块
                    handleModuleMapper.insert(handleModule);
                }
            }
            //按step查询用户id
            List<String> userIds = authorityUnitService.selectHandleByStep(HandleStepNode.INITIALAPPLY.getValue());
            if (CollectionUtils.isNotEmpty(userIds)) {
                //启动工作流,获取流程实例ID
                String processInstanceId = handleProcess.startHandleProcess(handle.getHandleId(), handleAddVo.getHandleApplicant(), handleAddVo.getHandleApplicantId(), userIds);
                //更新维修申请表里面的流程实例ID字段值
                handle.setProcessInstanceId(processInstanceId);
                handleMapper.updateById(handle);
            } else {
                throw new ServiceException("没有指定申请初审办理人！");
            }
            return true;
        } catch (Exception ex) {
            log.error("addHandleError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改处置申请
     *
     * @param handleUpdateVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateHandle(HandleUpdateVo handleUpdateVo) {
        try {
            Handle handle = new Handle();
            handle.setHandleId(handleUpdateVo.getHandleId());
            handle.setApplyReason(handleUpdateVo.getApplyReason());
            handle.setHandleDate(new Date());
            Map<String, Object> userByToken = authorityUnitService.getUserByToken();
            Long unitId = Long.valueOf(String.valueOf(userByToken.get("unit_id")));
            handle.setUnitId(unitId);
            //修改处置申请表
            handleMapper.updateById(handle);
            //删除原处置模块
            handleModuleMapper.delete(new QueryWrapper<HandleModule>().eq("handle_id", handleUpdateVo.getHandleId()));
            List<HandleModule> handleModuleList = handleUpdateVo.getHandleModuleList();
            if (CollectionUtils.isNotEmpty(handleModuleList)) {
                for (HandleModule handleModule : handleModuleList) {
                    handleModule.setHandleId(handleUpdateVo.getHandleId());
                    //新增处置模块
                    handleModuleMapper.insert(handleModule);
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("updateHandleError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 处置申请详情
     *
     * @param handleId
     * @return
     */
    @Override
    public HandleDetailDto handleDetail(Long handleId) {
        try {
            HandleDetailDto handleDetailDto = new HandleDetailDto();
            Handle handle = handleMapper.selectById(handleId);
            if (handle != null && handle.getIsHouse() != null) {
                Integer isHouse = handle.getIsHouse();
                //0代表选的是楼栋 1代表选的是房间
                if (isHouse.equals(0)) {
                    handleDetailDto = handleMapper.handleDetailToBuild(handleId);
                } else if (isHouse.equals(1)) {
                    handleDetailDto = handleMapper.handleDetailToHouse(handleId);
                }
            }
            //查询处置模块集合
            List<HandleModule> handleModuleList = handleModuleMapper.selectList(new QueryWrapper<HandleModule>().eq("handle_id", handleId));
            //查询处置总面积
            Float handleArea = handleMapper.selectAreaSum(handleId);
            handleDetailDto.setHandleArea(handleArea);
            handleDetailDto.setHandleModuleList(handleModuleList);
            return handleDetailDto;
        } catch (Exception ex) {
            log.error("handleDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 处置申请列表
     *
     * @param handleVo
     * @return
     */
    @Override
    public Page<HandleDetailDto> handleList(HandleVo handleVo) {
        try {
            Page<HandleDetailDto> page = new Page<>(handleVo.getCurrentPage(), handleVo.getPageSize());
            Page<HandleDetailDto> handlePage = handleMapper.handleList(page, handleVo);
            List<HandleDetailDto> records = handlePage.getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                for (HandleDetailDto handleDetailDto : records) {
                    Long handleId = handleDetailDto.getHandleId();
                    List<BuildPositionDto> buildPositionDtoList = handleMapper.selectHandlePosition(handleId);
                    handleDetailDto.setBuildPositionDtoList(buildPositionDtoList);
                }
            }
            return handlePage;
        } catch (Exception ex) {
            log.error("handleListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 更新用户处置权限
     *
     * @param activityAuthorityVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateHandleAuthority(@Valid ActivityAuthorityVo activityAuthorityVo) {
        try {
            //向组任务中添加成员
            if (CollectionUtils.isNotEmpty(activityAuthorityVo.getSteps())) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.in("step", activityAuthorityVo.getSteps());
                List<Handle> handleList = handleMapper.selectList(queryWrapper);
                if (CollectionUtils.isNotEmpty(handleList)) {
                    for (Handle handle : handleList) {
                        if (StringUtils.isNotBlank(handle.getProcessInstanceId())) {
                            handleProcess.addGroupUser(handle.getProcessInstanceId(), activityAuthorityVo.getUserId());
                        }
                    }
                }
            }

            //从组任务中删除成员
            if (CollectionUtils.isNotEmpty(activityAuthorityVo.getRemoveSteps())) {
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.in("step", activityAuthorityVo.getRemoveSteps());
                List<Handle> handleList = handleMapper.selectList(queryWrapper);
                if (CollectionUtils.isNotEmpty(handleList)) {
                    for (Handle handle : handleList) {
                        if (StringUtils.isNotBlank(handle.getProcessInstanceId())) {
                            handleProcess.deleteGroupUser(handle.getProcessInstanceId(), activityAuthorityVo.getUserId());
                        }
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("updateHandleAuthorityError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 处置审批列表
     *
     * @param handleVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Page<HandleDetailDto> handleApprovalList(HandleVo handleVo) {
        try {
            if (handleVo.getUserId() != null) {
                Page<HandleDetailDto> page = new Page<>(handleVo.getCurrentPage(), handleVo.getPageSize());
                List<HandleWorkFlow> handleList = handleProcess.findTaskListById(handleVo.getUserId());
                List<Long> handleIds = new ArrayList<>();
                if (CollectionUtils.isNotEmpty(handleList)) {
                    for (HandleWorkFlow handleWorkFlow : handleList) {
                        Handle handle = new Handle();
                        handle.setHandleId(handleWorkFlow.getHandleId());
                        handle.setTaskId(handleWorkFlow.getTaskId());
                        handleMapper.updateById(handle);
                        handleIds.add(handleWorkFlow.getHandleId());
                    }
                    handleVo.setUserId(null);
                    handleVo.setHandleIdList(handleIds);
                    Page<HandleDetailDto> handlePage = handleMapper.handleList(page, handleVo);
                    List<HandleDetailDto> records = handlePage.getRecords();
                    if (CollectionUtils.isNotEmpty(records)) {
                        for (HandleDetailDto handleDetailDto : records) {
                            Long handleId = handleDetailDto.getHandleId();
                            List<BuildPositionDto> buildPositionDtoList = handleMapper.selectHandlePosition(handleId);
                            handleDetailDto.setBuildPositionDtoList(buildPositionDtoList);
                        }
                    }
                    return handlePage;
                } else {
                    return page;
                }
            } else {
                throw new ServiceException("用户id不能为空！");
            }
        } catch (Exception ex) {
            log.error("handleApprovalListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 审核申请审批
     *
     * @param handleFlowVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveSubmitHandle(@Valid HandleFlowVo handleFlowVo) {
        try {
            HandleWorkFlow handleWorkFlow = dozerBeanMapper.map(handleFlowVo, HandleWorkFlow.class);

            Handle handle = new Handle();
            handle.setHandleId(handleFlowVo.getHandleId());

            //2.申请初审
            if (handleFlowVo.getStep().equals(HandleStepNode.INITIALAPPLY.getValue())) {
                this.initApply(handleWorkFlow, handleFlowVo, handle);
            }

            //3.财政或人民政府审批
            if (handleFlowVo.getStep().equals(HandleStepNode.GOVERNMENTAPPLY.getValue())) {
                this.financeApproval(handleWorkFlow, handleFlowVo, handle);
            }

            //4.转换用途
            if (handleFlowVo.getStep().equals(HandleStepNode.TRANSFROMATION.getValue())) {
                this.conversionPurposes(handleWorkFlow, handleFlowVo, handle);
            }

            //5.拍卖
            if (handleFlowVo.getStep().equals(HandleStepNode.ACUTION.getValue())) {
                this.auction(handleWorkFlow, handleFlowVo, handle);
            }

            //6.拆除
            if (handleFlowVo.getStep().equals(HandleStepNode.DISMANTLE.getValue())) {
                this.dismantle(handleWorkFlow, handleFlowVo, handle);
            }

            //7.征收
            if (handleFlowVo.getStep().equals(HandleStepNode.LEVY.getValue())) {
                this.levy(handleWorkFlow, handleFlowVo, handle);
            }
            return true;
        } catch (Exception ex) {
            log.error("saveSubmitHandleError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询可以处置的位置
     *
     * @return
     */
    @Override
    public List<HandlePositionDto> selectHandlePo() {
        try {
            Map<String, Object> userByToken = authorityUnitService.getUserByToken();
            Integer unitId = (Integer) userByToken.get("unit_id");
            return handleMapper.selectHandlePo(unitId);
        } catch (Exception ex) {
            log.error("selectHandlePoError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 申请初审
     *
     * @param handleWorkFlow
     * @param handleFlowVo
     * @param handle
     */
    @Transactional(rollbackFor = Exception.class)
    public void initApply(HandleWorkFlow handleWorkFlow, HandleFlowVo handleFlowVo, Handle handle) {
        try {
            //申请初审
            if (handleFlowVo.getAuditState().equals(1)) {
                handle.setStep(HandleStepNode.GOVERNMENTAPPLY.getValue());
                List<String> userIds = authorityUnitService.selectHandleByStep(HandleStepNode.GOVERNMENTAPPLY.getValue());
                //设置财政或人民政府审批权限用户
                if (CollectionUtils.isNotEmpty(userIds)) {
                    handleWorkFlow.setGtUserIds(userIds);
                } else {
                    throw new ServiceException("没有指定财政或人民政审批办理人！");
                }
            }

            ProcessInstance pi = handleProcess.saveTwoSubmitTask(handleWorkFlow);

            if (pi == null) {
                //审批不通过
                if (handleFlowVo.getAuditState().equals(2)) {
                    handle.setApplyStatus(ApplyStatusEnum.AUDITFAILED.getValue());
                }
            } else {
                //审核中
                handle.setApplyStatus(ApplyStatusEnum.INAUDIT.getValue());
            }
            if (handleFlowVo.getHandleWay() != null) {
                handle.setHandleWay(handleFlowVo.getHandleWay());
            } else {
                throw new ServiceException("处置方式不能为空！");
            }

            handleMapper.updateById(handle);

            //申请初审信息
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(handleFlowVo.getHandleId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("initApplyAuditState", handleFlowVo.getAuditState());
            commentMap.put("initApplyUser", handleFlowVo.getUserName());
            commentMap.put("initApplyTime", new Date());
            if (StringUtils.isNotBlank(handleFlowVo.getComment())) {
                commentMap.put("initApplyComment", handleFlowVo.getComment());
            } else {
                if (handleFlowVo.getAuditState().equals(2) && StringUtils.isBlank(handleFlowVo.getComment())) {
                    throw new ServiceException("审批意见不能为空！");
                }
            }
            commentMap.put("handleWay", handleFlowVo.getHandleWay());
            commentMap.put("userId", handleFlowVo.getUserId());
            commentMap.put("step", HandleStepNode.INITIALAPPLY.getValue());
            update.set("initApplyDb", commentMap);
            mongoTemplate.upsert(query, update, "handle");
        } catch (Exception ex) {
            log.error("initApplyError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 财政或人民政府审批
     *
     * @param handleWorkFlow
     * @param handleFlowVo
     * @param handle
     */
    @Transactional(rollbackFor = Exception.class)
    public void financeApproval(HandleWorkFlow handleWorkFlow, HandleFlowVo handleFlowVo, Handle handle) {
        try {
            //财政或人民政府审批
            if (handleFlowVo.getAuditState().equals(1)) {
                if (handleFlowVo.getHandleWay() != null) {
                    //转换用途
                    if (handleFlowVo.getHandleWay().equals(HandleWayEnum.TRANSFROMATION.getValue())) {
                        handle.setStep(HandleStepNode.TRANSFROMATION.getValue());
                        List<String> userIds = authorityUnitService.selectHandleByStep(HandleStepNode.TRANSFROMATION.getValue());
                        //设置转换用途权限用户
                        if (CollectionUtils.isNotEmpty(userIds)) {
                            handleWorkFlow.setConversionUserIds(userIds);
                        } else {
                            throw new ServiceException("没有指定转换用途办理人！");
                        }
                    }
                    //拍卖
                    if (handleFlowVo.getHandleWay().equals(HandleWayEnum.ACUTION.getValue())) {
                        handle.setStep(HandleStepNode.ACUTION.getValue());
                        List<String> userIds = authorityUnitService.selectHandleByStep(HandleStepNode.ACUTION.getValue());
                        //设置拍卖权限用户
                        if (CollectionUtils.isNotEmpty(userIds)) {
                            handleWorkFlow.setAuctionUserIds(userIds);
                        } else {
                            throw new ServiceException("没有指定拍卖办理人！");
                        }
                    }
                    //拆除
                    if (handleFlowVo.getHandleWay().equals(HandleWayEnum.DISMANTLE.getValue())) {
                        handle.setStep(HandleStepNode.DISMANTLE.getValue());
                        List<String> userIds = authorityUnitService.selectHandleByStep(HandleStepNode.DISMANTLE.getValue());
                        //设置拆除权限用户
                        if (CollectionUtils.isNotEmpty(userIds)) {
                            handleWorkFlow.setDismantleUserIds(userIds);
                        } else {
                            throw new ServiceException("没有指定拆除办理人！");
                        }
                    }
                    //征收
                    if (handleFlowVo.getHandleWay().equals(HandleWayEnum.LEVY.getValue())) {
                        handle.setStep(HandleStepNode.LEVY.getValue());
                        List<String> userIds = authorityUnitService.selectHandleByStep(HandleStepNode.LEVY.getValue());
                        //设置征收权限用户
                        if (CollectionUtils.isNotEmpty(userIds)) {
                            handleWorkFlow.setCollectionUserIds(userIds);
                        } else {
                            throw new ServiceException("没有指定征收办理人！");
                        }
                    }
                } else {
                    throw new ServiceException("处置方式不能为空！");
                }

                // 财政或人民政府审批通过后，向处置方式表写入数据
                QueryWrapper queryWrapper = new QueryWrapper();
                queryWrapper.eq("handle_id", handleFlowVo.getHandleId());
                List<HandleModule> handleModuleList = handleModuleMapper.selectList(queryWrapper);
                if (CollectionUtils.isNotEmpty(handleModuleList)) {
                    for (HandleModule handleModule : handleModuleList) {
                        HandleWay handleWay = new HandleWay();
                        if (handleModule.getYardId() != null) {
                            handleWay.setYardId(handleModule.getYardId());
                        }
                        if (handleModule.getBuildId() != null) {
                            handleWay.setBuildId(handleModule.getBuildId());
                        }
                        if (StringUtils.isNotBlank(handleModule.getHouseId())) {
                            handleWay.setHouseId(handleModule.getHouseId());
                        }
                        handleWay.setHandleWay(handleFlowVo.getHandleWay());
                        handleWayMapper.insert(handleWay);
                    }
                }
            }

            ProcessInstance pi = handleProcess.saveThreeSubmitTask(handleWorkFlow);

            if (pi == null) {
                //出租，审批通过
                if (handleFlowVo.getAuditState().equals(1)) {
                    handle.setApplyStatus(ApplyStatusEnum.AUDITPASS.getValue());
                }
                //审批不通过
                if (handleFlowVo.getAuditState().equals(2)) {
                    handle.setApplyStatus(ApplyStatusEnum.AUDITFAILED.getValue());
                }
            } else {
                //审核中
                handle.setApplyStatus(ApplyStatusEnum.INAUDIT.getValue());
            }
            handleMapper.updateById(handle);

            //财政或人民政府审批信息
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(handleFlowVo.getHandleId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("falAuditState", handleFlowVo.getAuditState());
            commentMap.put("falUser", handleFlowVo.getUserName());
            commentMap.put("falTime", new Date());
            if (StringUtils.isNotBlank(handleFlowVo.getComment())) {
                commentMap.put("falComment", handleFlowVo.getComment());
            } else {
                if (handleFlowVo.getAuditState().equals(2) && StringUtils.isBlank(handleFlowVo.getComment())) {
                    throw new ServiceException("审批意见不能为空！");
                }
            }
            commentMap.put("userId", handleFlowVo.getUserId());
            commentMap.put("step", HandleStepNode.GOVERNMENTAPPLY.getValue());
            commentMap.put("falApprovalData", handleFlowVo.getBasicDBObject());
            update.set("financeApprovalDb", commentMap);
            mongoTemplate.upsert(query, update, "handle");
        } catch (Exception ex) {
            log.error("financeApprovalError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 转换用途
     *
     * @param handleWorkFlow
     * @param handleFlowVo
     * @param handle
     */
    @Transactional(rollbackFor = Exception.class)
    public void conversionPurposes(HandleWorkFlow handleWorkFlow, HandleFlowVo handleFlowVo, Handle handle) {
        try {
            ProcessInstance pi = handleProcess.saveFourSubmitTask(handleWorkFlow);
            if (pi == null) {
                handle.setApplyStatus(ApplyStatusEnum.AUDITPASS.getValue());
            }
            handleMapper.updateById(handle);

            //转换用途信息
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(handleFlowVo.getHandleId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("cpsUser", handleFlowVo.getUserName());
            commentMap.put("cpsTime", new Date());
            if (StringUtils.isNotBlank(handleFlowVo.getPonUse())) {
                commentMap.put("ponUse", handleFlowVo.getPonUse());
            } else {
                throw new ServiceException("转换后用途不能为空！");
            }
            if (StringUtils.isNotBlank(handleFlowVo.getComment())) {
                commentMap.put("cpsComment", handleFlowVo.getComment());
            }
            commentMap.put("userId", handleFlowVo.getUserId());
            commentMap.put("step", HandleStepNode.TRANSFROMATION.getValue());
            commentMap.put("conPurposesData", handleFlowVo.getBasicDBObject());
            update.set("conversionPurposesDb", commentMap);
            mongoTemplate.upsert(query, update, "handle");
        } catch (Exception ex) {
            log.error("conversionPurposesError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 拍卖
     *
     * @param handleWorkFlow
     * @param handleFlowVo
     * @param handle
     */
    @Transactional(rollbackFor = Exception.class)
    public void auction(HandleWorkFlow handleWorkFlow, HandleFlowVo handleFlowVo, Handle handle) {
        try {
            ProcessInstance pi = handleProcess.saveFiveSubmitTask(handleWorkFlow);
            if (pi == null) {
                handle.setApplyStatus(ApplyStatusEnum.AUDITPASS.getValue());
            }
            handleMapper.updateById(handle);

            //转换用途信息
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(handleFlowVo.getHandleId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("auctionUser", handleFlowVo.getUserName());
            commentMap.put("auctionTime", new Date());
            if (StringUtils.isNotBlank(handleFlowVo.getWinningBidder())) {
                commentMap.put("winningBidder", handleFlowVo.getWinningBidder());
            } else {
                throw new ServiceException("中标人不能为空！");
            }
            if (StringUtils.isNotBlank(handleFlowVo.getContacts())) {
                commentMap.put("contacts", handleFlowVo.getContacts());
            } else {
                throw new ServiceException("联系人不能为空！");
            }
            if (StringUtils.isNotBlank(handleFlowVo.getContactNumber())) {
                commentMap.put("contactNumber", handleFlowVo.getContactNumber());
            } else {
                throw new ServiceException("联系电话不能为空！");
            }
            if (StringUtils.isNotBlank(handleFlowVo.getWinningAmount())) {
                commentMap.put("winningAmount", handleFlowVo.getWinningAmount());
            } else {
                throw new ServiceException("中标金额不能为空！");
            }
            if (StringUtils.isNotBlank(handleFlowVo.getAgency())) {
                commentMap.put("agency", handleFlowVo.getAgency());
            } else {
                throw new ServiceException("代理机构不能为空！");
            }
            if (StringUtils.isNotBlank(handleFlowVo.getAgentContact())) {
                commentMap.put("agentContact", handleFlowVo.getAgentContact());
            } else {
                throw new ServiceException("代理联系方式不能为空！");
            }
            commentMap.put("userId", handleFlowVo.getUserId());
            commentMap.put("step", HandleStepNode.ACUTION.getValue());
            commentMap.put("auctionData", handleFlowVo.getBasicDBObject());
            update.set("auctionDb", commentMap);
            mongoTemplate.upsert(query, update, "handle");
        } catch (Exception ex) {
            log.error("auctionError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 拆除
     *
     * @param handleWorkFlow
     * @param handleFlowVo
     * @param handle
     */
    @Transactional(rollbackFor = Exception.class)
    public void dismantle(HandleWorkFlow handleWorkFlow, HandleFlowVo handleFlowVo, Handle handle) {
        try {
            ProcessInstance pi = handleProcess.saveSixSubmitTask(handleWorkFlow);
            if (pi == null) {
                handle.setApplyStatus(ApplyStatusEnum.AUDITPASS.getValue());
            }
            handleMapper.updateById(handle);

            //拆除信息
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(handleFlowVo.getHandleId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("dismantleUser", handleFlowVo.getUserName());
            commentMap.put("dismantleTime", new Date());
            if (StringUtils.isNotBlank(handleFlowVo.getImplementationUnit())) {
                commentMap.put("implementationUnit", handleFlowVo.getImplementationUnit());
            } else {
                throw new ServiceException("实施单位不能为空！");
            }
            if (StringUtils.isNotBlank(handleFlowVo.getDemolitionTime())) {
                commentMap.put("demolitionTime", handleFlowVo.getDemolitionTime());
            } else {
                throw new ServiceException("拆除时间不能为空！");
            }
            if (StringUtils.isNotBlank(handleFlowVo.getComment())) {
                commentMap.put("dismantleComment", handleFlowVo.getComment());
            }
            commentMap.put("userId", handleFlowVo.getUserId());
            commentMap.put("step", HandleStepNode.DISMANTLE.getValue());
            commentMap.put("dismantleData", handleFlowVo.getBasicDBObject());
            update.set("dismantleDb", commentMap);
            mongoTemplate.upsert(query, update, "handle");
        } catch (Exception ex) {
            log.error("dismantleError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 征收
     *
     * @param handleWorkFlow
     * @param handleFlowVo
     * @param handle
     */
    @Transactional(rollbackFor = Exception.class)
    public void levy(HandleWorkFlow handleWorkFlow, HandleFlowVo handleFlowVo, Handle handle) {
        try {
            ProcessInstance pi = handleProcess.saveSevenSubmitTask(handleWorkFlow);
            if (pi == null) {
                handle.setApplyStatus(ApplyStatusEnum.AUDITPASS.getValue());
            }
            handleMapper.updateById(handle);

            //征收信息
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(handleFlowVo.getHandleId()));
            Update update = new Update();
            Map<String, Object> commentMap = new HashMap<>();
            commentMap.put("levyUser", handleFlowVo.getUserName());
            commentMap.put("levyTime", new Date());
            if (StringUtils.isNotBlank(handleFlowVo.getHouseLevyUnit())) {
                commentMap.put("houseLevyUnit", handleFlowVo.getHouseLevyUnit());
            } else {
                throw new ServiceException("房屋征收单位不能为空！");
            }
            if (handleFlowVo.getLevyCompensation() != null) {
                commentMap.put("levyCompensation", handleFlowVo.getLevyCompensation());
            } else {
                throw new ServiceException("征收补偿款不能为空！");
            }
            commentMap.put("userId", handleFlowVo.getUserId());
            commentMap.put("step", HandleStepNode.LEVY.getValue());
            commentMap.put("levyData", handleFlowVo.getBasicDBObject());
            update.set("levyDb", commentMap);
            mongoTemplate.upsert(query, update, "handle");
        } catch (Exception ex) {
            log.error("levyError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询处置类型列表
     *
     * @param handleId
     * @return
     */
    @Override
    public List<StepNodeDto> selectHandleType(Long handleId) {
        try {
            List<StepNodeDto> stepNodeDtoList = new ArrayList<>();
            Handle handle = handleMapper.selectById(handleId);
            //获取当前流程
            Integer step = handle.getStep();
            List<StepNodeDto> stepNodeList = Arrays.stream(HandleStepNode.values())
                    .map(p -> new StepNodeDto(p.getValue(), p.getName())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(stepNodeList)) {
                for (StepNodeDto stepNodeDto : stepNodeList) {
                    if (stepNodeDto.getValue() < step) {
                        stepNodeDtoList.add(stepNodeDto);
                    }
                }
            }
            return stepNodeDtoList;
        } catch (Exception ex) {
            log.error("selectHandleTypeError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 领导查看处置流程
     *
     * @param leaderReviewVo
     * @return
     */
    @Override
    public List<Document> leaderReviewDetail(LeaderReviewVo leaderReviewVo) {
        try {
            //流程号
            Integer step = leaderReviewVo.getStep();
            //处置编号
            Long id = leaderReviewVo.getRepairApplyId();
            List<Document> applyExtend = null;
            Query query = new Query();
            //1单位申请
            if (step.equals(HandleStepNode.UNITAPPLY.getValue())) {
                query.fields().include("busApprovalCommentDb.busApprovalComment")
                        .include("busApprovalCommentDb.initApplyUser")
                        .include("busApprovalCommentDb.initApplyTime")
                        .include("busApprovalCommentDb.busApprovalAuditState")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(id));
                applyExtend = mongoTemplate.find(query, Document.class, "handle");

                //2申请初审
            } else if (step.equals(HandleStepNode.INITIALAPPLY.getValue())) {
                query.fields().include("initApplyDb.initApplyAuditState")
                        .include("initApplyDb.initApplyTime")
                        .include("initApplyDb.initApplyUser")
                        .include("initApplyDb.initApplyComment")
                        .include("initApplyDb.handleWay")
                        .include("initApplyDb.step")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(id));
                applyExtend = mongoTemplate.find(query, Document.class, "handle");

                //3财政或人民政府审批
            } else if (step.equals(HandleStepNode.GOVERNMENTAPPLY.getValue())) {
                query.fields().include("financeApprovalDb.falApprovalData.physicalAddress")
                        .include("financeApprovalDb.falApprovalData.repairFileName")
                        .include("financeApprovalDb.falApprovalData.type")
                        .include("financeApprovalDb.falComment")
                        .include("financeApprovalDb.falTime")
                        .include("financeApprovalDb.handleWay")
                        .include("financeApprovalDb.falUser")
                        .include("financeApprovalDb.falAuditState")
                        .include("financeApprovalDb.step")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(id));
                applyExtend = mongoTemplate.find(query, Document.class, "handle");

                //4转换用途
            } else if (step.equals(HandleStepNode.TRANSFROMATION.getValue())) {
                query.fields().include("conversionPurposesDb.conPurposesData.physicalAddress")
                        .include("conversionPurposesDb.conPurposesData.repairFileName")
                        .include("conversionPurposesDb.conPurposesData.type")
                        .include("conversionPurposesDb.cpsUser")
                        .include("conversionPurposesDb.cpsComment")
                        .include("conversionPurposesDb.handleWay")
                        .include("conversionPurposesDb.ponUse")
                        .include("conversionPurposesDb.cpsTime")
                        .include("conversionPurposesDb.step")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(id));
                applyExtend = mongoTemplate.find(query, Document.class, "handle");

                //5拍卖
            } else if (step.equals(HandleStepNode.ACUTION.getValue())) {
                query.fields().include("auctionDb.auctionData.physicalAddress")
                        .include("auctionDb.auctionData.repairFileName")
                        .include("auctionDb.auctionData.type")
                        .include("auctionDb.agency")
                        .include("auctionDb.winningAmount")
                        .include("auctionDb.auctionTime")
                        .include("auctionDb.handleWay")
                        .include("auctionDb.winningBidder")
                        .include("auctionDb.agentContact")
                        .include("auctionDb.auctionUser")
                        .include("auctionDb.contactNumber")
                        .include("auctionDb.contacts")
                        .include("auctionDb.step")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(id));
                applyExtend = mongoTemplate.find(query, Document.class, "handle");

                //6拆除
            } else if (step.equals(HandleStepNode.DISMANTLE.getValue())) {
                query.fields().include("dismantleDb.dismantleData.physicalAddress")
                        .include("dismantleDb.dismantleData.repairFileName")
                        .include("dismantleDb.dismantleData.type")
                        .include("dismantleDb.demolitionTime")
                        .include("dismantleDb.handleWay")
                        .include("dismantleDb.implementationUnit")
                        .include("dismantleDb.dismantleUser")
                        .include("dismantleDb.dismantleTime")
                        .include("dismantleDb.step")
                        .include("dismantleDb.dismantleComment")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(id));
                applyExtend = mongoTemplate.find(query, Document.class, "handle");

                //7增收
            } else if (step.equals(HandleStepNode.LEVY.getValue())) {
                query.fields().include("levyDb.levyData.physicalAddress")
                        .include("levyDb.levyData.repairFileName")
                        .include("levyDb.levyData.type")
                        .include("levyDb.houseLevyUnit")
                        .include("levyDb.levyUser")
                        .include("levyDb.levyTime")
                        .include("levyDb.levyCompensation")
                        .include("levyDb.userId")
                        .include("levyDb.step")
                        .exclude("_id");
                query.addCriteria(Criteria.where("_id").is(id));
                applyExtend = mongoTemplate.find(query, Document.class, "handle");
            }
            return MongoDbUtil.DocumentToMap(applyExtend).stream().allMatch(MapUtils::isEmpty) ? null : applyExtend;
        } catch (Exception ex) {
            log.error("leaderReviewDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 领导查看
     *
     * @param handleId
     * @return
     */
    @Override
    public LeaderDetailDto leaderDetail(Long handleId) {
        try {
            LeaderDetailDto leaderDetailDto = new LeaderDetailDto();
            Handle handle = handleMapper.selectById(handleId);
            //处置详情
            HandleDetailDto handleDetailDto = this.handleDetail(handleId);
            LeaderReviewVo leaderReviewVo = new LeaderReviewVo();
            leaderReviewVo.setRepairApplyId(handleId);
            List<List<Document>> documents = new ArrayList<>();
            for (Integer i = 2; i <= handle.getStep(); i++) {
                leaderReviewVo.setStep(i);
                //流程详情
                List<Document> documentList = this.leaderReviewDetail(leaderReviewVo);
                documents.add(documentList);
            }
            leaderDetailDto.setHandleDetailDto(handleDetailDto);
            leaderDetailDto.setDocuments(documents);
            return leaderDetailDto;
        } catch (Exception ex) {
            log.error("leaderDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 修改时查询可以处置的位置
     *
     * @param handleId
     * @return
     */
    @Override
    public List<HandlePositionDto> selectHandlePoUp(Long handleId) {
        try {
            Map<String, Object> userByToken = authorityUnitService.getUserByToken();
            Integer unitId = (Integer) userByToken.get("unit_id");
            return handleMapper.selectHandlePoUp(handleId, unitId);
        } catch (Exception ex) {
            log.error("selectHandlePoUpError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询所有院落楼栋树
     *
     * @return
     */
    @Override
    public List<HandlePositionDto> selectAllHandlePo() {
        try {
            return handleMapper.selectAllHandlePo();
        } catch (Exception ex) {
            log.error("selectAllHandlePoError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询处置列表（不包含出租出借）
     *
     * @param handleLsVo
     * @return
     */
    @Override
    public Page<HandleDetailDto> handleLs(HandleLsVo handleLsVo) {

        try {
            Page<HandleDetailDto> page = new Page<>(handleLsVo.getCurrentPage(), handleLsVo.getPageSize());
            Page<HandleDetailDto> handlePage = handleMapper.handleLs(page, handleLsVo);
            List<HandleDetailDto> records = handlePage.getRecords();
            if (CollectionUtils.isNotEmpty(records)) {
                for (HandleDetailDto handleDetailDto : records) {
                    Long handleId = handleDetailDto.getHandleId();
                    List<BuildPositionDto> buildPositionDtoList = handleMapper.selectHandlePosition(handleId);
                    handleDetailDto.setBuildPositionDtoList(buildPositionDtoList);
                }
            }
            return handlePage;
        } catch (Exception ex) {
            log.error("handleListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询处置详情（不包含出租出借）
     *
     * @param basicVo
     * @return
     */
    @Override
    public HandleLsDto handleLsDetail(BasicVo<Long> basicVo) {
        return handleMapper.handleLsDetail(basicVo);
    }
}
