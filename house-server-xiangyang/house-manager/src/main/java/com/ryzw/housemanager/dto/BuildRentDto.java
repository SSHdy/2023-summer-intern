package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *楼座下的楼层
 * </p>
 *
 * @author zf
 * @since 2019/3/25
 */
@Data
public class BuildRentDto {

    /**
     * 楼层集合
     */
    private List<FloorRentDto> floorNameList;

    /**
     * 楼栋名称
     */
    private String buildName;

    /**
     * 楼座编号
     */
    private Long buildId;
}
