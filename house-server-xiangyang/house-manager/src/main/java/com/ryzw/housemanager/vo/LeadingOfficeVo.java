package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.LeadingOffice;
import com.ryzw.housemanager.entity.UseRelationship;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  接收领导人员办公信息以及存放领导职称以及接收房屋使用信息
 * </p>
 *
 * @author lyx
 * @since 2018/12/21
 */
@Data
public class LeadingOfficeVo {

    /**
     * 房间编号
     */
    @ApiModelProperty(required = true,value = "房间编号",example = "1")
    private String houseId;

    /**
     * 单位序号
     */
    @ApiModelProperty(required = true,value = "单位序号",example = "1")
    private Long unitId;


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
     * 部门名称
     */
    @ApiModelProperty(value = "部门名称",example = "***")
    private String departmentName;

    /**
     * 编号
     */
    @ApiModelProperty(value = "编号",example = "1")
    private Long housinguseId;


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
