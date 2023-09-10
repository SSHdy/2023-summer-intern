package com.ryzw.housemanager.vo;

import com.mongodb.BasicDBObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.dozer.Mapping;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     前端传输的处置审批参数
 * </p>
 *
 * @author wj
 * @since 2019-05-24 0024
 */
@Data
public class HandleFlowVo {


    /**
     * 处置审批id
     */
    @ApiModelProperty(required = true,value = "处置审批id")
    @NotNull(message = "处置审批id不能为空")
    private Long handleId;

    /**
     * 用户id
     */
    @ApiModelProperty(required = true,value = "用户id")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty(required = true,value = "用户名")
    @NotNull(message = "用户name不能为空")
    @Mapping(value = "currentUser")
    private String userName;

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
     * 处置方式
     */
    @ApiModelProperty(value = "处置方式")
    private Integer handleWay;


    /**
     * 处置文件扩展字段
     */
    @ApiModelProperty(value = "处置文件扩展字段")
    private List<BasicDBObject> basicDBObject;

    /**
     * 转换后用途
     */
    @ApiModelProperty(value = "转换后用途")
    private String ponUse;

    /**
     * 中标人
     */
    @ApiModelProperty(value = "中标人")
    private String winningBidder;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contacts;

    /**
     * 联系电话
     */
    @ApiModelProperty(value = "联系电话")
    private String contactNumber;

    /**
     * 中标金额
     */
    @ApiModelProperty(value = "中标金额")
    private String winningAmount;

    /**
     * 代理机构
     */
    @ApiModelProperty(value = "代理机构")
    private String agency;

    /**
     * 代理联系方式
     */
    @ApiModelProperty(value = "代理联系方式")
    private String agentContact;

    /**
     * 实施单位
     */
    @ApiModelProperty(value = "实施单位")
    private String implementationUnit;

    /**
     * 拆除时间
     */
    @ApiModelProperty(value = "拆除时间")
    private String demolitionTime;

    /**
     * 房屋征收单位
     */
    @ApiModelProperty(value = "房屋征收单位")
    private String houseLevyUnit;

    /**
     * 征收补偿款
     */
    @ApiModelProperty(value = "征收补偿款")
    private BigDecimal levyCompensation;

}
