package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.RepairModule;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 修改维修模块参数
 * </p>
 *
 * @author zf
 * @since 2019/4/3
 */
@Data
public class RepairApplyUpdateVo {

    /**
     * 维修模块集合
     */
    private List<RepairModuleVo> repairModuleList;

    /**
     * 单位集合
     */
    private List<Long> unitIdList;

    /**
     * 维修申请Id
     */
    private Long repairApplyId;

















}
