package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.ConfigurePlanData;
import com.ryzw.housemanager.entity.ConfigureRoom;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.dozer.Mapping;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     前端传输的配置审批参数
 * </p>
 *
 * @author wj
 * @since 2019-06-04 0004
 */
@Data
public class ConfigureFlowVo {

    /**
     * 配置审批id
     */
    @ApiModelProperty(required = true,value = "配置审批id")
    @NotNull(message = "配置审批id不能为空")
    private Long configureId;

    /**
     * 用户id
     */
    @ApiModelProperty(required = true,value = "用户id")
    @NotNull(message = "用户id不能为空")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty(required = true,value = "用户名")
    @NotNull(message = "用户name不能为空")
    @Mapping(value = "currentUser")
    private String userName;

    /**
     * 流程实例ID
     */
    @ApiModelProperty(required = true,value = "流程实例ID")
    @NotNull(message = "流程实例ID不能为空")
    private String processInstanceId;

    /**
     * 当前执行的任务id
     */
    @ApiModelProperty(required = true,value = "当前执行的任务id")
    @NotNull(message = "当前执行的任务id不能为空")
    private String taskId;

    /**
     * 当前流程步骤编号
     */
    @ApiModelProperty(required = true,value = "当前流程步骤编号")
    @NotNull(message = "当前流程步骤编号不能为空")
    private Integer step;


    /**
     * 连线名称
     */
    @ApiModelProperty(required = true,value = "选择意见不能为空")
    @NotNull(message = "选择意见不能为空")
    private Integer auditState;


    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String comment;

    /**
     * 区域ID集合
     */
    private List<Integer> regionIds;

    /**
     * 电梯台数
     */
    private Integer elevatorsNumber;

    /**
     * 重新分配单位id
     */
    private Long redistributionUnitId;

    /**
     * 配置房间列表
     */
    private List<ConfigureRoom> configureRooms;

    /**
     * 分配方案
     */
    private Integer allocationPlan;

    /**
     * 选择方案
     */
    private Integer selectionPlan;

    /**
     * 方案编号
     */
    private Long planId;

    /**
     *配置方案数据列表
     */
    private List<ConfigurePlanData> configurePlanDataList;

    /**
     * 新增院落与物业信息参数
     */
    private YardAddVo yardAddVo;

    /**
     * 新增楼座
     */
    private List<BuildVo> buildVoList;

    /**
     * 完善信息扩展字段（置换、租用、建设）
     */
    private Map<String,Object> improvingDataRRC;
}
