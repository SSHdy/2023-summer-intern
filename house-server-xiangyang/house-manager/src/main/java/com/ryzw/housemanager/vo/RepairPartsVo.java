package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *维修部位信息管理输入
 * </p>
 *
 * @author mfl
 * @since 2019/5/20
 */
@Data
public class RepairPartsVo {
    /**
     * id
     */
    private Integer repairPartsId;

    /**
     * 维修部位名称
     */
    private String repairPartName;

    /**
     * 质保期
     */
    private Integer repairPartQuality;
}
