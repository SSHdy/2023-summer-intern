package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *通过配置查询的新单位信息
 * </p>
 *
 * @author mfl
 * @since 2019/7/9
 */
@Data
public class NewConfUnitDto {
    /**
     * 单位序号
     */
    private Long newUnitId;

    /**
     * 单位名称
     */
    private String newUnitName;

    /**
     * 配置方案数据单位编号
     */
    private Long configurePlanNewUnitId;
}
