package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 院落信息表
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Yard  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 院落编号
     */
    @TableId(value = "yard_id", type = IdType.AUTO)
    private Long yardId;

    /**
     * 院落名称
     */
    @NotNull(message = "院落名称不能为空")
    @ApiModelProperty(value ="院落名称",required = true)
    private String yardName;

    /**
     * 院落位置
     */
    @NotNull(message = "院落位置不能为空")
    @ApiModelProperty(value ="院落位置",required = true)
    private String yardPosition;

    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    @ApiModelProperty(value ="经度",required = true)
    private BigDecimal yardLongitude;

    /**
     * 纬度
     */
    @NotNull(message = "纬度不能为空")
    @ApiModelProperty(value ="纬度",required = true)
    private BigDecimal yardLatitude;

    /**
     * 区域id
     */
    @NotNull(message = "区域id不能为空")
    @ApiModelProperty(value ="区域id",required = true)
    private Integer regionId;

    /**
     * 行政区划名称
     */
    private String yardAdministrativeRegion;

    /**
     * 类型 1院落  2土地
     */
    private Integer type;

}
