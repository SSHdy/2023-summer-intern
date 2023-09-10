package com.ryzw.houseauthority.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *<p>
 *基类，前端只需要传输id
 * </p>
 *
 * @author mfl
 * @since 2018/12/8
 */
@Data
public class BasicVo<T>{
    @NotNull(message = "ID不能为空")
    private T id;
}
