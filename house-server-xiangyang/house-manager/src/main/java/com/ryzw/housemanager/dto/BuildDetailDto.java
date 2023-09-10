package com.ryzw.housemanager.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.ryzw.housemanager.config.DoubleSerialize;
import com.ryzw.housemanager.entity.FloorDrawing;
import com.ryzw.housemanager.entity.Yard;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 输出给前端的楼栋详细信息
 * </p >
 *
 * @author mfl
 * @since 2018/12/8
 */
@Data
public class BuildDetailDto implements Serializable {

    private static final long serialVersionUID=8841433872811285795L;

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
    private Date buildDate;

    /**
     * 施工单位
     */
    private String builder;

    /**
     * 权属单位id
     */
    private Long propertyUnitId;

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
    private Integer obtainWay;

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
     * 电梯数量
     */
    private Integer elevatorNum;

    /**
     * 楼座照片相对路径
     */
    private List<FloorImgDto> floorImg;

    /**
     * 楼座cad集合
     */
    private List<FloorDrawingDto> floorDrawingDtoList;

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
     * 院落地址
     */
    private String yardPosition;

    /**
     * 院落行政区划
     */
    private String yardAdministrativeRegion;

    /**
     * 出租用房信息编号
     */
    private Long outRentId;

    /**
     * 租用单位
     */
    private Long rentUnit;

    /**
     * 租用开始时间
     */
    private Date startTime;

    /**
     * 租用截止时间
     */
    private Date endTime;

    /**
     * 租金标准
     */
    private BigDecimal rentMoney;

    /**
     * 财政备案或批复文号
     */
    private String financeRemark;

    /**
     * 是否租用
     */
    private Integer isRent;


    /**
     * 处置的楼座数量(状态：非审核通过)
     */
    private Integer handleNum;

    /**
     * 房产证号
     */
    private String housePropertyNumber;
}
