package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *  查询某一楼层相关数据
 * </p>
 *
 * @author zf
 * @since 2019/08/15
 */
@Data
public class FloorDataDto {

    /**
     * 房间数量
     */
    private Integer houseNum;

    /**
     * 房间面积
     */
    private Float sumHouseArea;

    /**
     * 使用人数
     */
    private Integer useNum;

    /**
     * 办公用房数量
     */
    private Long officeNum;

    /**
     * 办公用房面积
     */
    private Double officeArea;

    /**
     * 领导人数
     */
    private Integer leaderNum;

}
