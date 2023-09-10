package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2019/7/23
 */
@Data
public class CommonExcessDto {
    /**
     * 普通人员办公室总使用面积
     */
    private Float coTotalUseArea;

    /**
     * 普通人员办公室总的核定面积
     */
    private Float coTotalSpecifiedArea;
}
