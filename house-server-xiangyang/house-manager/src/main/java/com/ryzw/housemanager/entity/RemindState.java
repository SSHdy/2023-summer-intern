package com.ryzw.housemanager.entity;

/**
 * <p>
 *维修提醒状态
 * </p>
 *
 * @author mfl
 * @since 2019/3/4
 */
public enum RemindState {
    /**
     * 未提醒
     */
    NOTREMIND("未提醒",1),

    /**
     * 忽略
     */
    IGNORE("忽略",2),

    /**
     * 处理
     */
    HANDLE("处理",3);

    RemindState(String name,Integer value){
        this.name = name;
        this.value = value;
    }

    private String name;

    private Integer value;

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return  value;
    }
}
