package com.ryzw.housemanager.entity;

/**
 * <p>
 * 处置方式枚举
 * </p>
 *
 * @author zf
 * @since 2019/5/24
 */
public enum HandleWayEnum {

    /**
     * 转换用途
     */
    TRANSFROMATION("转换用途",1),

    /**
     * 出租
     */
    RENT("出租",2),

    /**
     * 拍卖
     */
    ACUTION("拍卖",3),

    /**
     * 拆除
     */
    DISMANTLE("拆除",4),

    /**
     * 不处置
     */
    NOTHIF("不处置",5),

    /**
     * 出借
     */
    LEND("出借",6),

    /**
     * 征收
     */
    LEVY("征收",7);



    HandleWayEnum(String name, Integer value){
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
