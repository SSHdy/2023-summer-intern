package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * <p>
 *付款对象
 * </p>
 *
 * @author yz
 * @since 2019/4/23
 */
@Data
public class ProcessPayHttpResultVo extends ProcessHttpResultVo {
    /**
     * 付款节点
     */
    @Size(min = 1, message = "付款节点不能为空")
    @NotNull(message = "付款节点不能为空")
    @ApiModelProperty(required = true,value = "付款节点")
    private List<RepairPaymentNodeVo> repairPaymentNodes;
}
