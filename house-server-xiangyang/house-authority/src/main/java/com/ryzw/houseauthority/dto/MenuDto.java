package com.ryzw.houseauthority.dto;

import com.ryzw.houseauthority.vo.PageVo;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author lyx
 * @since 2018/12/27
 */
@Data
public class MenuDto extends PageVo {
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
     * 菜单代号
     */
    private String menuKey;


    /**
     * 是否冻结
     */
    private Integer isFreeze;
}
