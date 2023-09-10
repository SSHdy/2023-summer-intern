package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *     内控预算明细所需要参数
 * </p>
 *
 * @author wj
 * @since 2019-09-25 0025
 */
@Data
public class BudgetDetailsVo {

    /**
     * 用户名
     */
    @ApiModelProperty(required = true,value = "用户名",example = "王体东")
    @NotNull(message = "用户名不能为空")
    private String name;

    /**
     * 电话号码
     */
    @ApiModelProperty(required = true,value = "电话号码",example = "18986783975")
    @NotNull(message = "电话号码不能为空")
    private String phoneNum;

    /**
     * 预算项目名称
     */
    private String search;


}
