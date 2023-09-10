package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 * 物业信息查询条件
 * </p>
 *
 * @author zf
 * @since 2019/5/5
 */
@Data
public class EstateVo extends PageVo {

    /**
     * 物业类型
     */
    private Long estateType;

    /**
     * 管理人
     */
    private String manager;


}
