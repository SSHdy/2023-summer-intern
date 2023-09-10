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
 * 房屋类型表
 * </p>
 *
 * @author lyx
 * @since 2018-12-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("houseType")
public class HouseType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 房屋类型编号
     */
    @TableId(value = "houseType_id", type = IdType.AUTO)
    @ApiModelProperty(value = "房屋类型编号")
    private Integer housetypeId;

    /**
     * 房屋类型名称
     */
    @TableField("houseType_name")
    @ApiModelProperty(value = "房屋类型名称")
    private String housetypeName;

    @TableField("houseType_remark")
    @ApiModelProperty(value = "房屋类型备注")
    private String housetypeRemark;

    /**
     * 房屋类型标识
     */
    private Integer houseTypeFlag;

    /**
     * 颜色类型
     */
    private String colorType;

    /**
     * 是否用建筑面积  0否  1是
     */
    private Integer useBuildArea;


}
