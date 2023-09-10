package com.ryzw.houseauthority.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *按照用户权限获取有效的菜单列表
 * </p>
 *
 * @author mfl
 * @since 2019/2/15
 */
@Data
public class UserMenuListDto {
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 菜单编号
     */
    private Integer menuId;

    /**
     * 菜单标题
     */
    private String menuTitle;

    /**
     * 菜单对应的页面url
     */
    private String menuUrl;

    /**
     * 父级编码
     */
    private Integer menuPid;

    /**
     * 菜单key
     */
    private String menuKey;

    /**
     * 菜单图片虚拟路径
     */
    private String menuImgVirtualUrl;

    /**
     * 菜单描述
     */
    private String menuDescribe;

    /**
     * 是否显示(0显示 1不显示)
     */
    private Integer display;

}
