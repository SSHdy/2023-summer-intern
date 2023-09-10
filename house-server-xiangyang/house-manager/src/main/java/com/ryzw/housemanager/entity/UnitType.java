package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lyx
 * @since 2018-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("unitType")
public class UnitType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单位类型id
     */
    @TableId(value = "unitType_id", type = IdType.AUTO)
    @ApiModelProperty(value = "单位类型id")
    private Integer unittypeId;

    /**
     * 单位类型
     */
    @TableField("unitType_name")
    @ApiModelProperty(value = "单位类型")
    private String unittypeName;

}
