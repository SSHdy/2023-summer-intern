package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 * 退租修改房间颜色
 * </p>
 *
 * @author zf
 * @since 2019/9/2
 */
@Data
public class HouseColorDto {

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 颜色类型
     */
    private String colorType;
}
