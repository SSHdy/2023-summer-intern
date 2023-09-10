package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * 查询档案列表条件
 * </p >
 *
 * @author zf
 * @since 2019/8/7
 */
@Data
public class BuildAndYardVo extends PageVo {

    /**
     * 院落Id集合
     */
    private List<Long> yardIdList;

    /**
     * 楼座Id集合
     */
    private List<Long> buildIdList;

    /**
     *院落Id
     */
    private Long yardId;

    /**
     * 楼座Id
     */
    private Long buildId;

    /**
     * 楼层
     */
    private Integer floorName;
}
