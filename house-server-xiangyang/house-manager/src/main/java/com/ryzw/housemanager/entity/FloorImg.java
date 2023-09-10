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
 * 楼座照片表
 * </p>
 *
 * @author mfl
 * @since 2018-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("floorImg")
public class FloorImg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 楼座图片编号
     */
    @TableId(value = "floorImg_id", type = IdType.AUTO)
    private Long floorimgId;

    /**
     * 楼座编号
     */
    private Long floorId;

    /**
     * 楼座图片地址
     */

    @TableField("floorImg_url")
    private String floorimgUrl;

    /**
     * 图片上传的虚拟路径
     */
    private String floorVirtualUrl;

}
