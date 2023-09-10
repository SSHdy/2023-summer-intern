package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.entity.Unit;
import com.ryzw.housemanager.entity.UnitProfessional;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 新增单位职级参数信息
 * </p>
 *
 * @author zf
 * @since 2019/12/11
 */
@Data
public class UnitProfessAddDto {

    /**
     * 单位信息
     */
    private Unit unit;

    /**
     * 职称信息
     */
    private List<UnitProfessional> unitProfessionalList;

}
