package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *单位集合下的楼栋信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/2/14
 */
@Data
public class BuildUnitDto {
    /**
     * 院落id
     */
    private Long yardId;

    /**
     * 楼栋id
     */
    private Long buildId;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 楼层
     */
    private Integer floorName;

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     *申请状态(0未审核，1审核通过，2审核未通过，3审核中)
     */
    private Integer applyStatus;

    /**
     * 处置方式(1转换用途 2出租 3拍卖 4 拆除 5不处置 6出借 7征收)
     */
    private Integer handleWay;
}
