package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

/**
 * <p>
 *     前端传输的维修申请信息
 * </p>
 *
 * @author wj
 * @since 2019/3/4 0004
 */
@Data
public class RepairListVo extends PageVo {

    @ApiModelProperty(required = true,value = "用户",example = "13")
    private Long userId;

    /**
     * 申请编号
     */
    @ApiModelProperty(value = "申请编号")
    private String repairApplyNo;

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
     * 申请状态(0未审核  1未审核通过  2审核通过)
     */
    @ApiModelProperty(value = "申请状态")
    private Integer applyStatus;

    /**
     * 维修内容
     */
    @ApiModelProperty(value = "维修内容")
    private String repairTarget;


}
