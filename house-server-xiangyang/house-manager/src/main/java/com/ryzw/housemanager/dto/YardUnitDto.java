package com.ryzw.housemanager.dto;

import lombok.Data;

/**
 * <p>
 *院落下的单位信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/9/5
 */
@Data
public class YardUnitDto {

    /**
     * 院落id
     */
    private Long yardId;

    /**
     * 单位序号
     */
    private Long unitId;

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 单位类别(1机关本级 2垂直管理机构 3派出机构 4参照公务员法管理的事业单位 5其他)
     */
    private  String unitCategory;


    /**
     * 职称编号
     */
    private Integer professionalId;

    /**
     * 职称名字
     */
    private String professionalName;

    /**
     * 人员数量
     */
    private Integer personNum;
}
