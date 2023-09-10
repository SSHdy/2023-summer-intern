package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *前端传输的uuid
 * </p>
 *
 * @author mfl
 * @since 2018/12/14
 */
@Data
public class UBasicVo extends PageVo{
        /**
         * 房间编号
         */
        @NotNull(message = "房间编号不能为空")
        @ApiModelProperty(value = "房间编号")
        private String houseId;
}
