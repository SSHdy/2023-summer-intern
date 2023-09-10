package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>
 * 修改维修模块面积参数
 * </p>
 *
 * @author zf
 * @since 2019/4/2
 */
@Data
public class RepairModuleUpdateVo {

    /**
     * 维修模块Id
     */
    @NotNull(message = "维修模块Id不能为空")
    @ApiModelProperty(value ="维修模块Id",required = true,example = "8")
    private Long repairModuleId;

    /**
     * 维修面积
     */
    @ApiModelProperty(value ="维修面积",example = "5")
    private BigDecimal repairModuleArea;
}
