package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 查询上报单位条件
 * </p>
 *
 * @author zf
 * @since 2019/8/29
 */
@Data
public class ReportUnitVo extends PageVo {

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 年份
     */
    @NotNull(message = "年份不能为空")
    private Integer reportYear;

    /**
     * 上报状态 0未上报  1已上报
     */
    private Integer state;
}
