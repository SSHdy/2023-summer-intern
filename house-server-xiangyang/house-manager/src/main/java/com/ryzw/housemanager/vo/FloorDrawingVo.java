package com.ryzw.housemanager.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2018/12/27
 */
@Data
public class FloorDrawingVo {
    /**
     * 楼座编号
     */
    @NotNull(message = "楼座编号不能为空")
    private Long buildId;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 楼层图纸附件地址
     */
    private String floordrawingUrl;

    /**
     * 楼层图纸附件虚拟地址
     */
    private String floorDrawingVirtualUrl;

    /**
     * 上传原文件名
     */
    private String floordrawingFileName;
}
