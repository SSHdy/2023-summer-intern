package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 资产图片表
 * </p>
 *
 * @author wj
 * @since 2019-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("assetsImg")
public class AssetsImg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "assets_img_id", type = IdType.AUTO)
    private Long assetsImgId;

    /**
     * 图片URL地址
     */
    private String imgUrl;

    /**
     * 资产id
     */
    private Long assetsId;


}
