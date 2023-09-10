package com.ryzw.houseprocess;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author wj
 * @since 2019-05-24 0024
 */
@Data
public class HandleWorkFlow {

    /**
     * 处置申请ID
     */
    private Long handleId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 当前用户名
     */
    private String currentUser;


    /**
     * 当前执行的任务id
     */
    private String taskId;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 连线名称
     */
    private Integer auditState;

    /**
     * 处置方式
     */
    private Integer handleWay;

    /**
     * 备注
     */
    private String comment;

    /**
     * 所有拥有财政或人民政府审批权限的用户ID
     */
    private List<String> gtUserIds;

    /**
     * 所有拥有转换用途权限的用户ID
     */
    private List<String> conversionUserIds;

    /**
     * 所有拥有拍卖权限的用户ID
     */
    private List<String> auctionUserIds;

    /**
     * 所有拥有拆除权限的用户ID
     */
    private List<String> dismantleUserIds;

    /**
     * 征收权限的用户ID
     */
    private List<String> collectionUserIds;

}
