package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 *     前端传输的流程权限参数
 * </p>
 *
 * @author wj
 * @since 2019/4/19 0019
 */
@Data
public class ActivityAuthorityVo {

    /**
     * 申请步骤
     */
    @NotNull(message = "申请步骤集合不能为空")
    @ApiModelProperty(value = "申请步骤", required = true)
    private List<Integer> steps;

    /**
     * 拥有审批权限的用户ID
     */
    @NotNull(message = "拥有项批权限的用户ID不能为空")
    @ApiModelProperty(value ="拥有项批权限的用户ID", required = true)
    private String  userId;


    /**
     * 移除申请步骤权限
     */
    @ApiModelProperty(value = "移除申请步骤权限")
    private List<Integer> removeSteps;

}
