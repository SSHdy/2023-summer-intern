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
public class LeaderExcessDto {
    /**
     * 领导办公室总使用面积
     */
    private Float leTotalUseArea;

    /**
     * 领导办公室总的核定面积
     */
    private Float leTotalSpecifiedArea;
}
