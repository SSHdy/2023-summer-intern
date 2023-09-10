package com.ryzw.houseauthority.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 菜单模块表
 * </p>
 *
 * @author yz
 * @since 2018-12-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 菜单编号
     */
    @TableId(value = "menu_id", type = IdType.AUTO)
    @ApiModelProperty(value = "菜单编号",example = "1")
    private Integer menuId;

    /**
     * 菜单标题
     */
    @ApiModelProperty(value = "菜单编号",example = "abc")
    private String menuTitle;

    /**
     * 菜单对应的页面url
     */
    @ApiModelProperty(value = "菜单对应的页面url",example = "/house")
    private String menuUrl;

    /**
     * 父级编码
     */
    @ApiModelProperty(value = "父级编码",example = "1")
    private Integer menuPid;

    /**
     * 菜单代号
     */
    @ApiModelProperty(value = "菜单代号",example = "2")
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
    @ApiModelProperty(value = "是否冻结",example = "0")
    private Integer isFreeze;

    /**
     * code
     */
    @ApiModelProperty(value = "code",example = "/user/add")
    private String code;

    /**
     * 菜单排序序号
     */
    private Integer menuOrder;

    /**
     * 是否显示(0显示 1不显示)
     */
    private Integer display;
}
