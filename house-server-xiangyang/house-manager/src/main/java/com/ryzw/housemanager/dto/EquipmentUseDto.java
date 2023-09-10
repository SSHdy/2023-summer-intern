package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author lyx
 * @since 2019/1/16
 */
@Data
public class EquipmentUseDto {
    private Long equipmentuseId;

    /**
     * buildId
     */
    private Long buildId;

    /**
     * 设备id
     */
    private Long equipmentId;

    /**
     * buildname
     */
    private String buildName;

    public EquipmentUseDto() {
    }
}
