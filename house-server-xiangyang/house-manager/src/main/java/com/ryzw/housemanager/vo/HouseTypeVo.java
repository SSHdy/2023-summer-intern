package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *房屋类型报表查询
 * </p>
 *
 * @author mfl
 * @since 2019/5/8
 */
@Data
public class HouseTypeVo {
    /**
     * 房屋类型id
     */
    @NotNull(message = "房屋类型id不能为空")
    private Integer housetypeId;

    /**
     * 楼栋id
     */
    private Long buildId;
}
