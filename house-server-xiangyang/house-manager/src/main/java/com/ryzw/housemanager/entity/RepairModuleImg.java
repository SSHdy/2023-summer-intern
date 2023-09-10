package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 维修模块照片表
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RepairModuleImg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维修模块照片编号
     */
    @TableId(value = "repair_module_img", type = IdType.AUTO)
    private Long repairModuleImg;

    /**
     * 维修模块编号
     */
    private Long repairModuleId;

    /**
     * 照片物理地址
     */
    private String repairModulePhysicsUrl;

    /**
     * 照片虚拟地址
     */
    private String repairModuleVirtualUrl;


}
