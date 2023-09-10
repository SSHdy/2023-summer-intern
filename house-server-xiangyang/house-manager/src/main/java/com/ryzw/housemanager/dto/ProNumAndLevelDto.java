package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 * 编制信息和等级信息
 * </p>
 *
 * @author zf
 * @since 2019/3/11
 */
@Data
public class ProNumAndLevelDto {

    /**
     * 单位职级关系表编号
     */
    private Long unitProfessionalId;

    /**
     * 单位编号
     */
    private Long unitId;

    /**
     * 职级Id
     */
    private Integer professionalId;

    /**
     * 职级人数
     */
    private Integer professionalNum;

    /**
     * 等级
     */
    private Integer professionalLevel;

    /**
     * 是否是正科或副科下的市级机关  1是 2否
     */
    private Integer isMunicipal;
}
