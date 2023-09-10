package com.ryzw.housemanager.entity;

/**
 * <p>
 * </p>
 *
 * @author wj
 * @since 2019/2/25 0025
 */
public enum AssetsTypeNode {
    /**
     * 车辆
     */
    VEHICLE("车辆",1),
    /**
     * 房屋
     */
    HOUSE("房屋",2),

    /**
     * 构筑物
     */
    STRUCTURE("构筑物",3),

    /**
     * 家具、用具、装具卡片
     */
    FURNITURE("家具、用具、装具",4),

    /**
     * 通用设备
     */
    GEQUIPMENT("通用设备",5),

    /**
     * 专用设备
     */
    SEQUIPMENT("专用设备",6),

    /**
     * 土地
     */
    LAND("土地",7);


    AssetsTypeNode(String name, Integer value){
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
