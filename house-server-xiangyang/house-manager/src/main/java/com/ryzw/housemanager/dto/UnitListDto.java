package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.entity.UnitProfessional;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2018/12/25
 */
@Data
public class UnitListDto {
    /**
     * 单位序号
     */
    @ApiModelProperty(value = "单位编号")
    private Long unitId;

    /**
     * 单位类型id
     */
    @ApiModelProperty(value = "单位类型id")
    private Integer unitType;

    /**
     * 类型编号
     */
    @ApiModelProperty(value = "类型名称")
    private String unittypeName;


    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String unitName;

    /**
     * 组织机构代码
     */
    @ApiModelProperty(value = "组织机构代码")
    private String unitOrganizationCode;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty(value = "统一社会信用代码")
    private String unitSocialCode;

    /**
     * 单位负责人
     */
    @ApiModelProperty(value = "单位负责人")
    private String unitCharger;

    /**
     * 编制人数
     */
    @ApiModelProperty(value = "编制人数")
    private Integer unitComplement;

    /**
     * 父级单位id
     */
    @ApiModelProperty(value = "父级单位编号")
    private Long unitPid;

    /**
     * 父级单位名称
     */
    @ApiModelProperty(value = "单位名称")
    private String superior;

    /**
     * 单位类别(1机关本级 2垂直管理机构 3派出机构 4参照公务员法管理的事业单位 5其他)
     */
    private String unitCategory;


    /**
     * 单位类型(0非单位  1一级单位  2二级单位)
     */
    @ApiModelProperty(value = "单位类型")
    private Integer unitClassify;

    /**
     * 单位职级关系集合
     */
    private List<ProNumAndLevelDto> unitProfessionalList;

    /**
     * 单位级别 正县、副县、正科、副科
     */
    private String unitGrade;

    /**
     * 机关级别
     */
    private Integer officeLevel;

    /**
     * 工勤编制数
     */
    private Integer unitWork;

    /**
     * 单位副职数
     */
    private Integer unitDeputy;

    /**
     * 是否是临时单位 0否, 1是
     */
    private Integer isTempUnit;

    /**
     * 办公人数
     */
    private Integer workerNumber;

    /**
     * 备注
     */
    private String remark;
}
