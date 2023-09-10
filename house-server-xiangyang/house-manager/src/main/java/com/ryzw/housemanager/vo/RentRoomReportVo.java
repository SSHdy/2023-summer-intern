package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *租赁房屋报表查询条件
 * </p>
 *
 * @author mfl
 * @since 2019/4/17
 */
@Data
public class RentRoomReportVo extends PageVo {
    /**
     * 院落编号
     */
    private List<Long> yardIds;

    /**
     * 楼座编号
     */
    private List<Long> buildIds;

    /**
     * 楼层
     */
    private List<Integer> floorNames;

    /**
     * 房间编号
     */
    private List<String> houseIds;

    /**
     * 房屋类型编号
     */
    private Integer houseTypeId;

    /**
     * 单位Id
     */
    private List<Long> unitIdList;
}
