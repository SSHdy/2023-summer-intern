package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2018/12/12
 */
@Data
public class HouseDetailVo {
    /**
     * 房间编号
     */
    @NotNull(message = "房间编号不能为空")
    private String houseId;
}
