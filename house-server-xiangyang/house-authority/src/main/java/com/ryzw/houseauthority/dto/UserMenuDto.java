package com.ryzw.houseauthority.dto;

import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *用户角色查询
 * </p>
 *
 * @author mfl
 * @since 2019/1/30
 */
@Data
public class UserMenuDto {

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
     * 菜单图片路径
     */
    private String menuImgurl;

    /**
     * 菜单描述
     */
    private String menuDescribe;

    /**
     * 父菜单下的子菜单集合
     */
    private List<UserMenuDto> children;

    public UserMenuDto(){
        children = new LinkedList<>();
    }
}
