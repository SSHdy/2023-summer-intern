package com.ryzw.housemanager.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *人工分配房间查询结果输出
 * </p>
 *
 * @author mfl
 * @since 2019/6/18
 */
@Data
public class ArtificialAllocationRoomDto {
    /**
     * 院落id
     */
    private Long yardId;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 楼栋id
     */
    private Long buildId;

    /**
     * 楼栋名称
     */
    private String buildName;

    /**
     * 房间id
     */
    private String houseId;

    /**
     * 楼层名称
     */
    private Integer floorName;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 房间使用面积
     */
    private Float houseUseArea;

    /**
     * 原单位列表
     */
    private List<OldUnitListDto> oldUnitList;

    /**
     * 新单位列表
     */
    private List<NewUnitListDto> newUnitList;

    /**
     * 配置方案数据编号
     */
    private Long configurePlanDataId;

    /**
     * 方案id
     */
    private Long planId;
}
