package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 查看分配方案数据
 * </p>
 *
 * @author zf
 * @since 2019/6/11
 */
@Data
public class PlanDataVo extends PageVo {

    /**
     * 配置Id
     */
    @NotNull(message = "配置Id不能为空")
    private Long configureId;

}
