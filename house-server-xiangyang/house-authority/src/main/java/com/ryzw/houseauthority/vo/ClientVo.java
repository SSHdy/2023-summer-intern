package com.ryzw.houseauthority.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 判断clientId是否重复
 * </p>
 *
 * @author zf
 * @since 2019/6/28
 */
@Data
public class ClientVo {

    /**
     * 授权编号
     */
    private Long oauthDetailId;

    /**
     * clientId
     */
    @NotNull(message = "clientId不能为空")
    private String clientId;
}
