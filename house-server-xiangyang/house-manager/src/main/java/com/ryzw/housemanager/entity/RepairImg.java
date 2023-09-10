package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 维修照片表
 * </p>
 *
 * @author wj
 * @since 2019-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("repairImg")
public class RepairImg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 维修图片编号
     */
    @TableId(value = "repairImg_id", type = IdType.AUTO)
    private Long repairImgId;

    /**
     * 维修模块编号
     */
    private Long repairModuleId;

    /**
     * 维修照片类型 0维修前 1维修后
     */
    @TableField("repairImg_type")
    private Integer repairImgType;

    /**
     * 维修照片类型 1图片 2文件 3压缩包
     */
    private Integer type;

    /**
     * 图片物理地址
     */
    private String physicalAddress;

    /**
     * 图片虚拟地址
     */
    private String virtualAddress;


}
