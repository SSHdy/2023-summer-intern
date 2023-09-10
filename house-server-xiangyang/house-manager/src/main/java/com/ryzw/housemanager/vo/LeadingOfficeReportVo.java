package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *领导人员办公室报表查询条件
 * </p>
 *
 * @author mfl
 * @since 2019/5/8
 */
@Data
public class LeadingOfficeReportVo {
    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id",example = "8")
    private Long unitId;

    /**
     * 在职情况编号
     */
    @ApiModelProperty(value = "在职情况编号",example = "0")
    private Integer leadingofficeWorkStatueId;

    /**
     * 使用人姓名
     */
    @ApiModelProperty(value = "使用人姓名",example = "***")
    private String leadingofficeName;
}
