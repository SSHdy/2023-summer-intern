package com.ryzw.housemanager.dto;

import lombok.Data;
import org.bson.Document;

import java.util.List;

/**
 * <p>
 * 查询竣工验收信息
 * </p>
 *
 * @author zf
 * @since 2019/4/18
 */
@Data
public class CompletedProjectDto {

    /**
     * mongodb中的字段
     */
    private List<Document> documentList;

    /**
     *竣工验收信息
     */
    private List<RepairProjectDto> repairProjectDtoList;

}
