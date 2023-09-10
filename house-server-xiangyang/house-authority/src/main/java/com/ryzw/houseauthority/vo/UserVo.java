package com.ryzw.houseauthority.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 用于接收用户信息
 * </p>
 *
 * @author lyx
 * @since 2018/12/28
 */
@Data
public class UserVo {
    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号",example = "1")
    private Long userId;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名",example = "乔巴")
    private String name;

    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名",example = "qiaoba")
    private String username;

    /**
     * 电话号码
     */
    @ApiModelProperty(value = "电话号码",example = "13765412345")
    private String phoneNum;

    /**
     * 是否冻结
     */
    @ApiModelProperty(value = "是否冻结",example = "1")
    private Integer isFreeze;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码",example = "1")
    private String password;

    /**
     * 性别
     */
    @ApiModelProperty(value = "是否冻结",example = "1")
    private Integer sex;

    /**
     * 批量userIds数组
     */
    @ApiModelProperty(value = "批量userIds数组",example = "[]")
    private List<Long> userIdList;
}
