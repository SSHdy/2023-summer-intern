package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 资产统计Vo
 * </p>
 *
 * @author yz
 * @since 2019/2/27
 */
@Data
public class AssetsStatisticsVo {
    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private Date beginDate;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private Date endDate;
}
