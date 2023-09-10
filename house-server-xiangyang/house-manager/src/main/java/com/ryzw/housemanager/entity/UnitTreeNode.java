package com.ryzw.housemanager.entity;

/**
 * <p>
 *左侧单位树枚举
 * </p>
 *
 * @author mfl
 * @since 2019/1/2
 */
public enum UnitTreeNode {
    /**
     * 非单位
     */
    NONUNIT("非单位",0),

    /**
     * 一级单位
     */
    FIRSTUNIT("一级单位",1),

//    /**
//     * 二级单位
//     */
//    TWOUNIT("二级单位",2),
    /**
     * 院落
     */
    YARD("院落",2),
    /**
     * 楼栋
     */
    BUILD("楼栋",3),

    /**
     * 楼层
     */
    FLO("楼层",4),

    /**
     *  房间
     */
    HOUSE("房间",5);



    UnitTreeNode(String name, Integer value){
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
