package com.ryzw.housemanager.entity;

/**
 * <p>
 * 树形菜单类型枚举
 * </p>
 *
 * @author mfl
 * @since 2018-12-12
 */
public enum TreeNode {

    /**
     * 院落
     */
    CAUSE("院落",0),
    /**
     * 楼栋
     */
    AMT("楼栋",1),

    /**
     * 楼层
     */
    FLO("楼层",2),

    /**
     * 房间
     */
    HOU("房间",3),

    /**
     * 结束符号，没有实际意义
     */
    END("结束符号",4);

    TreeNode(String name, Integer value){
        this.name = name;
        this.value=value;
    }

    private String name;

    private Integer value;


    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }

}
