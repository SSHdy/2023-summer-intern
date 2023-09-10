package com.ryzw.housemanager.dto;

import lombok.Data;
import org.bson.Document;

import java.util.List;

/**
 * <p>
 * 查询施工设计
 * </p>
 *
 * @author zf
 * @since 2019/4/22
 */
@Data
public class DesignReviewDto {

    /**
     * mongo中字段
     */
    private List<Document> documentList;

    /**
     * 设计单位
     */
    private String designUnit;

    /**
     * 规划许可证号
     */
    private String planningPermit;



}
