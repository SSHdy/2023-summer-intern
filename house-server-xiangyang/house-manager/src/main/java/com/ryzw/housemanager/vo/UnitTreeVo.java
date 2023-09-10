package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *单位数传入的参数
 * </p>
 *
 * @author mfl
 * @since 2019/1/2
 */
@Data
public class UnitTreeVo {

    /**
     * 单位序号
     */
    @ApiModelProperty(value = "单位序号")
    private Long unitId;

    /**
     * 节点类型
     */
    @ApiModelProperty(value = "节点类型")
    @NotNull(message = "查询节点类型不能为空")
    private Integer Type;


    /**
     * 节点ID
     */
    @ApiModelProperty(value = "节点ID")
    private String Id;

    @ApiModelProperty(value = "一级单位是否有权限")
    private boolean authority;

    @ApiModelProperty(value = "搜索的单位编号")
    private Long searchUnitId;
}
