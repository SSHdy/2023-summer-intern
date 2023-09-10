package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 产权类型查询条件
 * </p>
 *
 * @author zf
 * @since 2019/3/12
 */
@Data
public class PropertyRightTypeVo extends PageVo{

    /**
     * 产权类型 （1为房产证，2为土地证，3为不动产）
     */
    @ApiModelProperty(value = "产权类型",example = "1")
    private Integer propertyType;

}
