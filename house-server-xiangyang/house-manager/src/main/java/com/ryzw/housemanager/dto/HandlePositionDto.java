package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 查询可以处置的位置
 * </p>
 *
 * @author zf
 * @since 2018/5/27
 */
@Data
public class HandlePositionDto {

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 楼座集合
     */
    private List<BuildPositionDto> buildList;
}
