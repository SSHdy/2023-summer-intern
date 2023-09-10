package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *     内控审批流程传输的参数
 * </p>
 *
 * @author wj
 * @since 2019/4/16 0016
 */
@Data
public class ProcessHttpResultVo {

    /**
     * 申请ID
     */
    @ApiModelProperty(required = true,value = "申请ID")
    private Long applyId;

    /**
     * 内控审批单号
     */
    @ApiModelProperty(required = true,value = "内控审批单号")
    @NotNull(message = "内控审批单号不能为空")
    private String sano;


    /**
     * 审批意见状态
     */
    @ApiModelProperty(required = true,value = "审批意见状态")
    @NotNull(message = "审批意见状态不能为空")
    private Integer auditState;

    /**
     * 办理人
     */
    @ApiModelProperty(required = true,value = "办理人")
    @NotNull(message = "办理人不能为空")
    private String currentUser;


    /**
     * 详细意见备注
     */
    @ApiModelProperty(required = true,value = "详细意见备注")
    private String comment;




}
