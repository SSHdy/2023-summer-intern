package com.ryzw.housemanager.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ryzw.housemanager.entity.FloorDrawing;
import com.ryzw.housemanager.entity.OutRent;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 操作楼栋，前端需要传输的参数
 * </p >
 *
 * @author mfl
 * @since 2018/12/8
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildVo {
    /**
     * 楼座编号
     */
    @TableId(value = "build_id", type = IdType.AUTO)
    @ApiModelProperty(value = "楼座编号", example = "1")
    private Long buildId;

    /**
     * 院落编号
     */
    @ApiModelProperty(required = true, value = "院落编号", example = "3")
    @NotNull(message = "院落编号不能为空")
    private Long yardId;

    /**
     * 楼座名称
     */
    @NotNull(message = "楼座名称不能为空")
    @ApiModelProperty(required = true, value = "楼座名称", example = "11号楼")
    private String buildName;

    /**
     * 建筑面积
     */
    @NotNull(message = "建筑面积不能为空")
    @ApiModelProperty(required = true, value = "建筑面积", example = "3214.24")
    private Float buildArea;

    /**
     * 建成时间
     */
    @ApiModelProperty(value = "建成时间", example = "2017-10-02")
    private Date buildDate;

    /**
     * 施工单位
     */
    @ApiModelProperty(value = "施工单位", example = "中建三局")
    private String builder;

    /**
     * 产权所属单位id
     */
    @ApiModelProperty(value = "产权所属单位id", example = "100")
    private Long propertyUnitId;

    /**
     * 地上楼层
     */
    @NotNull(message = "地上楼层不能为空")
    @ApiModelProperty(required = true, value = "地上楼层", example = "20")
    private Integer overgroundFloor;

    /**
     * 地下楼层
     */
    @NotNull(message = "地下楼层不能为空")
    @ApiModelProperty(required = true, value = "地下楼层", example = "-2")
    private Integer undergroundFloor;

    /**
     * 确权年份
     */
    @ApiModelProperty(value = "确权年份", example = "2019")
    private String buildConfirmation;

    /**
     * 取得方式
     */
    @ApiModelProperty(value = "取得方式", example = "土地竞价")
    private String obtainWay;

    /**
     * 取得年份
     */
    @ApiModelProperty(value = "取得年份", example = "2018")
    private String obtainDate;

    /**
     * 土地证号
     */
    @ApiModelProperty(value = "土地证号", example = "CN201211452145")
    private String buildCertificate;

    /**
     * 土地性质
     */
    @ApiModelProperty(value = "土地性质", example = "国有土地")
    private String buildNature;

    /**
     * 占地面积
     */
    @ApiModelProperty(value = "占地面积", example = "504.65", required = true)
    private Float coveredArea;

    /**
     * 完损等级
     */
    @ApiModelProperty(value = "完损等级", example = "一级")
    private String lossGrade;

    /**
     * 登记用途
     */
    @ApiModelProperty(value = "登记用途", example = "办公")
    private String registerUse;

    /**
     * 结构类型
     */
    @ApiModelProperty(value = "结构类型", example = "框架结构")
    private String structureType;

    /**
     * 权证编号
     */
    @ApiModelProperty(value = "权证编号", example = "CN6552145852")
    private String warrantNum;

    /**
     * 所有权人
     */
    @ApiModelProperty(value = "所有权人", example = "国土局")
    private String warrantPerson;

    /**
     * 权属说明
     */
    @ApiModelProperty(value = "权属说明", example = "武汉市政府")
    private String warrantRemark;

    /**
     * 资产原值(万)
     */
    @ApiModelProperty(value = "资产原值", example = "3521.35")
    private Float assetsValue;

    /**
     * 统一权属登记范围
     */
    @ApiModelProperty(value = "统一权属登记范围")
    private String warrantRange;

    /**
     * 人防面积
     */
    @ApiModelProperty(value = "人防面积", example = "1201.35")
    private Float civilDefenceArea;

    /**
     * 楼座备注
     */
    @ApiModelProperty(value = "楼座备注")
    private String buildRemark;

    /**
     * 经度
     */
    @NotNull(message = "经度不能为空")
    @ApiModelProperty(required = true, value = "经度", example = "114.330789")
    private BigDecimal buildLongitude;

    /**
     * 纬度
     */
    @NotNull(message = "纬度不能为空")
    @ApiModelProperty(required = true, value = "纬度", example = "30.579965")
    private BigDecimal buildLatitude;

    /**
     * 楼座新增照片url数组
     */
    @ApiModelProperty(value = "楼座新增照片url数组")
    private List<Map<String, Object>> newFloorImgUrlList;

    /**
     * 楼座删除照片id数组
     */
    @ApiModelProperty(value = "楼座删除照片id数组")
    private List<Long> oldFloorImgIdList;

    /**
     * 朝向：1(朝东)，2(朝南)，3(朝西)，4(朝北)，5(南北)
     */
    @ApiModelProperty(value = "朝向：1(朝东)，2(朝南)，3(朝西)，4(朝北)，5(南北)", example = "1")
    private String orientation;

    /**
     * 电梯数量
     */
    @ApiModelProperty(value = "电梯数量", example = "1")
    private Integer elevatorNum;

    /**
     * 房产证号
     */
    private String housePropertyNumber;

    /**
     * 出租对象
     */
    private OutRent outRent;

    /**
     * 楼座cad集合
     */
    private List<FloorDrawing> floorDrawingList;

    /**
     * 楼座删除cad id数组
     */
    private List<Long> oldCadIdList;

    /**
     * 修改时删除的楼层集合
     */
    private List<Integer> delFloorList;

}
