package com.ryzw.housemanager.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ryzw.housemanager.entity.Enclosure;
import com.ryzw.housemanager.entity.HousingUse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端传输的新增租赁信息与租赁附件信息
 * </p>
 *
 * @author zf
 * @since 2019/3/13
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RentEnclosureVo {

    /**
     * 房屋租赁编号
     */
    @ApiModelProperty(value = "房屋租赁编号", example = "1")
    private Long rentId;

    /**
     * 租赁方式
     */
    @ApiModelProperty(value = "租赁方式", example = "3")
    private Integer rentMode;

    /**
     * 承租用途
     */
    @ApiModelProperty(value = "承租用途", example = "用途")
    private String lesseeUse;

    /**
     * 承租年限
     */
    @ApiModelProperty(value = "承租年限", example = "10")
    private Double lesseeYears;

    /**
     * 承租方
     */
    @NotNull(message = "承租方不能为空")
    @ApiModelProperty(value = "承租方", example = "张三")
    private String renter;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", example = "2017-10-02")
    @NotNull(message = "租(借)用起始时间不能为空")
    private Date rentBegin;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", example = "2018-10-02")
    @NotNull(message = "租(借)用截止时间不能为空")
    private Date rentEnd;

    /**
     * 租金
     */
    @ApiModelProperty(value = "租金", example = "10")
    private BigDecimal rentMoney;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人", example = "李四")
    private String contacts;

    /**
     * 押金
     */
    @ApiModelProperty(value = "押金", example = "500")
    private BigDecimal deposit;

    /**
     * 租赁类型
     */
    @ApiModelProperty(value = "租赁类型", example = "1")
    private Integer rentType;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式", example = "12345678912")
    private String contactPhone;

    /**
     * 建筑面积
     */
    @ApiModelProperty(value = "建筑面积", example = "120")
    private Double coveredArea;

    /**
     * 所属办公楼
     */
    @ApiModelProperty(value = "所属办公楼", example = "财政楼")
    private String officeBuilding;

    /**
     * 所属单位
     */
    @ApiModelProperty(value = "所属单位", example = "1")
    private Long affiliatedUnit;

    /**
     * 财政备案
     */
    @ApiModelProperty(value = "财政备案", example = "备案号")
    private String financialCode;

    /**
     * 付款时间
     */
    @ApiModelProperty(value = "付款时间", example = "2018-10-02")
    private Date firstPaymentTime;

    /**
     * 付款周期(月)
     */
    private Integer paymentCycle;

    /**
     * 表类型 1出租  2出借
     */
    @ApiModelProperty(value = "表类型", example = "1")
    private Integer type;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", example = "备注信息")
    private String remark;

    /**
     * 状态 1院落 2土地
     */
    private Integer state;

    /**
     * 土地id集合
     */
    private List<Long> soilList;

    /**
     * 租赁附件路径集合
     */
    private List<Enclosure> addressList;

    /**
     * 租赁删除照片id数组
     */
    @ApiModelProperty(value = "租赁删除照片id数组")
    private List<Long> deleteEnclosureIdList;

    /**
     * 院落楼座关系集合
     */
    private List<HousingUse> housingUseList;

    /**
     * 修改时减少的房间号
     */
    private List<String> delHouseIdList;

}
