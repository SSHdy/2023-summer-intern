package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.Date;

/**
 * <p>
 * 业务用房相关面积
 * </p>
 *
 * @author zf
 * @since 2019/9/3
 */
@Data
public class BusinessAreaDto {

    /**
     * 用地面积
     */
    private Float coveredArea;


    /**
     * 总建筑面积
     */
    private Float buildArea;

    /**
     * 建设年代
     */
    private Date buildDate;
}
