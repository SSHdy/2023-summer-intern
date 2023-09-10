package com.ryzw.housemanager.vo;

import lombok.Data;

/**
 * <p>
 *维修部位前端传入的参数
 * </p>
 *
 * @author mfl
 * @since 2019/3/19
 */
@Data
public class RepairPartVo extends PageVo{

    /**
     * 维修部位id
     */
    private Integer repairPartsId;

}
