package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2019/8/22
 */
@Data
public class FloorHouseListVo {
    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 楼层
     */
    private List<Integer> floorNameList;
}
