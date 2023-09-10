package com.ryzw.housemanager.dto;

import lombok.Data;
import org.bson.Document;

import java.util.List;

/**
 * <p>
 *  楼栋处置信息
 * </p>
 *
 * @author zf
 * @since 2019/7/16
 */
@Data
public class BuildHandleDto {

    /**
     * 处置编号
     */
    private Long handleId;

    /**
     *处置方式(1转换用途 2出租 3拍卖 4 拆除 5不处置 6出借 7征收)
     */
    private  Integer handleWay;


    /**
     * mongo中数据
     */
    private List<Document> documentList;
}
