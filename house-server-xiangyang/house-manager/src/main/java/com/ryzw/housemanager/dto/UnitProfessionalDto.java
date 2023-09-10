package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *单位职称信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/9/5
 */
@Data
public class UnitProfessionalDto {

    /**
     * 单位信息
     */
    private List<YardUnitDto> yardUnitDtoList;

    /**
     * 职称信息
     */
    private List<YardFourDto> yardFourDtoList;

}
