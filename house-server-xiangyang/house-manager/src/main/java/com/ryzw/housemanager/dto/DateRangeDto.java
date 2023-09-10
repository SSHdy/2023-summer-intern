package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 工作量统计日期范围
 * </p>
 *
 * @author zf
 * @since 2019/7/30
 */
@Data
public class DateRangeDto {

    /**
     * 最大年份
     */
    private String maxYear;

    /**
     * 最小年份
     */
    private String minYear;

    /**
     * 最大月份
     */
    private String maxMonth;

    /**
     * 最小月份
     */
    private String minMonth;

}
