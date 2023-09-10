package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *楼栋坐标查询输入
 * </p>
 *
 * @author mfl
 * @since 2019/2/19
 */
@Data
public class BuildPositionVo {
    /**
     * 院落id
     */
    private Long yardId;


    /**
     * 单位id集合
     */
    private List<Long> unitIds;

}
