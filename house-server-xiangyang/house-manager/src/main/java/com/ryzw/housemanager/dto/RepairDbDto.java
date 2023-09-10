package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *     内控返回成功时，在data里面的字段
 * </p>
 *
 * @author wj
 * @since 2019-09-23 0023
 */
@Data
public class RepairDbDto {

    /**
     * 内控审批单号
     */
    private String sano;
}
