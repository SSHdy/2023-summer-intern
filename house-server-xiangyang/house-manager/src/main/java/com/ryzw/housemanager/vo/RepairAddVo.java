package com.ryzw.housemanager.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 新增维修申请参数
 * </p>
 *
 * @author zf
 * @since 2019/3/29
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepairAddVo {

    /**
     * 维修申请人
     */
    @ApiModelProperty(value = "维修申请人", example = "admin")
    private String repairApplicant;

    /**
     * 维修申请人编号
     */
    @NotNull(message = "用户ID不能为空")
    @ApiModelProperty(value = "用户ID", required = true, example = "13")
    private Long repairApplicantId;


    /**
     * 项目名称
     */
    @ApiModelProperty(value = "项目名称", example = "维修")
    private String repairProject;

    /**
     * 维修模块集合
     */
    private List<RepairModuleVo> repairModuleList;

    /**
     * 使用单位id集合
     */
    private List<Long> unitIdList;


    /**
     * 所有拥有业务科室审批权限的用户ID
     */
    @NotNull(message = "业务科室审批用户组ID不能为空")
    @ApiModelProperty(value = "业务科室审批用户组ID", required = true)
    private List<String> userIds;

    /**
     * 没有审批权限的用户ID(如何没有时，传一个空数组过来,这个ID一定要是之前拥有审批权限的用户的ID)
     */
    @NotNull(message = "没有业务科室审批权限用户组ID不能为空")
    @ApiModelProperty(value = "没有业务科室审批权限用户组ID", required = true)
    private List<String> removeUserIds;

}
