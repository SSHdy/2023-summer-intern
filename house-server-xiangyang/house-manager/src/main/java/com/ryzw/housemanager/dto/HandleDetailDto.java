package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.entity.HandleModule;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 处置申请详情
 * </p>
 *
 * @author zf
 * @since 2019/5/24
 */
@Data
public class HandleDetailDto {

    /**
     * 处置编号
     */
    private Long handleId;

    /**
     * 当前状态
     */
    private Integer applyStatus;

    /**
     * 申请时间
     */
    private Date handleDate;

    /**
     * 申请原因
     */
    private String applyReason;

    /**
     * 处置面积
     */
    private Float handleArea;

    /**
     * 处置申请人
     */
    private String handleApplicant;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 处置位置集合
     */
    private List<BuildPositionDto> buildPositionDtoList;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 任务id
     */
    private  String taskId;

    /**
     * 当前流程号
     */
    private Integer step;

    /**
     * 处置方式
     */
    private Integer handleWay;

    /**
     * 处置模块对象集合
     */
    private List<HandleModule> handleModuleList;

    /**
     * 处置是否到房间 0否 1是
     */
    private Integer isHouse;


}
