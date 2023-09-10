package com.ryzw.housemanager.dto;

import lombok.Data;
import org.dozer.Mapping;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *    查询所有菜单列表输出
 * </p>
 *
 * @author wj
 * @since 2019-05-30 0030
 */
@Data
public class MenuChildAllDto {
    /**
     * 菜单代号
     */
    @Mapping("menuKey")
    private String key;

    /**
     * 菜单标题
     */
    @Mapping("menuTitle")
    private String name;

    /**
     * 菜单对应的页面url
     */
    @Mapping("menuUrl")
    private String path;

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
     * 父菜单下的子菜单集合
     */
    private List<MenuChildAllDto> children;

    public MenuChildAllDto(){
        children = new LinkedList<>();
    }
}
