package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.ConLeaderDto;
import com.ryzw.housemanager.dto.ConPlanDataDto;
import com.ryzw.housemanager.dto.ConfigureDto;
import com.ryzw.housemanager.dto.StepNodeDto;
import com.ryzw.housemanager.entity.Configure;
import com.ryzw.housemanager.vo.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 配置表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
public interface IConfigureService extends IService<Configure> {

    /**
     * 新增配置申请
     *
     * @param configureAddVo
     * @return
     */
    boolean addConfigure(ConfigureAddVo configureAddVo);

    /**
     * 查询配置申请基础数据
     *
     * @param configureId
     * @return
     */
    ConfigureDto basicConfigure(Long configureId);

    /**
     * 修改配置申请
     *
     * @param configureAddVo
     * @return
     */
    boolean updateConfigure(ConfigureAddVo configureAddVo);

    /**
     * 配置申请列表
     *
     * @param configureVo
     * @return
     */
    Page<ConfigureDto> configureList(ConfigureVo configureVo);

    /**
     * 更新用户配置权限
     *
     * @param activityAuthorityVo
     * @return
     */
    boolean updateConfigureAuthority(@Valid ActivityAuthorityVo activityAuthorityVo);

    /**
     * @param configureVo
     * @return
     */
    Page<ConfigureDto> configureApprovalList(ConfigureVo configureVo);

    /**
     * 审核申请审批
     *
     * @param configureFlowVo
     * @return
     */
    boolean saveSubmitConfigure(@Valid ConfigureFlowVo configureFlowVo);

    /**
     * 内控 领导审批
     *
     * @param processHttpResultVo
     * @return
     */
    boolean leaderApproval(ProcessHttpResultVo processHttpResultVo);

    /**
     * 查询配置步骤集合
     *
     * @param configureId
     * @return
     */
    List<StepNodeDto> selectConfigureStep(Long configureId);

    /**
     * 领导查看配置流程
     *
     * @param leaderReviewVo
     * @return
     */
    ConLeaderDto leaderReviewDetail(LeaderReviewVo leaderReviewVo);

    /**
     * 查看分配方案数据
     *
     * @param planDataVo
     * @return
     */
    Page<ConPlanDataDto> planDataDetail(PlanDataVo planDataVo);

    /**
     * 查询某个用户所有审批配置记录
     *
     * @param approvalRecordVo
     * @return
     */
    Page<ConfigureDto> selectConfigureRecord(ApprovalRecordVo approvalRecordVo);

    /**
     * 查询某个用户审批配置详情列表
     *
     * @param approvalConfigureVo
     * @return
     */
    List<StepNodeDto> selectApprovalDetail(ApprovalConfigureVo approvalConfigureVo);

    /**
     * 查看分配方案数据(人工)
     *
     * @param configureId
     * @return
     */
    String planData(Long configureId);

    /**
     * 判断领导审批是否被打回
     *
     * @param configureId
     * @return
     */
    String isLeaderApproval(Long configureId);

    /**
     * 显示相关联的配置申请(houseIdList)
     *
     * @param houseListVo
     * @return
     */
    Page<Configure> configureListByHouseId(HouseListVo houseListVo);

    /**
     * 显示相关联的配置申请(build+floorNameList)
     * @param houseListVo
     * @return
     */
    Page<Configure> configureListByFloor(HouseListVo houseListVo);

    void updateConfigureDate(List<Long> configureIds);

    /**
     * 获取相关联的配置申请Id集合
     *
     * @param houseListVo
     * @return
     */
    List<Long> configureIdList(HouseListVo houseListVo);

}
