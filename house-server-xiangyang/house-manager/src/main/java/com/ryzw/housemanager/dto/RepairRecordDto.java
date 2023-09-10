package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 输出给前段的相关维修记录(最近一条)
 * </p>
 *
 * @author zf
 * @since 2019/4/12
 */
@Data
public class RepairRecordDto {

    /**
     * 院落Id
     */
    private Long yardId;

    /**
     * 楼座Id
     */
    private Long buildId;

    /**
     * 房间id
     */
    private String houseId;

    /**
     * 最近时间
     */
    private Date maxTime;

    /**
     * 维修部位Id
     */
    private String repairPartId;

    /**
     * 楼层
     */
    private Long floorName;

    /**
     *联系电话
     */
    private String contactPhone;

    /**
     * 负责人
     */
    private String responsiblePerson;
}
