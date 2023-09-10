package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *超标房间信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/4/25
 */
@Data
public class RecordDto {
    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 楼栋名称
     */
    private String buildName;

    /**
     * 楼层
     */
    private Integer floorName;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 房间使用人数
     */
    private Integer roomNum;

    /**
     * 套内面积
     */
    private Float houseUseArea;

    /**
     * 超标面积
     */
    private Float excessiveArea;

    /**
     * 超标比例
     */
    private Float excessiveProportion;

    /**
     * 是否备案(0未备案  1已备案)
     */
    private Integer isRecord;


}
