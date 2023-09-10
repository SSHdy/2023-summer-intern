package com.ryzw.housemanager.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 *     查询备案详情信息
 * </p>
 *
 * @author wj
 * @since 2019/1/31 0031
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecordVo {

    /**
     * 备案id
     */
    @ApiModelProperty(value = "备案id",example = "5")
    @NotNull(message = "备案id不能为空")
    private Integer recordId;
}
