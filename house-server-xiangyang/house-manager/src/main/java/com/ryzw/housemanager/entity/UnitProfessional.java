package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 单位职级关系表
 * </p>
 *
 * @author mfl
 * @since 2019-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UnitProfessional implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单位职级关系表编号
     */
    @TableId(value = "unit_professional_id", type = IdType.AUTO)
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
     * 是否是正科或副科下的市级机关  1是 2否
     */
    private Integer isMunicipal;



}
