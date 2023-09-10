package com.ryzw.housemanager.dto;

import lombok.Data;
import sun.util.resources.ga.LocaleNames_ga;

import java.util.List;

/**
 * <p>
 *闲置用房树查询条件
 * </p>
 *
 * @author zf
 * @since 2019/7/8
 */
@Data
public class SpareTreeDto {

    /**
     * 出租编号
     */
    private Long rentId;

    /**
     * 处置方式
     */
    private Integer handleWay;

    /**
     * 租赁关系编号集合
     */
    private List<Long> rentRelationshipIdList;

}
