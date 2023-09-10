package com.ryzw.housemanager.vo;


import lombok.Data;

/**
 * <p>
 * 合同提醒参数
 * </p>
 *
 * @author zf
 * @since 2019/8/28
 */
@Data
public class ContractPageVo extends PageVo{

    /**
     * 出租单位
     */
    private String rentalUnit;
}
