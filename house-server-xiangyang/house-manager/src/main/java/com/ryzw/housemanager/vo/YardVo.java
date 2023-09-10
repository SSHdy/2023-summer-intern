package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 院落VO类
 * </p>
 *
 * @author yz
 * @since 2018/12/14
 */
@Data
public class YardVo extends PageVo {
    /**
     * 院落编号
     */
    @NotNull(message = "院落编号不能为空")
    @ApiModelProperty(value = "院落编号")
    private Long yardId;


    /**
     *类型 1院落  2土地
     */
    @NotNull(message = "类型不能为空")
    @ApiModelProperty(value = "类型 1院落 2土地")
    private Integer type;
}
