package com.ryzw.housemanager.dto;

import lombok.Data;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 查询项目预算信息
 * </p>
 *
 * @author zf
 * @since 2019/4/13
 */
@Data
public class ProjectBudgetDto {

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
     * 设计图纸集合
     */
    private List<Document> designFileList;
}
