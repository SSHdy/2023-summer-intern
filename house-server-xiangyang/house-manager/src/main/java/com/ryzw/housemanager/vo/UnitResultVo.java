package com.ryzw.housemanager.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *树形结构的院落信息
 * </p>
 *
 * @author mfl
 * @since 2019/1/2
 */
@Data
public class UnitResultVo {
    /**
     * 单位序号
     */
    private Long unitId;
    /**
     * 院落ID
     */
    private Long yardId;

    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 包含的楼座数目
     */
    private Integer buildCount;
}
