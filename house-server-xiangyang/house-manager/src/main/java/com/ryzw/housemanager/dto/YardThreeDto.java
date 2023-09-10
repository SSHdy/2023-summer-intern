package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *院落权属信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/9/4
 */
@Data
public class YardThreeDto {

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 权属登记情况
     */
    private String registration;

    /**
     * 建设年代
     */
    private String buildingAge;

    /**
     * 是否为租（借）用
     */
    private Integer isRent;

    /**
     * 备注
     */
    private String remark;
}
