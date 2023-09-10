package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *分页查询人工分房信息输入
 * </p>
 *
 * @author mfl
 * @since 2019/6/28
 */
@Data
public class ArtificialAllocationVo extends PageVo {
    /**
     * 配置方案数据编号
     */
    private Long configurePlanDataId;

    /**
     * 方案id
     */
    private Long planId;
}
