package com.ryzw.houseauthority.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *  用于菜单查询所需要的条件
 * </p>
 *
 * @author lyx
 * @since 2018/12/27
 */
@Data
public class MenuListVo extends PageVo{
    /**
     * 菜单标题
     */
    @ApiModelProperty(value = "菜单标题",example = "***")
    private String menuTitle;

    /**
     * 是否冻结
     */
    @ApiModelProperty(value = "是否冻结",example = "0代表非冻结，1代表冻结")
    private Integer isFreeze;
}
