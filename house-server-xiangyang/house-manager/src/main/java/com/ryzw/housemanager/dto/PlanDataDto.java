package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 查询配置方案数据列表
 * </p>
 *
 * @author zf
 * @since 2019/6/11
 */
@Data
public class PlanDataDto {

    /**
     * 配置方案数据id
     */
    private Long configurePlandataid;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 房间面积
     */
    private Float houseUseArea;

    /**
     * 原使用单位名称集合
     */
    private List<String> oldUnitNameList;


}
