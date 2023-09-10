package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *房间信息
 * </p>
 *
 * @author zf
 * @since 2019/3/25
 */
@Data
public class HouseRentDto {

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 房间号
     */
    private String houseNumber;
}
