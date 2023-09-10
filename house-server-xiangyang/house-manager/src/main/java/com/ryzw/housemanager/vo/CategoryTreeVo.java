package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 资产类目树
 * </p>
 *
 * @author yz
 * @since 2019/2/28
 */
@Data
public class CategoryTreeVo {

    @ApiModelProperty(value = "父节点编码", example = "3270000")
    private String categoryPcode;

    @NotNull(message = "资产类别不能为空!")
    @ApiModelProperty(value = "资产类型", example = "5", required = true)
    private Integer type;
}

