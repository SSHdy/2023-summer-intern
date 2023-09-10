package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 查询要下载的文件路径
 * </p>
 *
 * @author zf
 * @since 2019/4/13
 */
@Data
public class ProjectFileVo {

    /**
     * 当前流程
     */
    @ApiModelProperty(value = "当前流程",example = "5")
    @NotNull(message = "当前流程不能为空")
    private Integer step;

    /**
     * 维修申请Id
     */
    @ApiModelProperty(value = "维修申请Id",example = "65")
    @NotNull(message = "维修申请Id不能为空")
    private Long repairApplyId;

}
