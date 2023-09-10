package com.ryzw.housemanager.entity;

/**
 * <p>
 * 处置流程节点
 * </p>
 *
 * @author zf
 * @since 2019/5/24
 */
public enum HandleStepNode {

    /**
     * 单位申请
     */
    UNITAPPLY("单位申请",1),

    /**
     * 申请初审
     */
    INITIALAPPLY("申请初审",2),

    /**
     * 财政或人民政府审批
     */
    GOVERNMENTAPPLY("财政或人民政府审批",3),

    /**
     * 转换用途
     */
    TRANSFROMATION("转换用途",4),

    /**
     * 拍卖
     */
    ACUTION("拍卖",5),

    /**
     * 拆除
     */
    DISMANTLE("拆除",6),

    /**
     * 征收
     */
    LEVY("征收",7);


    HandleStepNode(String name, Integer value){
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
