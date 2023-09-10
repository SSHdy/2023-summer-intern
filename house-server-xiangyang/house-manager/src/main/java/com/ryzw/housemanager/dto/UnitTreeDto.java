package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 *单位树左侧输出
 * </p>
 *
 * @author mfl
 * @since 2019/1/4
 */
@Data
public class UnitTreeDto {
    /**
     * 单位序号
     */
    @ApiModelProperty(value = "单位序号")
    private Long unitId;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;

    /**
     * 单位子节点数量
     */
    @ApiModelProperty(value = "单位子节点数量")
    private Integer floorNum;

    /**
     * 单位树父节点id
     */
    @ApiModelProperty(value = "单位树父节点id")
    private String unitTreeId;

    public UnitTreeDto(){
        children = new LinkedList<>();
    }
    /**
     * 父菜单下的子菜单集合
     */
    private List<UnitTreeDto> children;
}
