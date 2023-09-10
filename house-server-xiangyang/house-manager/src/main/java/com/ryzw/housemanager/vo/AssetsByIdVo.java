package com.ryzw.housemanager.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *     操作详情资产，前端需要传输的参数
 * </p>
 *
 * @author wj
 * @since 2019/1/15 0015
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetsByIdVo {

    /**
     * 采购项目编号
     */
    @NotNull(message = "purchasingId不能为空")
    private String purchasingId;
}
