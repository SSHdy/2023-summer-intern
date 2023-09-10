package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *需要输出的单位信息
 * </p>
 *
 * @author mfl
 * @since 2019/3/20
 */
@Data
public class AllUnitDto {
    /**
     * 单位序号
     */
    private Long unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 父级单位id
     */
    private Long unitPid;

    /**
     * 单位类型(0非单位  1一级单位  2二级单位)
     */
    private Integer unitClassify;

}
