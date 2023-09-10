package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 房屋类型报表信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/4/18
 */
@Data
public class HouseTypeReportDto {

    /**
     *单位核定编制
     */
    private Integer unitComplement;

    /**
     * 房屋类型报表数量统计相关
     */
    private List<HouseTypeSumDto> houseTypeSumDtoList;

}
