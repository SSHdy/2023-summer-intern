package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 查询所有单位树
 * </p>
 *
 * @author mfl
 * @since 2019/2/19
 */
@Data
public class AllUnitTreeDto {
    /**
     * 单位编号
     */
    @ApiModelProperty(value = "单位编号")
    private Long unitId;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;

    /**
     * 单位类型
     */
    @ApiModelProperty(value = "单位类型")
    private Integer unitClassify;


    /**
     * 子节点数量
     */
    @ApiModelProperty(value = "子节点数量")
    private int unitPcount;
}
