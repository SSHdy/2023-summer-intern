package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *输出单位的无穷树
 * </p>
 *
 * @author mfl
 * @since 2019/3/20
 */
@Data
public class UnitInfinityTreeDto {
    /**
     * 单位序号
     */
    private Long unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 父级单位id
     */
    private Long unitPid;

    /**
     * 单位类型(0非单位  1一级单位  2二级单位)
     */
    private Integer unitClassify;

    /**
     * 子菜单集合
     */
    List<UnitInfinityTreeDto> childrens;

    public UnitInfinityTreeDto(){
        childrens = new LinkedList<>();
    }

}
