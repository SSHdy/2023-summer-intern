package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *单位超标信息查询输出
 * </p>
 *
 * @author mfl
 * @since 2019/7/24
 */
@Data
public class ExcessInfoDto {
    /**
     * 单位序号
     */
    private Long unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 普通人员办公室总超标面积
     */
    private Float tExceedArea;

    /**
     * 普通人员办公室总的使用面积
     */
    private Float tUseArea;

    /**
     * 普通人员办公室总核定面积
     */
    private Float tSpecifiedArea;

    /**
     * 普通人员办公室超标比例
     */
    private Float tExcessRatio;

    /**
     * 领导办公室总超标面积
     */
    private Float pExceedArea;

    /**
     * 领导办公室总的使用面积
     */
    private Float pEseArea;

    /**
     * 领导办公室总核定面积
     */
    private Float pSpecifiedArea;

    /**
     * 领导办公室超标比例
     */
    private Float pExcessRatio;
}
