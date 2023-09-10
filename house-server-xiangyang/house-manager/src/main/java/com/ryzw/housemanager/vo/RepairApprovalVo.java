package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *     前端传输的维修审批参数
 * </p>
 *
 * @author wj
 * @since 2019/3/7 0007
 */
@Data
public class RepairApprovalVo extends PageVo {

    @ApiModelProperty(required = true,value = "用户",example = "11")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 申请编号
     */
    @ApiModelProperty(value = "申请编号")
    private String repairApplyNo;


    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称")
    private String repairProject;

    /**
     * 开始申请时间
     */
    @ApiModelProperty(value = "开始申请时间")
    private Date startApplicationTime;

    /**
     * 结束申请时间
     */
    @ApiModelProperty(value = "结束申请时间")
    private Date endApplicationTime;

    /**
     * 当前处理审批的维修申请
     */
    private List<Long> repairIds;
}
