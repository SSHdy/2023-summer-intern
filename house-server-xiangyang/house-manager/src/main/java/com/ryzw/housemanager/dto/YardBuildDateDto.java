package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 院落楼座建成时间
 * </p>
 *
 * @author yz
 * @since 2019/9/11
 */
@Data
public class YardBuildDateDto {
    /**
     * 院落编号
     */
    private Long yardId;
    /**
     * 楼座编号
     */
    private Long buildId;
    /**
     * 楼座建成时间
     */
    private Date buildDate;

    /**
     * 权属登记情况
     */
    private String registration;


    /**
     * 是否为租（借）用
     */
    private Integer isRent;

    /**
     * 备注
     */
    private String remark;
}
