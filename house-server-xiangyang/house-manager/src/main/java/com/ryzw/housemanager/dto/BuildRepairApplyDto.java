package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 查询某栋楼关联的维修申请
 * </p>
 *
 * @author zf
 * @since 2019/8/2
 */
@Data
public class BuildRepairApplyDto {

    /**
     * 维修申请id
     */
    private Long repairApplyId;

    /**
     * 维修院落
     */
    private String yardName;

    /**
     * 院落Id
     */
    private Long yardId;

    /**
     * 维修楼座
     */
    private String buildName;

    /**
     * 楼座Id
     */
    private Long buildId;

    /**
     * 维修申请人
     */
    private String repairApplicant;

    /**
     * 维修申请时间
     */
    private Date repairApplyDate;


}
