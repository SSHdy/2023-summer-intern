package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 * 查询内控采购流程步骤详情条件
 * </p >
 *
 * @author zf
 * @since 2019/9/24
 */
@Data
public class DbStepDetailVo {

    /**
     * 维修申请id
     */
    private Long repairApplyId;

    /**
     * 内控审批单号
     */
    private String sano;

    /**
     * 采购步骤标识
     */
    private String stepTags;
}
