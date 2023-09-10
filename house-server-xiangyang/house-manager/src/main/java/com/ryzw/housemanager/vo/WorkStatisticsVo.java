package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * <p>
 *     工作流统计通用条件
 * </p>
 *
 * @author wj
 * @since 2019-07-24 0024
 */
@Data
public class WorkStatisticsVo {

    /**
     * 当前流程
     */
    private Integer step;

    /**
     * 申请时间(月)
     */
    @ApiModelProperty(value = "申请时间(月)", required = true, example = "2019-07")
    private String applyDateMonth;

    /**
     * 申请时间(年)
     */
    @ApiModelProperty(value = "申请时间(年)", required = true, example = "2019")
    private String applyDateYear;

    /**
     * 处置方式
     */
    private Integer handleWay;

    /**
     * 用户可以查看的单位Id集合
     */
    private List<Long> unitIdList;

}
