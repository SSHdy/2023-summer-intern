package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *院落下的楼座
 * </p>
 *
 * @author zf
 * @since 2019/3/25
 */
@Data
public class YardRentDto {

    /**
     * 楼座集合
     */
    private List<BuildRentDto> buildList;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 院落编号
     */
    private Long yardId;
}
