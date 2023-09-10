package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *设备信息的修改添加，前端需要传入的参数
 * </p>
 *
 * @author lyx
 * @since 2019/1/15
 */
@Data
public class EquipmentVo {
    /**
     * 设备编号
     */
    private Long equipmentId;

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 楼座编号
     */
    private List<Long> buildIdList;

    /**
     * 设备名称
     */
    private String equipmentName;

    /**
     * 设备数量
     */
    private Integer equipmentNum;

    /**
     * 负责人
     */
    private String equipmentCharge;

    /**
     * 负责人联系方式
     */
    private String equipmentContact;

    /**
     * 设备购入时间
     */
    private Date equipmentTime;

    /**
     * 设备唯一编号
     */
    private String equipmentCode;

    /**
     * 备注
     */
    private String equipmentRemark;

    /**
     * 设备型号
     */
    private String equipmentType;

    /**
     * 设备品牌
     */
    private String equipmentBrand;

}
