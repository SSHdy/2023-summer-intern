package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *专项巡检信息传参
 * </p>
 *
 * @author mfl
 * @since 2019/10/8
 */
@Data
public class SpecialInspectionVo extends PageVo{

    /**
     * 被检查单位id
     */
    private Long unitId;

    /**
     * 被检查院落id
     */
    private List<Long> yardIds;

    /**
     * 被检查楼栋id
     */
    private List<Long> buildIds;

    /**
     * 被检查的楼层
     */
    private List<Integer> floorNames;

}
