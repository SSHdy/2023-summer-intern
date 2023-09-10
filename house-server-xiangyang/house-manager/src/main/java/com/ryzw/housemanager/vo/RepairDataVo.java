package com.ryzw.housemanager.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 *前端传输的完善维修资料参数
 * </p >
 *
 * @author
 * @since 2019/3/7
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RepairDataVo {

    /**
     * 维修记录编号
     */
    @TableId(value = "repairRecord_id", type = IdType.AUTO)
    private Long repairRecordId;

    /**
     * 维修编号
     */
    private Long repairId;

    /**
     * 工程名单
     */
    private String projectName;

    /**
     * 工程级别
     */
    private String projectLevel;

    /**
     * 工程类型
     */
    private String projectType;

    /**
     * 维修面积
     */
    private Float repairArea;

    /**
     * 决算金额
     */
    private BigDecimal repairAccount;

    /**
     * 开工时间
     */
    private Date repairBegin;

    /**
     * 骏工时间
     */
    private Date repairEnd;

    /**
     * 施工许可证号
     */
    private String constructionNum;

    /**
     * 规划许可证号
     */
    private String planNum;

    /**
     * 设计单位
     */
    private String designUnit;

    /**
     * 监理单位
     */
    private String controlUnit;

    /**
     * 工程质量监督单位
     */
    private String supervisoryUnit;

    /**
     * 维修资料编号
     */
    private Long repairDataId;

    /**
     * 资料附件物理地址
     */
    private String physicalAddress;

    /**
     * 资料附件虚拟地址
     */
    private String virtualAddress;

}
