package com.ryzw.housemanager.vo;

import com.ryzw.housemanager.entity.ContractEnclosure;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *     前端传输的延期合同信息
 * </p>
 *
 * @author wj
 * @since 2019-08-28 0028
 */
@Data
public class ContractDelayVo {

    /**
     * 合同id
     */
    @NotNull(message = "合同id不能为空")
    @ApiModelProperty(value = "合同id", example = "1")
    private Long contractId;

    /**
     * 租赁结束时间
     */
    @NotNull(message = "租赁结束时间不能为空")
    @ApiModelProperty(value = "租赁结束时间", example = "2018-10-02")
    private Date leaseEndTime;

    /**
     * 合同附件
     */
    @NotNull(message = "合同附件不能为空")
    @ApiModelProperty(value = "合同附件")
    private List<ContractEnclosure> contractEnclosures;

    /**
     * 删除的租赁合同附件id集合
     */
    @ApiModelProperty(value = "删除的租赁合同附件id集合")
    private List<Long> deleteContractEnclosureIds;

}
