package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *查询项目评审信息
 * </p>
 *
 * @author zf
 * @since 2018/4/24
 */
@Data
public class ProjectReviewtVo {

    /**
     * 维修申请Id
     */
    private Long repairApplyId;

    /**
     * 评审类型 1无领导查看  2有领导查看
     */
    private Integer leaderState;
}
