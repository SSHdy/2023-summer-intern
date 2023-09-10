package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *闲置用房楼栋信息列表输出
 * </p>
 *
 * @author mfl
 * @since 2019/3/13
 */
@Data
public class SpareBuildDto {
    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 楼层列表
     */
    private List<SpareFloorDto> floorList;
}
