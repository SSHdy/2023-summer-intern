package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 项目管理照片表
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RepairPmImg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目施工部位编号
     */
    @TableId(value = "repair_pm_img_id", type = IdType.AUTO)
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


}
