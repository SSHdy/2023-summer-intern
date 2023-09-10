package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *维修类型查询报表输出
 * </p>
 *
 * @author mfl
 * @since 2019/4/26
 */
@Data
public class RepairReportVo {
    /**
     * 申请状态（0未审核，1审核通过，2审核未通过）
     */
    private Integer applyStatus;
}
