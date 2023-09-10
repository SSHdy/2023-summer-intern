package com.ryzw.houseauthority.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *  用于接收新增用户勾选角色的类
 * </p>
 *
 * @author mfl
 * @since 2018/12/28
 */
@Data
public class UserRoleVo {
    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号",example = "1")
    private Long userId;

    /**
     * 用户姓名
     */
    @ApiModelProperty(value = "用户姓名",example = "柯尔")
    private String name;

    /**
     * 登录名
     */
    @ApiModelProperty(value = "登录名",example = "cole")
    private String username;

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
     * 所属单位
     */
    @ApiModelProperty(value = "所属单位",example = "105")
    private Long unitId;


    /**
     * 角色编号
     */
    @ApiModelProperty(value = "角色编号",example = "[1,2]")
    private List<Integer> roleIdList;

    /**
     * 维修编号
     */
    @ApiModelProperty(value = "维修编号",example = "[1,2]")
    private List<Long> repairIdList;

    /**
     * 添加维修缓存的集合
     */
    @ApiModelProperty(value = "添加缓存的集合",example = "[1,2]")
    private List<Long> addRepairRight;

    /**
     * 删除维修缓存的集合
     */
    @ApiModelProperty(value = "删除缓存的集合",example = "[1,2]")
    private List<Long> deleteRepairRight;

    /**
     * 处置流程编号
     */
    @ApiModelProperty(value = "处置流程编号",example = "[1,2]")
    private List<Long> handleIdList;

    /**
     * 添加处置管理缓存的集合
     */
    @ApiModelProperty(value = "添加处置管理缓存的集合",example = "[1,2]")
    private List<Long> addHandleRight;

    /**
     * 删除处置管理缓存的集合
     */
    @ApiModelProperty(value = "删除处置管理缓存的集合",example = "[1,2]")
    private List<Long> deleteHandleRight;

    /**
     * 处置流程编号
     */
    @ApiModelProperty(value = "处置流程编号",example = "[1,2]")
    private List<Long> configIdList;

    /**
     * 添加配置管理缓存的集合
     */
    @ApiModelProperty(value = "添加配置管理缓存的集合",example = "[1,2]")
    private List<Long> addConfigRight;

    /**
     * 删除配置管理缓存的集合
     */
    @ApiModelProperty(value = "删除配置管理缓存的集合",example = "[1,2]")
    private List<Long> deleteConfigRight;

    /**
     * 添加角色的集合
     */
    @ApiModelProperty(value = "添加角色的集合",example = "[1,2]")
    private List<Integer> addRoleRight;

    /**
     * 删除角色的集合
     */
    @ApiModelProperty(value = "删除角色的集合",example = "[1,2]")
    private List<Integer> deleteRoleRight;


}
