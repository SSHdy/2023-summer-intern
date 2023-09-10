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
 * 楼座图纸
 * </p>
 *
 * @author mfl
 * @since 2018-11-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("floorDrawing")
public class FloorDrawing implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 楼层图纸编号
     */
    @TableId(value = "floorDrawing_id", type = IdType.AUTO)
    private Long floordrawingId;

    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 楼层图纸附件地址
     */
    @TableField("floorDrawing_url")
    private String floordrawingUrl;

    /**
     * 楼层图纸附件虚拟地址
     */
    @TableField("floorDrawing_virtual_url")
    private String floordrawingVirtualUrl;

    /**
     *  上传原文件名
     */
    @TableField("floorDrawing_file_name")
    private String floordrawingFileName;
}
