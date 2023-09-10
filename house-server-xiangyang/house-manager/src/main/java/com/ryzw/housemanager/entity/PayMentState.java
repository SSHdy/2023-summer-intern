package com.ryzw.housemanager.entity;

/**
 * <p>
 *付款状态
 * </p>
 *
 * @author mfl
 * @since 2019/4/9
 */
public enum PayMentState {
    /**
     * 未付款
     */
    NOTPAY("未付款",1),

    /**
     * 付款中
     */
    PAY("付款中",2),

    /**
     * 已付款
     */
    PAYEND("已付款",3);

    PayMentState(String name,Integer value){
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
