package com.ryzw.housemanager.vo;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *合同附件下载数据传入
 * </p>
 *
 * @author mfl
 * @since 2019/8/29
 */
@Data
public class ContractEnclosureVo {
    /**
     * 附件Id集合
     */
    private List<Long> contractEnclosureId;

    /**
     * 合同Id
     */
    private Long contractId;

    /**
     * 下载类型
     */
    private Integer enclosureType;
}
