package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 单位信息表
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Unit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 单位序号
     */
    @TableId(value = "unit_id", type = IdType.AUTO)
    private Long unitId;

    /**
     * 类型编号
     */
    @TableField(value = "unit_Type", strategy = FieldStrategy.IGNORED)
    @ApiModelProperty(value = "类型编号")
    private Integer unitType;


    /**
     * 单位名称
     */
    @NotNull(message = "单位名称不能为空")
    @ApiModelProperty(value = "单位名称", required = true)
    private String unitName;

    /**
     * 组织机构代码
     */
//    @NotNull(message = "组织机构代码不能为空")
    @ApiModelProperty(value = "组织机构代码", required = true)
    @TableField("unit_organization_code")
    private String unitOrganizationCode;

    /**
     * 统一社会信用代码
     */
    @NotNull(message = "统一社会信用代码不能为空")
    @ApiModelProperty(value = "统一社会信用代码", required = true)
    private String unitSocialCode;

    /**
     * 单位负责人
     */
    @ApiModelProperty(value = "单位负责人", required = true)
    private String unitCharger;

    /**
     * 编制人数
     */
    @NotNull(message = "编制人数不能为空")
    @ApiModelProperty(value = "编制人数", required = true)
    private Integer unitComplement;

    /**
     * 父级单位id
     */
    @NotNull(message = "父级单位编号不能为空")
    @ApiModelProperty(value = "父级单位编号不能为空", required = true)
    private Long unitPid;

    /**
     * 是否为单位（0代表分组，1代表单位）
     */
    @ApiModelProperty(value = "1")
    private Integer unitMark;


    /**
     * 单位类型(0非单位  1一级单位  2二级单位)
     */
    @ApiModelProperty(value = "0")
    private Integer unitClassify;

    /**
     * 单位树id
     */
    private String unitTreeId;

    /**
     * 单位级别 正县、副县、正科、副科
     */
    private String unitGrade;

    /**
     * 单位类别(1机关本级 2垂直管理机构 3派出机构 4参照公务员法管理的事业单位 5其他)
     */
    private String unitCategory;

    /**
     * 机关级别(1市级机关 2县级机关 3乡级机关)
     */
    @TableField(strategy = FieldStrategy.IGNORED)
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
