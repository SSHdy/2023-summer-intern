package com.ryzw.houseauthority.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *  用于接收批量删除数组
 * </p>
 *
 * @author lyx
 * @since 2018/12/27
 */
@Data
public class MenuVo{
    /**
     * 菜单编号
     */
    @ApiModelProperty(value = "菜单编号",example = "1")
    private Integer menuId;

    /**
     * 菜单标题
     */
    @ApiModelProperty(value = "菜单标题",example = "资产管理")
    private String menuTitle;

    /**
     * 是否冻结
     */
    @ApiModelProperty(value = "是否冻结",example = "0代表非冻结，1代表冻结")
    private Integer isFreeze;

    /**
     * 批量MenuIds数组
     */
    @ApiModelProperty(value = "批量MenuIds数组",example = "[]")
    private List<Integer> menuIdList;
}
