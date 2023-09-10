package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 房屋租赁信息列表前端传入的信息
 * </p>
 *
 * @author mfl
 * @since 2019/3/13
 */
@Data
public class RentPageVo extends PageVo {
    /**
     * 承租方
     */
    @ApiModelProperty(value = "承租方")
    private String renter;

    /**
     * 租赁状态（0退租，1租赁，null全部）
     */
    @ApiModelProperty(value = "租赁状态（0退租，1租赁，null全部）")
    private Integer rentState;

    /**
     * 表类型 1出租 2出借
     */
    @ApiModelProperty(value = "1")
    @NotNull(message = "表类型不能为空")
    private Integer type;
}
