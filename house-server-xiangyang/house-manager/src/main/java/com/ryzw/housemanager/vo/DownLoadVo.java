package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 下载多个CAD，用于存放选中的多个楼座
 * </p>
 *
 * @author lyx
 * @since 2018/12/13
 */
@Data
public class DownLoadVo {
    /**
     * 楼座名称
     */
    @ApiModelProperty(value = "楼座名称", example = "1号大楼")
    @NotNull(message = "楼座名称不能为空")
    private String buildName;

    /**
     * 楼座Id
     */
    @ApiModelProperty(value = "楼座编号", example = "1")
    @NotNull(message = "楼座Id不能为空")
    private Long buildId;

    /**
     * 楼层
     */
    @ApiModelProperty(value = "楼层", example = "1")
    //@NotNull(message = "楼层不能为空")
    private Integer floorName;

    /**
     * 存放floorDrawingIdList
     */
    @ApiModelProperty(value = "存放floorDrawingIdList", example = "[]")
    private List<Integer> floorDrawingIdList;
}
