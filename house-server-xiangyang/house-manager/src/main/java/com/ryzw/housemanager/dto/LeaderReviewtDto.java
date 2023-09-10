package com.ryzw.housemanager.dto;

import lombok.Data;
import org.bson.Document;

import java.util.List;

/**
 * <p>
 * 返回给前端的领导查看信息
 * </p>
 *
 * @author zf
 * @since 2018/4/17
 */
@Data
public class LeaderReviewtDto {

    /**
     * mongo中的字段
     */
    private List<Document> applyExtend;




}
