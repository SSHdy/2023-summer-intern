package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.vo.PageVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 输出给前端的维修申请信息
 * </p>
 *
 * @author wj
 * @since 2019/3/29 0029
 */
@Data
public class RepairApplyDto extends PageVo {

    /**
     * 维修申请id
     */
    private Long repairApplyId;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 申请编号
     */
    private String repairApplyNo;

    /**
     * 维修申请人
     */
    private String repairApplicant;

    /**
     * 维修申请人Id
     */
    private Long repairApplicantId;

    /**
     * 维修申请时间
     */
    private Date repairApplyDate;

    /**
     * 申请状态（0未审核，1审核通过，2审核未通过）
     */
    private Integer applyStatus;

    /**
     * 项目名称
     */
    private String repairProject;

    /**
     * 申请面积
     */
    private Double repairModuleArea;

    /**
     * 维修总体进度
     */
    private Double repairProgress;

    /**
     * 维修单位集合
     */
    private List<String> unitNameList;

    /**
     * 维修位置字符串
     */
    private String positionStr;

    /**
     * 维修类型  0 自费维修  1 零星维修  2机关管理局维修
     */
    private Integer repairType;

    /**
     * 维修内容集合
     */
    private List<String> repairTargetList;

}
