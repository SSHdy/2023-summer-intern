package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 *质保期管理信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/4/11
 */
@Data
public class RepairQualityDto {
    /**
     * 维修模块位置
     */
    private String repairModulePosition;

    /**
     * 质保日期
     */
    private Date repairQuality;

    /**
     * 维修面积
     */
    private Double repairModuleArea;

    /**
     * 竣工时间
     */
    private Date completionTime;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 负责人
     */
    private String responsiblePerson;

    /**
     * 维修部位名称
     */
    private String repairPartName;

    /**
     * 质保期
     */
    private Integer repairPartQuality;

    /**
     * 维修地点字符中
     */
    private String positionStr;

}
