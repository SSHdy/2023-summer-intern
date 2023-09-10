package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *领导人员办公司报表查询输出
 * </p>
 *
 * @author mfl
 * @since 2019/5/8
 */
@Data
public class LeadingOfficeReportDto {
    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 使用人姓名
     */
    private String leadingofficeName;

    /**
     * 在职情况名称
     */
    private String leadingofficeWorkstatue;

    /**
     * 职称名字
     */
    private String professionalName;

    /**
     * 职务级别
     */
    private String professionalLevel;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 使用面积
     */
    private Float houseUseArea;

}
