package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Year;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 楼座基本信息表
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Build implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 楼座编号
     */
    @TableId(value = "build_id", type = IdType.AUTO)
    @NotNull(message = "楼座编号不能为空")
    private Long buildId;

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 楼座名称
     */
    private String buildName;

    /**
     * 建筑面积
     */
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
     * 产权所属单位id
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
    @TableField(strategy = FieldStrategy.IGNORED)
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
    @TableField(strategy = FieldStrategy.IGNORED)
    private Float assetsValue;

    /**
     * 人防面积
     */
    @TableField(strategy = FieldStrategy.IGNORED)
    private Float civilDefenceArea;

    /**
     * 楼座备注
     */
    private String buildRemark;

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
     * 楼栋单元号
     */
    private Integer unitNo;

    /**
     * 是否关联合同(0不关联  1关联)
     */
    private Integer buildContract;

    /**
     * 房产证号
     */
    private String housePropertyNumber;

}
