package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 * 每种房间类型对应的使用面积
 * </p >
 *
 * @author zf
 * @since 2020.4.10
 */
@Data
public class AreaDetailByHouseDto {

    /**
     * 房间类型Id
     */
    private Integer houseTypeId;

    /**
     * 房间类型名称
     */
    private String houseTypeName;

    /**
     * 该类型的房间总使用面积
     */
    private Float houseUseAreaSum;




}
