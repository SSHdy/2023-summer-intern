package com.ryzw.housemanager.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 出租用房信息与合计
 * </p>
 *
 * @author zf
 * @since 2019/12/19
 */
@Data
public class RentRoomAndSumDto {

    /**
     * 出租用房分页数据
     */
    private Page<RentRoomReportDto> spareRoomDtoPage;

    /**
     * 合计使用面积
     */
    private BigDecimal houseUseAreaSum;

    /**
     * 合计建筑面积
     */
    private BigDecimal houseBuildAreaSum;
}
