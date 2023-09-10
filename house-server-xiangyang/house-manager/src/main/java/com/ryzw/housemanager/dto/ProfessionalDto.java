package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 * 获取职级判断条件
 * </p>
 *
 * @author zf
 * @since 2020.4.8
 */
@Data
public class ProfessionalDto {

    /**
     * 职务级别
     */
    private Integer professionalLevel;

    /**
     * 单位级别
     */
    private Integer unitGrade;
}
