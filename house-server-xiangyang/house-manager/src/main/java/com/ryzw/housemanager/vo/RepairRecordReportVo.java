package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 维修报表信息查询条件
 * </p>
 *
 * @author zf
 * @since 2019/5/9
 */
@Data
public class RepairRecordReportVo {

    /**
     * 开工时间
     */
    private Date startTime;

    /**
     * 竣工时间
     */
    private Date endTime;

    /**
     * 维修部位名称
     */
    private String repairPartName;
}
