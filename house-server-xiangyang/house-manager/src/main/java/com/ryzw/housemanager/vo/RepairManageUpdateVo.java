package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.RepairItems;
import com.ryzw.housemanager.entity.RepairProject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 前端传输的施工日志修改信息
 * </p>
 *
 * @author wj
 * @since 2019/4/4
 */
@Data
public class RepairManageUpdateVo {

    /**
     * 维修申请Id
     */
    private Long repairApplyId;

    /**
     * 项目管理对象
     */
    private RepairProject repairProject;

    /**
     * 施工部位Id与照片集合
     */
    private List<PartsIdAndImgVo> partsIdAndImgVoList;

    /**
     * 增减项对象集合
     */
    private List<RepairItems> repairItemsList;

    /**
     * 删除项目管理照片Id集合
     */
    @ApiModelProperty(value = "删除项目管理照片Id集合")
    private List<Long> deleteRepairPmImgIdList;


}
