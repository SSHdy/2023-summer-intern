package com.ryzw.houseauthority.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author lyx
 * @since 2019/1/10
 */
@Data
public class NewPassWordVo {
    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名",example = "qiaoba")
    private String username;

    /**
     * 旧密码
     */
    @ApiModelProperty(value = "旧密码",example = "***")
    private CharSequence password;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码",example = "***")
    private String newPassword;
}
