package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *市（州、盟）办公用房信息报表查询第一块信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/9/4
 */
@Data
public class YardOneDto {
    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 区域名称
     */
    private String regionName;

    /**
     * 院落位置
     */
    private String yardPosition;

    /**
     * 总用地面积
     */
    private Double siteArea;

    /**
     * 总建筑面积
     */
    private Double totalCoveredArea;
}
