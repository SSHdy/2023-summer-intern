package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *流水信息输入
 * </p>
 *
 * @author mfl
 * @since 2019/1/14
 */
@Data
public class OperationFlowVo extends PageVo{

    /**
     * 流水号
     */
    @ApiModelProperty(value = "流水号",example = "1")
    private String serialNumber;

    /**
     * 采购项目编号
     */
    @ApiModelProperty(value = "采购项目编号",example = "1")
    private String purchasingId;

    /**
     * 采购项目名称
     */
    @ApiModelProperty(value = "采购项目名称",example = "1")
    private String purchasingPname;
}
