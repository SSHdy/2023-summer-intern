package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *位置信息输入
 * </p>
 *
 * @author mfl
 * @since 2019/2/18
 */
@Data
public class PositionVo{
    /**
     * 单位id集合
     */
    private List<Long> unitIds;
}
