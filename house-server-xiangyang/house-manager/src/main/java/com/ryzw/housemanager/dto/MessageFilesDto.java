package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 查询信息档案列表
 * </p>
 *
 * @author zf
 * @since 2019-08-07
 */
@Data
public class MessageFilesDto {

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 院落Id
     */
    private Long yardId;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 楼座Id
     */
    private Long buildId;

    /**
     * 行政区划
     */
    private String yardAdministrativeRegion;

    /**
     * 所属产权
     */
    private String unitName;

    /**
     * 结构类型
     */
    private String structureType;

    /**
     * 建成时间
     */
    private Date buildDate;

    /**
     * 建筑面积
     */
    private Float buildArea;


}
