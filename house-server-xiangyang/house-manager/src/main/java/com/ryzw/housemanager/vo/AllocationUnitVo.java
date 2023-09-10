package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *人工分房单位信息查询条件输入
 * </p>
 *
 * @author mfl
 * @since 2019/6/21
 */
@Data
public class AllocationUnitVo {
    /**
     * 房间编号
     */
    @NotNull(message = "房间编号不能为空")
    private String houseId;

    /**
     * 方案id
     */
    @NotNull(message = "方案id不能为空")
    private Long planId;
}
