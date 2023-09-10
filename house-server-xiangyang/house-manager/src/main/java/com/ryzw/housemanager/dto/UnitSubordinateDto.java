package com.ryzw.housemanager.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *下属单位报表信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/5/9
 */
@Data
public class UnitSubordinateDto {
    /**
     * 组织机构代码
     */
    private String unitOrganizationCode;

    /**
     * 统一社会信用代码
     */
    private String unitSocialCode;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 单位负责人
     */
    private String unitCharger;

    /**
     * 单位类型
     */
    private String unittypeName;

    /**
     * 单位分类（是否参公，公益一类、二类或其他）
     */
    @ApiModelProperty(value = "单位分类（是否参公，公益一类、二类或其他）")
    private String unitGrade;

    /**
     * 单位地址
     */
    private String unitPosition;

    /**
     * 单位级别(0非单位  1一级单位  2二级单位)
     */
    @ApiModelProperty(value = "0")
    private Integer unitClassify;

    /**
     * 编制人数
     */
    @ApiModelProperty(value = "编制人数",required = true)
    private Integer unitComplement;

    /**
     * 单位建筑面积
     */
    private Float buildArea;

    /**
     * 单位占地面积
     */
    private Float useArea;
}
