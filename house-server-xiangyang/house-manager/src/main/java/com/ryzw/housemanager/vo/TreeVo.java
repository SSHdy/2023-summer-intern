package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * <p>
 *  树形结果查询字段
 * </p>
 *
 * @author yz
 * @since 2018/12/12
 */
@Data
public class TreeVo {
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
    private Long Id;

    /**
     * 楼层名称
     */
    @ApiModelProperty(value = "楼层名称")
    private Long buildId;


    /**
     * 查询院落ID
     */
    @ApiModelProperty(value = "查询院落ID")
    private Long searchYardId;
}
