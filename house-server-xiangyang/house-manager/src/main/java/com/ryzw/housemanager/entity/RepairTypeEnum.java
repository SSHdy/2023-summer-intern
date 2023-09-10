package com.ryzw.housemanager.entity;

/**
 * <p>
 *     维修统计用的 维修类型枚举
 * </p>
 *
 * @author wj
 * @since 2019/3/29 0029
 */
public enum RepairTypeEnum {

    /**
     * 自费维修
     */
    EXPENSE("自费维修",0),

    /**
     * 零星维修
     */
    SPORADIC("零星维修",1),

    /**
     * 机关管理局维修
     */
    AUTHORITY("机关管理局维修",2);



    RepairTypeEnum(String name, Integer value){
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
