package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.dozer.Mapping;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     前端传输的审核申请审批参数
 * </p>
 *
 * @author wj
 * @since 2019/3/7 0007
 */
@Data
public class RepairFlowVo {

    /**
     * 维修审批id
     */
    @ApiModelProperty(required = true,value = "维修审批id")
    @NotNull(message = "维修审批id不能为空")
    private Long repairId;

    /**
     * 用户id
     */
    @ApiModelProperty(required = true,value = "用户id")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty(required = true,value = "用户名",example = "王体东")
//    @NotNull(message = "用户名不能为空")
    private String name;

    /**
     * 用户名
     */
    @ApiModelProperty(required = true,value = "用户名")
    @NotNull(message = "用户name不能为空")
    @Mapping(value = "currentUser")
    private String userName;

    /**
     * 手机号
     */
    @ApiModelProperty(required = true,value = "手机号")
    @NotNull(message = "手机号不能为空")
    private String phoneNum;

    /**
     * 流程实例ID
     */
    @ApiModelProperty(required = true,value = "流程实例ID")
    @NotNull(message = "流程实例ID不能为空")
    private String processInstanceId;

    /**
     * 当前执行的任务id
     */
    @ApiModelProperty(required = true,value = "当前执行的任务id")
    @NotNull(message = "当前执行的任务id不能为空")
    private String taskId;

    /**
     * 当前流程步骤编号
     */
    @ApiModelProperty(required = true,value = "当前流程步骤编号")
    @NotNull(message = "当前流程步骤编号不能为空")
    private Integer step;


    /**
     * 连线名称
     */
    @ApiModelProperty(required = true,value = "选择意见不能为空")
    @NotNull(message = "选择意见不能为空")
    private Integer auditState;


    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comment;

    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String repairProject;

    /**
     * 维修概算
     */
    @ApiModelProperty(value = "维修概算")
    private BigDecimal budgetEstimate;

    /**
     * 单位面积价格
     */
    @ApiModelProperty(value = "单位面积价格")
    private BigDecimal unitAreaPrice;

    /**
     * 设计单位
     */
    @ApiModelProperty(value = "设计单位")
    private String designUnit;

    /**
     * 规划许可证号
     */
    @ApiModelProperty(value = "规划许可证号")
    private String planningPermit;

    /**
     * 预算单位
     */
    @ApiModelProperty(value = "预算单位")
    private String auditUnit;

    /**
     * 预算总价
     */
    @ApiModelProperty(value = "预算总价")
    private BigDecimal totalBudgetPrice;

    /**
     * 评审单位
     */
    @ApiModelProperty(value = "评审单位")
    private String accreditationUnit;


    /**
     * 决算金额
     */
    @ApiModelProperty(value = "决算金额")
    private BigDecimal finalSum;


    /**
     * 维修申请扩展字段
     */
    @ApiModelProperty(value = "维修申请扩展字段")
    private Map<String,Object> basicDBObject;

    /**
     * 要删除的文件物理路径地址
     */
    private List<String> physicalAddressList;


    /**
     * 项目负责人
     */
    private List<Long> projectLeaders;


    /**
     * 负责人
     */
    private String responsiblePerson;

    /**
     * 施工单位名称
     */
    private String repairUnitName;

    /**
     * 传给内控的参数
     */
    private RepairHttpResultVo repairHttpResultVo;

}
