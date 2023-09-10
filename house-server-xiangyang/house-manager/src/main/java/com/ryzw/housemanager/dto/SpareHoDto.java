package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *房屋信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/3/13
 */
@Data
public class SpareHoDto {
    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 房间号
     */
    private String houseNumber;
}
