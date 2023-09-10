package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2019/1/16
 */
@Data
public class AssetsDetailVo {
    /**
     * 资产id
     */
    @NotNull(message = "资产id不能为空")
    private Object _id;
}
