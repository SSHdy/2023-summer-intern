package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *房间明细查询条件
 * </p>
 *
 * @author mfl
 * @since 2019/7/24
 */
@Data
public class RoomDetailVo extends PageVo {
    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 使用人姓名
     */
    private String usePerson;

    /**
     * 是否为领导
     */
    private Integer isLeading;

    /**
     * 单位类型
     */
    private Integer unitType;

    /**
     * 单位级别 正县、副县、正科、副科
     */
    private String unitGrade;

    /**
     * 院落Id
     */
    private Long yardId;

    /**
     * 楼栋Id
     */
    private Long buildId;
}
