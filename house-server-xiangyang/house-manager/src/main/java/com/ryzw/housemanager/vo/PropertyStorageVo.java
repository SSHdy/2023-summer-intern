package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 产权信息查询条件
 * </p>
 *
 * @author zf
 * @since 2019/3/11
 */
@Data
public class PropertyStorageVo extends PageVo{
    /**
     *产权编号
     */
    @ApiModelProperty(value = "产权编号",example = "1")
    private String propertyRightId;

    /**
     *院落Id
     */
    @ApiModelProperty(value = "院落Id",example = "1")
    private String yardId;

    /**
     *楼栋Id
     */
    @ApiModelProperty(value = "楼栋Id",example = "1")
    private String buildId;

    /**
     * 产权类型 （1为房产证，2为土地证，3为不动产）
     */
    @ApiModelProperty(value = "产权类型",example = "1")
    @NotNull(message = "产权类型不能为空")
    private Integer propertyType;
}
