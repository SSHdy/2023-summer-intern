package com.ryzw.houseauthority.dto;

import lombok.Data;

/**
 * <p>
 *用户详情查询
 * </p>
 *
 * @author mfl
 * @since 2019/7/19
 */
@Data
public class UserDetailDto {

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 登录名
     */
    private String username;

    /**
     * 电话号码
     */
    private String telephone;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 是否冻结
     */
    private Integer isFreeze;

    /**
     * 标题（0对内 1对外）
     */
    private Integer roleBanner;


}
