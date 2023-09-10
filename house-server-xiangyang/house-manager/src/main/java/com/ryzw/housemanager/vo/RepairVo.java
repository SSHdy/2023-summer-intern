package com.ryzw.housemanager.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 *维修管理前端传输的信息
 * </p >
 *
 * @author mfl
 * @since 2018/12/8
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepairVo{

    /**
     * 维修编号
     */
    @TableId(value = "repair_id", type = IdType.AUTO)
    private Long repairId;

    /**
     * 楼座编号
     */
    @NotNull(message = "楼座编号不能为空")
    @ApiModelProperty(value ="楼座编号",required = true,example = "1")
    private Long buildId;

    /**
     * 所属单位id
     */
    @NotNull(message = "所属单位id不能为空")
    @ApiModelProperty(value ="所属单位id",required = true)
    private Long unitId;

    /**
     * 申请单位
     */
    private Long applicationUnitId;

    /**
     * 院落编号
     */
    @NotNull(message = "院落编号不能为空")
    @ApiModelProperty(value ="院落编号",required = true,example = "3")
    private Long yardId;

    /**
     * 房间编号
     */
    @NotNull(message = "房间编号不能为空")
    @ApiModelProperty(value ="房间编号",required = true,example = "089d4df0-1a30-11e9-ac44-bd404a3496fc")
    private String houseId;

    /**
     * 维修楼层
     */
    private Integer repairLevel;

    /**
     * 项目名称
     */
    private String repairProject;


    /**
     * 申请时间
     */
    @ApiModelProperty(value = "申请时间",example = "2018-01-05T09:12:13.444Z")
    private Date applicationTime;

    /**
     * 申请金额
     */
    private BigDecimal repairAmount;

    /**
     * 维修内容
     */
    private String repairContent;

    /**
     * 施工单位
     */
    private String repairUnit;

    /**
     * 项目金额
     */
    private BigDecimal repairMoney;

    /**
     * 申请状态(0未审核  1审核通过  2审核未通过)
     */
    private Integer applyStatus;

    /**
     * 维修状态(0无效  1挂起  2取消  3完成)
     */
    private Integer repairStatus;

    /**
     * 申请人
     */
    private String application;

    /**
     * 当前问题
     */
    private String currentProblems;

    /**
     * 项目内容
     */
    private String projectContent;

    /**
     * 备注
     */
    private String repairRemark;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value ="用户ID",required = true,example = "13")
    private Long userId;

    /**
     * 所有拥有审批权限的用户ID
     */
    @NotNull(message = "审批用户组ID不能为空")
    @ApiModelProperty(value ="审批用户组ID",required = true)
    private List<String> userIds;

    /**
     * 没有审批权限的用户ID(如何没有时，传一个空数组过来,这个ID一定要是之前拥有审批权限的用户的ID)
     */
    @NotNull(message = "没有审批权限的用户组ID不能为空")
    @ApiModelProperty(value ="没有审批权限用户组ID",required = true)
    private List<String> removeUserIds;

    /**
     * 维修申请新增照片url数组
     */
    @ApiModelProperty(value = "维修申请新增照片url数组")
    private List<Map<String,Object>> newRepairImgUrlList;

    /**
     * 维修申请删除照片id数组
     */
    @ApiModelProperty(value = "维修申请删除照片id数组")
    private List<Long> oldRepairImgIdList;
}
