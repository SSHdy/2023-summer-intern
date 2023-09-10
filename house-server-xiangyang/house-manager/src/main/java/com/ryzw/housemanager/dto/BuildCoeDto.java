package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 * 各楼栋系数
 * </p>
 *
 * @author zf
 * @since 2019/12/10
 */
@Data
public class BuildCoeDto {

    /**
     * 楼栋Id
     */
    private Long buildId;

    /**
     * 楼栋系数
     */
    private Double buildCoe;

}
