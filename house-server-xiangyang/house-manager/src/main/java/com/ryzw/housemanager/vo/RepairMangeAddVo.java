package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.RepairItems;
import com.ryzw.housemanager.entity.RepairProject;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 新增维修管理（日志）参数
 * </p>
 *
 * @author zf
 * @since 2019/4/8
 */
@Data
public class RepairMangeAddVo {

    /**
     * 维修申请Id
     */
    private Long repairApplyId;

    /**
     * 施工日志信息对象
     */
    private RepairProject repairProject;

    /**
     * 施工维修部位与照片参数集合
     */
    private List<PartsIdAndImgVo> partsIdAndImgVoList;

    /**
     * 增减项对象
     */
    private List<RepairItems> repairItems;


}
