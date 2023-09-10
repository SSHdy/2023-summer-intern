package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *楼层房间信息输入
 * </p>
 *
 * @author mfl
 * @since 2019/7/8
 */
@Data
public class FloorHouseInfoVo {
    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 楼层
     */
    private Integer floorName;

}
