package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *行政区划信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/5/6
 */
@Data
public class AdministrativeRegionDto {
    /**
     * 行政区划
     */
    private String regionName;

    /**
     * 院落位置信息
     */
    private List<YardPositionInfoDto> yardPositionInfo;
}
