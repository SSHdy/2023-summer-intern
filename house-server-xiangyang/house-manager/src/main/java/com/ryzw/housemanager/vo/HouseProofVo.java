package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 房屋报表信息输入
 * </p>
 *
 * @author mfl
 * @since 2019/1/18
 */
@Data
public class HouseProofVo extends PageVo {
    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id")
    private List<Long> unitIdList;

    /**
     * 院落位置
     */
    @ApiModelProperty(value = "院落位置")
    private String yardPosition;

    /**
     * 楼栋Id
     */
    @ApiModelProperty(value = "楼栋Id")
    private Long buildId;
}
