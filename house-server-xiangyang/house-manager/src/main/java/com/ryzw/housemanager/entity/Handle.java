package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 处置表
 * </p>
 *
 * @author mfl
 * @since 2019-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Handle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 处置申请编号
     */
    @TableId(value = "handle_id", type = IdType.AUTO)
    private Long handleId;

    /**
     * 处置编号
     */
    private String handleNo;

    /**
     * 处置申请时间
     */
    private Date handleDate;

    /**
     * 处置申请人
     */
    private String handleApplicant;

    /**
     * 处置申请人编号
     */
    private Long handleApplicantId;

    /**
     * 当前流程
     */
    private Integer step;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 申请状态(0未审核，1审核通过，2审核未通过，3审核中)
     */
    private Integer applyStatus;

    /**
     * 申请原因
     */
    private String applyReason;

    /**
     * 处置方式(1转换用途 2出租 3拍卖 4 拆除)
     */
    private Integer handleWay;

    /**
     * 申请单位名称
     */
    private String unitName;

    /**
     * 申请单位编号
     */
    private Long unitId;

    /**
     * 处置是否到房间 0否 1是
     */
    private Integer isHouse;


}
