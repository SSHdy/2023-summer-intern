package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 *前端传输的楼栋信息
 * </p >
 * @author mfl
 * @since 2018/12/8
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BuildListVo extends PageVo {

    /**
     * 院落编号
     */
    @ApiModelProperty(required = true,value = "院落编号",example = "1")
    private Long yardId;

    /**
     * 楼座名称
     */
    @ApiModelProperty(required = true,value = "楼座名称",example = "1")
    private String buildName;

}
