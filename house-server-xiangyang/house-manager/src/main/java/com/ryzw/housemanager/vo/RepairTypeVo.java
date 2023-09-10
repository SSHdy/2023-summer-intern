package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 查询维修类型列表参数
 * </p>
 *
 * @author zf
 * @since 2019/4/17
 */
@Data
public class RepairTypeVo {

    /**
     * 维修申请Id
     */
    @NotNull(message = "维修申请Id不能为空")
    @ApiModelProperty(value = "维修申请Id", example = "65")
    private Long repairApplyId;

    /**
     * 维修类型（1自费,2机关管理局）
     */
    @NotNull(message = "维修类型不能为空")
    @ApiModelProperty(value = "维修类型（1自费,2机关管理局）", example = "1")
    private Integer type;
}
