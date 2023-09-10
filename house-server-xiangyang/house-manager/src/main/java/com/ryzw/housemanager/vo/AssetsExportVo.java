package com.ryzw.housemanager.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author wj
 * @since 2019/2/25 0025
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetsExportVo {

    /**
     * 需要导出的资产id
     */
    @ApiModelProperty(required = true,value = "资产id")
    @NotNull(message = "assetsIds不能为空")
    private List<String> assetsIds;

    /**
     * 资产类别
     */
    @ApiModelProperty(required = true,value = "资产类别")
    @NotNull(message = "assetsType不能为空")
    private Integer assetsType;

}
