package com.ryzw.housemanager.dto;

import com.ryzw.housemanager.entity.RepairImg;
import lombok.Data;
import org.activiti.engine.task.Comment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *     输出给前端的维修信息详情
 * </p>
 *
 * @author
 * @since 2019/3/5
 */
@Data
public class RepairDetailDto {

    /**
     * 项目名称
     */
    private String repairProject;

    /**
     * 维修编号
     */
    private Long repairId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 任务id
     */
    private String taskId;

    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 所属单位名称
     */
    private String unitName;

    /**
     * 维修内容
     */
    private String repairContent;

    /**
     * 申请时间
     */
    private Date applicationTime;

    /**
     * 申请人
     */
    private String application;

    /**
     *申请金额
     */
    private BigDecimal repairMoney;

    /**
     * 申请单位名称
     */
    private String applicationUnitName;

    /**
     * 维修楼层
     */
    private Integer repairLevel;

    /**
     * 维修部位
     */
    private Integer repairPart;

    /**
     * 维修图片数组
     */
    private List<RepairImg> repairImgUrlList;

    /**
     * 历史批注信息
     */
    private List<Comment> commentList;

}
