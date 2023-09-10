package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 查询出借用房信息列表参数
 * </p>
 *
 * @author zf
 * @since 2019/5/5
 */
@Data
public class OutRentVo extends PageVo {

    /**
     * 楼座Id
     */
    private Long buildId;

    /**
     * 开始使用日期
     */
    private Date startTime;

    /**
     * 结束使用日期
     */
    private Date endTime;
}
