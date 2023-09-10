package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 资产类别表
 * </p>
 *
 * @author mfl
 * @since 2019-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "category_id", type = IdType.AUTO)
    private Long categoryId;

    /**
     * 设备编码
     */
    private String categoryCode;

    /**
     * 设备名称
     */
    private String categoryName;

    /**
     * 计量单位
     */
    private String categoryUnit;

    /**
     * 父级id
     */
    private Integer categoryPid;

    /**
     * 备注
     */
    private String categoryRemark;


}
