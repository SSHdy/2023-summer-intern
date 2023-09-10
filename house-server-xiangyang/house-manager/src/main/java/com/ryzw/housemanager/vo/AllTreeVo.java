package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 所有的树节点的树形结构
 * </p>
 *
 * @author yz
 * @since 2019/2/19
 */
@Data
public class AllTreeVo {
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
     * 楼栋ID
     */
    @ApiModelProperty(value = "楼栋ID")
    private Long BuildId;

    /**
     * 查询院落ID
     */
    @ApiModelProperty(value = "查询院落ID")
    private Long searchYardId;
}
