package com.ryzw.housemanager.dto;


import lombok.Data;

/**
 * <p>
 * 上报单位分页数据
 * </p>
 *
 * @author zf
 * @since 2019/8/29
 */
@Data
public class ReportUnitDto {

    /**
     * 上报编号
     */
    private Long reportId;

    /**
     * 上报单位
     */
    private String reportUnitName;

    /**
     * 是否上报 0为上报 1已上报
     */
    private Integer isReport;

    /**
     * 上报文件物理路径
     */
    private String reportEnclosurePhysicsUrl;
}
