package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *房屋类型报表信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/5/8
 */
@Data
public class TypeReportDto {

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 使用类型
     */
    private String useType;

    /**
     * 房间名称
     */
    private String houseNumber;

    /**
     * 使用面积
     */
    private Float houseUseArea;
}
