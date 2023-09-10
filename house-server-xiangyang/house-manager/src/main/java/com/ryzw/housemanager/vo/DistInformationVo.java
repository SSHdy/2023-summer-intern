package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *     前端传输的行政区划相关信息
 * </p>
 *
 * @author wj
 * @since 2019-09-04 0004
 */
@Data
public class DistInformationVo {

    /**
     * 行政区划id
     */
    @ApiModelProperty(value = "行政区划id")
    private Integer distInformationId;

    /**
     * 行政区划名称
     */
    @NotNull(message = "行政区划名称不能为空")
    @ApiModelProperty(value = "行政区划名称", example = "宜昌市")
    private String distName;

    /**
     * 行政区划级别
     */
    @NotNull(message = "行政区划级别不能为空")
    @ApiModelProperty(value = "行政区划级别", example = "市（州、盟）")
    private String distLevel;

    /**
     * 行政区划代码
     */
    @NotNull(message = "行政区划代码不能为空")
    @ApiModelProperty(value = "行政区划代码", example = "420101000000")
    private String distCode;

    /**
     * 填表人单位
     */
    @NotNull(message = "填表人单位不能为空")
    @ApiModelProperty(value = "填表人单位", example = "宜昌机关事务管理局")
    private String fillerUnit;

    /**
     * 填表人姓名
     */
    @NotNull(message = "填表人姓名不能为空")
    @ApiModelProperty(value = "填表人姓名", example = "张三")
    private String fillerName;

    /**
     * 联系电话
     */
    @NotNull(message = "联系电话不能为空")
    @ApiModelProperty(value = "联系电话", example = "0717-12345678")
    private String contactNumber;

}
