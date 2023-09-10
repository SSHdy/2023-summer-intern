package com.ryzw.housemanager.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *判断院落是否重复
 * </p>
 *
 * @author mfl
 * @since 2019/8/5
 */
@Data
public class YardRepetitionVo {
    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 院落名称
     */
    @NotNull(message = "院落名称不能为空")
    @ApiModelProperty(value ="院落名称",required = true)
    private String yardName;
}
