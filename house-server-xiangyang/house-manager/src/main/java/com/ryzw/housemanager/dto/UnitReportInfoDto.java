package com.ryzw.housemanager.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>
 *单位报表信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/5/6
 */
@Data
public class UnitReportInfoDto {
    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 组织机构代码
     */
    private String unitOrganizationCode;

    /**
     * 统一社会信用代码
     */
    private String unitSocialCode;

    /**
     * 单位负责人
     */
    private String unitCharger;

    /**
     * 单位类型
     */
    private String unittypeName;

    /**
     * 编制人数
     */
    private Integer unitComplement;

    /**
     * 职称分类名字
     */
    private List<String> professionalName;

    /**
     * 行政区划信息输出
     */
    private List<AdministrativeRegionDto> administrativeRegion;
}
