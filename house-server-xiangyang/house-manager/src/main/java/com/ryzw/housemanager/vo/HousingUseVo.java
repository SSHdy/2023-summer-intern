package com.ryzw.housemanager.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ryzw.housemanager.entity.LeadingOffice;
import com.ryzw.housemanager.entity.UseRelationship;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 院落，楼栋，房屋，单位关系参数
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HousingUseVo {

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号",example = "1")
    private Long useunitId;

    /**
     * 院落楼座关系id
     */
    @ApiModelProperty(value = "编号",example = "1")
    private Long housinguseId;

    /**
     * 楼座编号
     */
    @ApiModelProperty(value = "楼座编号",example = "1")
    private Long buildId;

    /**
     * 院落编号
     */
    @ApiModelProperty(value = "院落编号",example = "1")
    private Long yardId;

    /**
     * 单位序号
     */
    @ApiModelProperty(value = "单位序号",example = "1")
    private Long unitId;

    /**
     * 房间编号
     */
    @ApiModelProperty(value = "房间编号",example = "UUID")
    private String houseId;

    /**
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称",example = "XXX")
    private String departmentName;

    /**
     * 分配时间
     */
    @ApiModelProperty(value = "分配时间",example = "2018-12-12")
    private Date distributionTime;

    /**
     * 使用形式
     */
    @ApiModelProperty(value = "使用形式",example = "***")
    private String useForm;

    /**
     * 人员集合
     */
    @ApiModelProperty(value = "人员集合list",example = "[]")
    private List<LeadingOffice> leadingOfficeList;


    /**
     * 职称list
     */
    @ApiModelProperty(value = "职称list",example = "[]")
    private List<UseRelationship> useRelationshipList;

    /**
     * 删除人员ids
     */
    @ApiModelProperty(value = "职称list",example = "[]")
    private List<Long> deleteLeadingOfficeIds;
}
