package com.ryzw.housemanager.dto;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * <p>
 *     使用单位（多个单位填多条）及批复编制情况(人)信息输出
 * </p>
 *
 * @author wj
 * @since 2019-09-06 0006
 */
@Data
@Accessors(chain = true)
public class UseUnitAndSituationDto {

    /**
     * 单位名称
     */
    private String unitName;

    /**
     * 类别
     */
    private String unitType;

    /**
     * 市级正职(副省级)
     */
    private Integer cityOfficialStaffNum;

    /**
     * 市级副职（副市级）
     */
    private Integer cityDeputyStaffNum;

    /**
     * 市直部内正职（正局级）
     */
    private Integer bureauOfficialStaffNum;

    /**
     * 市直部内副职（副局级）
     */
    private Integer bureauDeputyStaffNum;

    /**
     * 正处级
     */
    private Integer directorNum;

    /**
     * 副处级
     */
    private Integer deputyDirectorNum;

    /**
     * 局（处）级以下
     */
    private Integer underDirectorNum;


}
