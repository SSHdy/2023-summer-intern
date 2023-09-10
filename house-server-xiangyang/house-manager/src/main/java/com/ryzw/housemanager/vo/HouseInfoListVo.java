package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *房屋信息详情输入
 * </p>
 *
 * @author mfl
 * @since 2019/1/17
 */
@Data
public class HouseInfoListVo extends PageVo{
    /**
     * 院落编号
     */
    @ApiModelProperty(value = "院落编号")
    private Long yardId;

    /**
     * 院落位置
     */
    @ApiModelProperty(value ="院落位置")
    private String yardPosition;
}
