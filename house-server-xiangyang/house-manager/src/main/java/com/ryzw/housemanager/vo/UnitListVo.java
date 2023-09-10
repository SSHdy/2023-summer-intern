package com.ryzw.housemanager.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *查询房屋信息前端需要传送的值
 * </p>
 *
 * @author mfl
 * @since 2018/12/26
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UnitListVo extends PageVo{

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;

    /**
     * 类型编号
     */
    @ApiModelProperty(value = "类型编号")
    private Integer unitType;
}
