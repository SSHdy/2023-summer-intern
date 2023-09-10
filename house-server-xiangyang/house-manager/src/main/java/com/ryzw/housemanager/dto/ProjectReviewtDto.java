package com.ryzw.housemanager.dto;

import lombok.Data;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 查询项目评审信息
 * </p>
 *
 * @author zf
 * @since 2019/4/13
 */
@Data
public class ProjectReviewtDto {

    /**
     * 设计单位
     */
    private String designUnit;

    /**
     * 规划许可证号
     */
    private String planningPermit;

    /**
     * 预算总价
     */
    private BigDecimal totalBudgetPrice;

    /**
     * 预算单位
     */
    private String auditUnit;

    /**
     * 项目评审单位
     */
    private String accreditationUnit;

    /**
     * 决算金额
     */
    private BigDecimal finalSum;

    /**
     * 设计图纸集合
     */
    private List<Document> designFileList;
}
