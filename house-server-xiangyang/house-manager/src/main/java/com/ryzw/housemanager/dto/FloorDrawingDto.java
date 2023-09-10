package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *  用于存FloorDrawingUrlList的DTO
 * </p>
 *
 * @author lyx
 * @since 2018/12/14
 */
@Data
public class FloorDrawingDto {
    /**
     * 楼层图纸编号
     */
    private Long floordrawingId;

    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 楼层图纸附件地址
     */
    private String floordrawingUrl;

    /**
     *楼层图纸附件虚拟地址
     */
    private String floordrawingVirtualUrl;

    /**
     * cad文件名
     */
    private String floordrawingFileName;

    /**
     * 楼层
     */
    private Integer floor;
}
