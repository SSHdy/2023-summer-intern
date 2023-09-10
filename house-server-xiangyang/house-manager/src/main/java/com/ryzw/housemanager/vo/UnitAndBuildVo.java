package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 房屋类型报表查询条件
 * </p>
 *
 * @author zf
 * @since 2019/12/13
 */
@Data
public class UnitAndBuildVo {

    /**
     * 单位Id集合
     */
    private List<Long> unitIdList;

    /**
     * 楼栋Id
     */
    private Long buildId;
}
