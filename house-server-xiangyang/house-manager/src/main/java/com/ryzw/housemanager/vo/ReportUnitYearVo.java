package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 查询所有单位上报文件按年份条件
 * </p>
 *
 * @author zf
 * @since 2019/9/6
 */
@Data
public class ReportUnitYearVo {

    /**
     * 年份
     */
    @NotNull(message = "年份不能为空")
    private Integer reportYear;
}
