package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.Handle;
import com.ryzw.housemanager.vo.*;
import org.bson.Document;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 处置表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-05-24
 */
public interface IHandleService extends IService<Handle> {

    /**
     * 新增处置申请
     *
     * @param handleAddVo
     * @return
     */
    boolean addHandle(HandleAddVo handleAddVo);

    /**
     * 修改处置申请
     *
     * @param handleUpdateVo
     * @return
     */
    boolean updateHandle(HandleUpdateVo handleUpdateVo);

    /**
     * 处置申请详情
     *
     * @param handleId
     * @return
     */
    HandleDetailDto handleDetail(Long handleId);

    /**
     * 处置申请列表
     *
     * @param handleVo
     * @return
     */
    Page<HandleDetailDto> handleList(HandleVo handleVo);

    /**
     * 更新用户处置权限
     *
     * @param activityAuthorityVo
     * @return
     */
    boolean updateHandleAuthority(@Valid ActivityAuthorityVo activityAuthorityVo);

    /**
     * 处置审批列表
     *
     * @param handleVo
     * @return
     */
    Page<HandleDetailDto> handleApprovalList(HandleVo handleVo);

    /**
     * 审核申请审批
     *
     * @param handleFlowVo
     * @return
     */
    boolean saveSubmitHandle(@Valid HandleFlowVo handleFlowVo);

    /**
     * 查询可以处置的位置
     *
     * @return
     */
    List<HandlePositionDto> selectHandlePo();

    /**
     * 查询处置类型列表
     *
     * @param handleId
     * @return
     */
    List<StepNodeDto> selectHandleType(Long handleId);

    /**
     * 领导查看处置流程
     *
     * @param leaderReviewVo
     * @return
     */
    List<Document> leaderReviewDetail(LeaderReviewVo leaderReviewVo);

    /**
     * 领导查看
     *
     * @param handleId
     * @return
     */
    LeaderDetailDto leaderDetail(Long handleId);

    /**
     * 修改时查询可以处置的位置
     *
     * @param handleId
     * @return
     */
    List<HandlePositionDto> selectHandlePoUp(Long handleId);

    /**
     * 查询所有院落楼栋树
     *
     * @return
     */
    List<HandlePositionDto> selectAllHandlePo();

    /**
     * 查询处置列表（不包含出租出借）
     *
     * @param handleLsVo
     * @return
     */
    Page<HandleDetailDto> handleLs(HandleLsVo handleLsVo);

    /**
     * 查询处置详情（不包含出租出借）
     * @param basicVo
     * @return
     */
    HandleLsDto handleLsDetail(BasicVo<Long> basicVo);

}
