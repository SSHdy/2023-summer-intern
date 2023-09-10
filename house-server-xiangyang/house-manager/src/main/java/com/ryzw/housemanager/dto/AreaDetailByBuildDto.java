package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 每个楼栋下每种房间类型对应的使用面积
 * </p >
 *
 * @author zf
 * @since 2020.4.10
 */
@Data
public class AreaDetailByBuildDto {

    /**
     * 楼栋Id
     */
    private Long buildId;

    /**
     * 楼栋名称
     */
    private String buildName;

    /**
     * 院落位置
     */
    private String yardPosition;

    /**
     * 房间类型对应面积集合
     */
    private List<AreaDetailByHouseDto> areaDetailByHouseDtoList;

}
