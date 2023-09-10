package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *通过配置查询的元单位信息
 * </p>
 *
 * @author mfl
 * @since 2019/7/9
 */
@Data
public class OldConfUnitDto {
    /**
     * 单位序号
     */
    private Long oldUnitId;

    /**
     * 单位名称
     */
    private String oldUnitName;

    /**
     * 配置方案数据单位编号
     */
    private Long configurePlanOldUnitId;
}
