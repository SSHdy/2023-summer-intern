package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *闲置用房信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/3/15
 */
@Data
public class SpareHouseDto {
    /**
     * 房间编号
     */
    private String id;

    /**
     * 房间号
     */
    private String name;

    /**
     * 父菜单下的子菜单集合
     */
    private List<SpareHouseDto> children;

    public SpareHouseDto(){
        children = new LinkedList<>();
    }
}
