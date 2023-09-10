package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 房间明细汇总数据
 * </p >
 *
 * @author zf
 * @since 2020.4.9
 */
@Data
public class HouseDetailByUnitDto {

    /**
     * 单位Id
     */
    private Long unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 单位性质名称
     */
    private String unitTypeName;

    /**
     * 单位级别
     */
    private String unitGrade;

    /**
     * 单位总编制人数
     */
    private Integer unitComplement;

    /**
     * 面积明细数据
     */
    List<AreaDetailDto> areaDetailDtoList;

}
