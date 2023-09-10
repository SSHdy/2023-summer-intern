package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *  树结构中的楼层信息
 * </p>
 *
 * @author mfl
 * @since 2019/2/20
 */

@Data
public class BuildFloorDto {

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 楼层名称
     */
    private List<Integer> floorName;

    /**
     * 地上楼层
     */
    private Integer overgroundFloor;

    /**
     * 地下楼层
     */
    private Integer undergroundFloor;
}
