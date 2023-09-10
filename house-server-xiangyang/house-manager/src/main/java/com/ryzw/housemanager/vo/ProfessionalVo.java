package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *前端传输的职称信息
 * </p>
 *
 * @author mfl
 * @since 2018/12/17
 */
@Data
public class ProfessionalVo extends PageVo{

    /**
     * 职称编号
     */
    @ApiModelProperty(value = "职称编号")
    private Integer professionalId;

    /**
     * 职称名字
     */
    @ApiModelProperty(value = "职称名字")
    private String professionalName;

    /**
     * 职务级别
     */
    @ApiModelProperty(value = "职务级别")
    private String professionalLevel;

    /**
     * 规定面积
     */
    @ApiModelProperty(value = "规定面积")
    private Float specifiedArea;
}
