package com.ryzw.housemanager.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *市(州、盟)办公用房信息输出
 * </p>
 *
 * @author mfl
 * @since 2019/9/5
 */
@Data
@Accessors(chain = true)
public class YardUnitProfessionalDto {


    /**
     * 序号
     */
    private Integer id;

    /**
     * 地址坐落
     */
    private String position;

    /**
     *  用地面积
     */
    private Double siteArea;

    /**
     * 总建筑面积
     */
    private Double totalCoveredArea;

    /**
     * 办公用房建筑面积
     */
    private Double officeCoveredArea;

    /**
     * 技术业务用房建筑面积
     */
    private Double technologyCoveredArea;

    /**
     * 使用单位（多个单位填多条）及批复编制情况(人)
     */
    private List<UseUnitAndSituationDto> useUnitAndSituation;


    /**
     * 办公室使用面积
     */
    private Double officeUseArea;

    /**
     * 服务用房使用面积
     */
    private Double serviceUseArea;

    /**
     * 设备用房使用面积
     */
    private Double equipmentUseArea;

    /**
     * 总使用面积(合计)
     */
    private Double totalUseArea;

    /**
     * 附属用房建筑面积
     */
    private Double affiliatedCoveredArea;

    /**
     * 权属登记情况
     */
    private String registration;

    /**
     * 建设年代
     */
    private String buildingAge;

    /**
     * 是否为租（借）用
     */
    private String isRent;

    /**
     * 备注
     */
    private String remark;

}
