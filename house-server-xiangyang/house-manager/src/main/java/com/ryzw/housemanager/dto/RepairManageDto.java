package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.entity.RepairItems;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 输出给前端的维修施工管理（日志）
 * </p>
 *
 * @author zf
 * @since 2019/4/4
 */
@Data
public class RepairManageDto {

    /**
     * 维修申请Id
     */
    private Long repairApplyId;

    /**
     * 日期填写状态 1今日已填写
     */
    private Integer dateState;

    /**
     * 施工日志信息
     */
    private List<RepairLogDto> repairLogDtoList;

    /**
     * 增减项文件路径集合
     */
    private List<RepairItems> repairItemsList;
}
