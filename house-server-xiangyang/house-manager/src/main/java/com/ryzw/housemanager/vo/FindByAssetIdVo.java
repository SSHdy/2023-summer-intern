package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *资产信息分页查询前端输入
 * </p>
 *
 * @author mfl
 * @since 2019/2/28
 */
@Data
public class FindByAssetIdVo extends PageVo{
    /**
     * 资产id
     */
    private String assetsIdComn;
}
