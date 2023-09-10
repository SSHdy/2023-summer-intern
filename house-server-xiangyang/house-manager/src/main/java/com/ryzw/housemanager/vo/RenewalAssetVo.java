package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *更新楼栋相关的资产信息
 * </p>
 *
 * @author mfl
 * @since 2019/2/24
 */
@Data
public class RenewalAssetVo {
    /**
     * 资产id
     */
    private String id;
    /**
     * 院落id
     */
    private Long yardId;
    /**
     * 院落id
     */
    private Long buildId;
    /**
     * 楼栋id
     */
    private Integer floorName;
    /**
     * 房间id
     */
    private String houseId;

}
