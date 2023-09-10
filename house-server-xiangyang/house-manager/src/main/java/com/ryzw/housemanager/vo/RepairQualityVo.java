package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *质保期管理前端信息传入
 * </p>
 *
 * @author mfl
 * @since 2019/4/11
 */
@Data
public class RepairQualityVo extends PageVo {

    /**
     * 维修模块编号
     */
    private Long repairModuleId;

    /**
     * 维修部位id
     */
    private Integer repairPartsId;

}
