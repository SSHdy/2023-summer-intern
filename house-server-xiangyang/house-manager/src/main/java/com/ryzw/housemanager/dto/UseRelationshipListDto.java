package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2018/12/28
 */
@Data
public class UseRelationshipListDto {
    /**
     * 房间单位职称人员关系id
     */
    private Long jobId;

    /**
     * 职称编号
     */
    private Integer professionalId;

    /**
     * 房间使用人数
     */
    private Integer roomNum;
}
