package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *专项巡查查询条件
 * </p>
 *
 * @author mfl
 * @since 2019/10/8
 */
@Data
public class SpecialListVo extends PageVo {
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
     * 单位id
     */
    private Long unitId;
}
