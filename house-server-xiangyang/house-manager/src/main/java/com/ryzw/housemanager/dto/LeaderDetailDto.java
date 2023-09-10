package com.ryzw.housemanager.dto;

import lombok.Data;
import org.bson.Document;

import java.util.List;

/**
 * <p>
 * 领导处置查看
 * </p>
 *
 * @author zf
 * @since 2019/5/28
 */
@Data
public class LeaderDetailDto {

    /**
     * 处置详情
     */
    private HandleDetailDto handleDetailDto;

    /**
     * mongo对象集合
     */
    private List<List<Document>> documents;
}
