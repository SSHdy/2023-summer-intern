package com.ryzw.housemanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>
 *在职情况管理
 * </p>
 *
 * @author mfl
 * @since 2018/12/18
 */
@Data
@AllArgsConstructor
public class StatueDto {
    /**
     * 在职状态的value，例如：value is "在职" or "离职" and so on
     */
    private String value;
    /**
     *在职状态的key，例如：key is "0" or "1" and so on
     */
    private int key;
}
