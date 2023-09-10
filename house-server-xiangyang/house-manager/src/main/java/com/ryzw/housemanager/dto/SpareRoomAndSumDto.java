package com.ryzw.housemanager.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 闲置用房信息与合计
 * </p>
 *
 * @author zf
 * @since 2019/12/19
 */
@Data
public class SpareRoomAndSumDto {

    /**
     * 闲置用房分页数据
     */
    private Page<SpareRoomDto> spareRoomDtoPage;

    /**
     * 合计使用面积
     */
    private BigDecimal houseUseAreaSum;

    /**
     * 合计建筑面积
     */
    private BigDecimal houseBuildAreaSum;
}
