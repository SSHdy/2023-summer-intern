package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 处置模块表
 * </p>
 *
 * @author mfl
 * @since 2019-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HandleModule implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 处置模块编号
     */
    @TableId(value = "handle_module_id", type = IdType.AUTO)
    private Long handleModuleId;

    /**
     * 院落编号
     */
    @ApiModelProperty(required = true,value = "院落编号")
    @NotNull(message = "院落编号不能为空")
    private Long yardId;

    /**
     * 楼座编号
     */
    @ApiModelProperty(required = true,value = "楼座编号")
    @NotNull(message = "楼座编号不能为空")
    private Long buildId;

    /**
     * 处置编号
     */
    private Long handleId;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 房间编号
     */
    private String houseId;


}
