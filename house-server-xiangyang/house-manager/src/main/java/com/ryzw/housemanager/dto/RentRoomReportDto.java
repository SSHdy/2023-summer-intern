package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 * 租赁房屋报表信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/4/17
 */
@Data
public class RentRoomReportDto {
    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 楼座名称
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
     * 套内面积
     */
    private Float houseUseArea;

    /**
     * 建筑面积
     */
    private Float houseBuildArea;

    /**
     * 联系人
     */
    private String contacts;

    /**
     * 联系方式
     */
    private String contactPhone;

    /**
     * 承租年限
     */
    private Double lesseeYears;

    /**
     * 楼栋系数
     */
    private Double buildCoe;

    /**
     * 是否用建筑面积   0否  1是
     */
    private Integer useBuildArea;
}
