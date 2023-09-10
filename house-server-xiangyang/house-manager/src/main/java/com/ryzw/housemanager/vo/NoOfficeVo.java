package com.ryzw.housemanager.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author lyx
 * @since 2019/1/18
 */
@Data
public class NoOfficeVo extends PageVo {
    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;

    /**
     * 院落编号
     */
    @ApiModelProperty(value ="院落编号",example = "1")
    private Long yardId;

    /**
     * 楼座编号
     */
    @ApiModelProperty(value = "楼座编号",example = "1")
    private Long buildId;
}
