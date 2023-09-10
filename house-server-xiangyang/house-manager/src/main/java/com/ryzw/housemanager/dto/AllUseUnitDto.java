package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * 所有在使用房间的单位数据
 * </p >
 *
 * @author zf
 * @since 2020.4.9
 */
@Data
public class AllUseUnitDto {

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
}
