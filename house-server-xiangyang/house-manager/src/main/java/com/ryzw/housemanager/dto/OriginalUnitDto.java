package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *原始单位信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/7/9
 */
@Data
public class OriginalUnitDto {
    /**
     * 单位序号
     */
    private Long originalUnitId;

    /**
     * 单位名称
     */
    private String originalUnitName;

    /**
     * 自用房间编号
     */
    private Long useunitId;
}
