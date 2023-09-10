package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *资产类别信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/1/9
 */
@Data
public class CategoryDto {
    /**
     * 父级id
     */
    private Integer categoryPid;

    /**
     * 父id为categoryPid的数量
     */
    private Integer childNum;
}
