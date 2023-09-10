package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *前端需要传送的区域信息
 * </p>
 *
 * @author mfl
 * @since 2019/6/14
 */
@Data
public class RegionVo extends PageVo {
    /**
     * 区域id
     */
    private Integer regionId;

    /**
     * 区域名称
     */
    private String regionName;
}
