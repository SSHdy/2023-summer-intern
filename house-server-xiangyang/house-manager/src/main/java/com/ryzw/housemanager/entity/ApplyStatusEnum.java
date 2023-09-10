package com.ryzw.housemanager.entity;

/**
 * <p>
 * </p>
 *
 * @author wj
 * @since 2019/3/29 0029
 */
public enum ApplyStatusEnum {

    /**
     * 未审核
     */
    UNAUDITED("未审核",0),

    /**
     * 审核通过
     */
    AUDITPASS("审核通过",1),

    /**
     * 审核未通过
     */
    AUDITFAILED("审核未通过",2),

    /**
     * 审核中
     */
    INAUDIT("审核中",3);



    ApplyStatusEnum(String name, Integer value){
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
