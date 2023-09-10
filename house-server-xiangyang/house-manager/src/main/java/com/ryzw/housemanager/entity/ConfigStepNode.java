package com.ryzw.housemanager.entity;

/**
 * <p>
 *配置流程节点
 * </p>
 *
 * @author mfl
 * @since 2019/7/15
 */
public enum ConfigStepNode {
    /**
     * 单位申请
     */
    UNITAPPLY("单位申请",1),

    /**
     * 机关管理局用房情况审查
     */
    UNITREVIEW("机关管理局用房情况审查",2),

    /**
     * 拟定分配方案
     */
    DUADPLAN("拟定分配方案",3),

    /**
     * 领导审批
     */
    LEADERAPPROVAL("领导审批",4);

    ConfigStepNode(String name, Integer value){
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
