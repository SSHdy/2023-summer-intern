package com.ryzw.housemanager.entity;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 *     流水历史记录
 * </p>
 *
 * @author wj
 * @since 2019/1/16 0016
 */
@Data
public class LsHistory {

    /**
     * 入库状态(0未入库  1已入库)
     */
    private Integer state;

    /**
     * 操作时间
     */
    private Date operationTime;

    /**
     * 操作人
     */
    private String operation;

    /**
     * 流水号
     */
    private String serialNumber;


}
