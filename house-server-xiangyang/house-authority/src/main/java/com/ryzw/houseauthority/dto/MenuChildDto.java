package com.ryzw.houseauthority.dto;

import com.ryzw.houseauthority.vo.PageVo;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author lyx
 * @since 2019/1/3
 */
@Data
public class MenuChildDto extends PageVo{
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
     * 菜单代号
     */
    private String menuKey;

    /**
     * 菜单图片路径
     */
    private String menuImgurl;

    /**
     * 菜单图片虚拟路径
     */
    private String menuImgVirtualUrl;

    /**
     * 菜单描述
     */
    private String menuDescribe;

    /**
     * 是否冻结
     */
    private Integer isFreeze;

    /**
     * code
     */
    private String code;

    /**
     * 菜单排序序号
     */
    private Integer menuOrder;

    /**
     * 是否显示(0显示 1不显示)
     */
    private Integer display;

    /**
     * 父菜单下的子菜单集合
     */
    private List<MenuChildDto> children;

    public MenuChildDto(){
        children = new LinkedList<>();
    }
}
