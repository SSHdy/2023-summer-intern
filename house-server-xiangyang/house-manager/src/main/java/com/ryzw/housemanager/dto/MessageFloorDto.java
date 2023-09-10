package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 信息档案楼层树
 * </p>
 *
 * @author zf
 * @since 2019/8/15
 */
@Data
public class MessageFloorDto {

    /**
     * 院落Id
     */
    private Long yardId;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 楼座Id
     */
    private Long buildId;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 楼层集合
     */
    private List<Integer> floorList;
}
