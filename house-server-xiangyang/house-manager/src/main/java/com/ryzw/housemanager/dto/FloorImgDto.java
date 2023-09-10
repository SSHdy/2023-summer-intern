package com.ryzw.housemanager.dto;

import lombok.Data;
/**
 *
 *传输给前端的楼座照片信息
 * </p >
 *
 * @author mfl
 * @since 2018/12/8
 */
@Data
public class FloorImgDto {

    /**
     * 楼座照片id
     */
    private Long floorimgId;

    /**
     * 楼座id
     */
    private Long floorId;

    /**
     * 照片url
     */
    private String floorimgUrl;

    /**
     * 图片虚拟路径
     */
    private String floorVirtualUrl;
}
