package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *一栋楼下的楼层集合
 * </p>
 *
 * @author mfl
 * @since 2019/8/26
 */
@Data
public class FloorNameVo {
    /**
     * 楼栋id
     */
    private Long buildId;

    /**
     * 变化的楼层
     */
    private List<Integer> floorName;

}
