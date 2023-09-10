package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.RepairPmImg;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 新增施工部位与照片参数
 * </p>
 *
 * @author zf
 * @since 2019/4/8
 */
@Data
public class PartsIdAndImgVo {

    /**
     * 施工照片对象集合
     */
    private List<RepairPmImg> repairPmImgList;

    /**
     * 项目施工照片编号
     */
    private Long repairPmImgId;

    /**
     * 项目施工部位编号
     */
    private Long repairProjectPartsId;

    /**
     * 项目管理照片物理地址
     */
    private String repairPmImgPhysicsUrl;

    /**
     * 项目管理照片虚拟地址
     */
    private String repairPmImgVirtualUrl;

    /**
     * 施工维修部位id
     */
    private Long repairPartsId;

    /**
     * 维修模块Id
     */
    private Long repairModuleId;

    /**
     * 维修部位名称
     */
    private String repairPartName;
}
