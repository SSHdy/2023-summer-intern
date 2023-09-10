package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *闲置用房前端需要传入的值
 * </p>
 *
 * @author mfl
 * @since 2019/4/17
 */
@Data
public class SpareRoomVo extends PageVo{
    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 楼层
     */
    private Integer floorName;

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 房屋类型编号
     */
    private Integer housetypeId;

    /**
     *权属单位集合
     */
    private List<Long> unitIdList;
}
