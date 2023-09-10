package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *     配置方案列表参数
 * </p>
 *
 * @author wj
 * @since 2019-06-17 0017
 */
@Data
public class ConfigurePlanVo{

    /**
     * 配置申请ID
     */
    @NotNull(message = "配置申请ID不能为空")
    private Long configureId;

}
