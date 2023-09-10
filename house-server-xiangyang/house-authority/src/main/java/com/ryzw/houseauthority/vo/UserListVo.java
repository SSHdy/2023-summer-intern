package com.ryzw.houseauthority.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 用于接收用户查询参数
 * </p>
 *
 * @author lyx
 * @since 2018/12/28
 */
@Data
public class UserListVo extends PageVo{
    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名",example = "***")
    private String username;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名",example = "草帽")
    private String name;

    /**
     * 是否冻结
     */
    @ApiModelProperty(value = "是否冻结",example = "1")
    private Integer isFreeze;
}
