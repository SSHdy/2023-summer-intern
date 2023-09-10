package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.ContractEnclosure;
import com.ryzw.housemanager.entity.ContractRelationship;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *     前端传输的租赁合同相关信息
 * </p>
 *
 * @author wj
 * @since 2019-08-28 0028
 */
@Data
public class ContractVo {

    /**
     * 合同id
     */
    @ApiModelProperty(value = "合同id", example = "1")
    private Long contractId;

    /**
     * 租赁开始时间
     */
    @NotNull(message = "租赁开始时间不能为空")
    @ApiModelProperty(value = "租赁开始时间", example = "2017-10-02")
    private Date leaseBeginTime;

    /**
     * 租赁结束时间
     */
    @NotNull(message = "租赁结束时间不能为空")
    @ApiModelProperty(value = "租赁结束时间", example = "2018-10-02")
    private Date leaseEndTime;

    /**
     * 合同年限
     */
    @NotNull(message = "合同年限不能为空")
    @ApiModelProperty(value = "合同年限", example = "3")
    private Integer contractYear;


    /**
     * 付款时间
     */
    @NotNull(message = "付款时间不能为空")
    @ApiModelProperty(value = "付款时间", example = "2018-10-02")
    private Date paymentTime;

    /**
     * 首次付款时间
     */
    @ApiModelProperty(value = "首次付款时间", example = "2019-8-28")
    private Date firstPaymentTime;

    /**
     * 备注信息
     */
    @ApiModelProperty(value = "备注信息", example = "备注信息")
    private String remark;

    /**
     * 出租单位
     */
    @NotNull(message = "出租单位不能为空")
    @ApiModelProperty(value = "出租单位", example = "出租单位")
    private String rentalUnit;

    /**
     * 付款周期(月)
     */
    @NotNull(message = "付款周期(月)不能为空")
    @ApiModelProperty(value = "付款周期(月)", example = "1")
    private Integer paymentCycle;

    /**
     * 租金
     */
    @NotNull(message = "租金不能为空")
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
    @NotNull(message = "押金不能为空")
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
     * 合同附件
     */
    @NotNull(message = "合同附件不能为空")
    @ApiModelProperty(value = "合同附件")
    private List<ContractEnclosure> contractEnclosures;

    /**
     * 删除的租赁合同附件id集合
     */
    @ApiModelProperty(value = "租赁合同附件id集合")
    private List<Long> deleteContractEnclosureIds;

    /**
     * 院落楼座关系集合
     */
    @NotNull(message = "院落楼座信息不能为空")
    @ApiModelProperty(value = "院落楼座信息")
    private List<ContractRelationship> contractRelationships;




}
