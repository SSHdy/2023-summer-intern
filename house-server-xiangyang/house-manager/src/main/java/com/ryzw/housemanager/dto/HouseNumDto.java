package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * 返回给前端的房间集合
 * </p >
 *
 * @author zf
 * @since 2018/7/5
 */
@Data
public class HouseNumDto {

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 房间号
     */
    private String houseNumber;
}
