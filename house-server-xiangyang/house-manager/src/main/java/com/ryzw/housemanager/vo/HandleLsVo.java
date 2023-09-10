package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *查询处置列表（不包含出租出借）
 * </p>
 *
 * @author mfl
 * @since 2019/7/19
 */
@Data
public class HandleLsVo extends PageVo{

    /**
     * 处置楼栋
     */
    private Long buildId;

    /**
     * 处置院落(土地)集合
     */
    private List<Long> yardIdList;

    /**
     * 申请开始时间
     */
    private Date startTime;

    /**
     * 申请结束时间
     */
    private Date endTime;

    /**
     * 申请单位编号
     */
    private Long unitId;

    /**
     * 房间编号集合
     */
    private List<String> houseIdList;

    /**
     * 处置方式(1转换用途 2出租 3拍卖 4 拆除 5不处置 6出借 7征收)
     */
    private Integer handleWay;
}
