package com.ryzw.houseauthority.dto;

import lombok.Data;

/**
 * <p>
 *角色下的菜单详情输出
 * </p>
 *
 * @author mfl
 * @since 2019/3/27
 */
@Data
public class MenuDetailDto {
    /**
     * 菜单编号
     */
    private Integer menuId;

    /**
     * 菜单标题
     */
    private String menuTitle;
}
