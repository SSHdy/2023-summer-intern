package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 树返回对象
 * </p>
 *
 * @author yz
 * @since 2019/3/19
 */
@Data
public class TreeDto {

    /**
     * 编号
     */
    private Object Id;

    /**
     * 名称
     */
    private Object Name;

    /**
     * 子节点
     */
    private List<TreeDto> children;


    /**
     * 父节点
     */
    private Object pId;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 是否显示 true显示
     */
    private String disabled;


    public TreeDto() {
        children = new ArrayList<>();
    }
}
