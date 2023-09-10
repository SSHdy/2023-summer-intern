package com.ryzw.housemanager.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ryzw.housemanager.config.DoubleSerialize;
import com.ryzw.housemanager.entity.Yard;
import com.ryzw.housemanager.vo.PageVo;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;

/**
 * 输出给前端的楼栋信息
 * </p >
 *
 * @author mfl
 * @since 2018/12/8
 */
@Data
public class BuildDto {

    /**
     * 楼栋id
     */
    private Long buildId;

    /**
     * 院落id
     */
    private Long yardId;
    /**
     * 院落名称
     */
    private String yardName;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 建筑面积
     */
    @JsonSerialize(using = DoubleSerialize.class)
    private Float buildArea;

    /**
     * 建成时间
     */
    private LocalDate buildDate;

    /**
     * 施工单位
     */
    private String builder;

    /**
     * 产权所属单位id
     */
    private String unitName;

    /**
     * 地上楼层
     */
    private Integer overgroundFloor;

    /**
     * 地下楼层
     */
    private Integer undergroundFloor;

    /**
     * 取得方式
     */
    private String obtainWay;

    /**
     * 土地证号
     */
    private String buildCertificate;

    /**
     * 占地面积
     */
    @JsonSerialize(using = DoubleSerialize.class)
    private Float coveredArea;

    /**
     * 完损等级
     */
    private String lossGrade;

    /**
     * 结构类型
     */
    private String structureType;

    /**
     * 权证编号
     */
    private String warrantNum;

    /**
     * 所有权人
     */
    private String warrantPerson;

    /**
     * 权属说明
     */
    private String warrantRemark;

    /**
     * 资产原值(万)
     */
    @JsonSerialize(using = DoubleSerialize.class)
    private Float assetsValue;


    /**
     * 人防面积
     */
    @JsonSerialize(using = DoubleSerialize.class)
    private Float civilDefenceArea;

    /**
     * 楼座备注
     */
    private String buildRemark;

    /**
     * 院落
     */
    private Yard yard;

    /**
     * 楼座经度
     */
    private BigDecimal buildLongitude;

    /**
     * 楼座纬度
     */
    private BigDecimal buildLatitude;

    /**
     * 朝向：1(朝东)，2(朝南)，3(朝西)，4(朝北)，5(南北)
     */
    private String orientation;

    /**
     * 电梯数量
     */
    private Integer elevatorNum;

    /**
     * 房产证号
     */
    private String housePropertyNumber;

}
