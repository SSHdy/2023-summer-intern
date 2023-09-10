package com.ryzw.housemanager.entity;

/**
 * <p>
 *      区分领导和一般人员 类型枚举
 * </p>
 *
 * @author wj
 * @since 2019/5/5 0005
 */
public enum LeadingTypeEnum {

    /**
     * 自费维修
     */
    LEADING("领导",0),

    /**
     * 零星维修
     */
    GENERAL("一般人员",1);



    LeadingTypeEnum(String name, Integer value){
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
