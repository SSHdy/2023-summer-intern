package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *专项巡检整改信息
 * </p>
 *
 * @author mfl
 * @since 2019/10/12
 */
@Data
public class ReformVo {
    /**
     * 专项巡检编号
     */
    private Long specialInspectionId;

    /**
     * 整改状态（0未完成 1完成）
     */
    private Integer specialInspectionRectifyState;

    /**
     * 整改结果
     */
    private String specialInspectionRectifyResult;
}
