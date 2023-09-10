package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 查询某个用户所有审批记录
 * </p>
 *
 * @author zf
 * @since 2019/4/30
 */
@Data
public class ApprovalRecordVo extends PageVo {

    /**
     * 用户id
     */
    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private Long userId;
}
