package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *资产信息修改需要传入的参数
 * </p>
 *
 * @author mfl
 * @since 2019/2/22
 */
@Data
public class AssetsUpdateVo {
    /**
     * id
     */
    private String id;

    /**
     * 会计凭证号
     */
    private String accountingVoucherNumber;

    /**
     * 卡片编号
     */
    private String cardId;
}
