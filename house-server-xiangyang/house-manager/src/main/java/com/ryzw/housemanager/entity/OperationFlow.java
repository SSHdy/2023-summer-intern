package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 操作流水表
 * </p>
 *
 * @author mfl
 * @since 2019-01-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("operationFlow")
public class OperationFlow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "operation_flow_id", type = IdType.AUTO)
    private Long operationFlowId;

    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 操作时间
     */
    private Date operationTime;

    /**
     * 采购项目编号
     */
    private String purchasingId;

    /**
     * 采购项目名称
     */
    private String purchasingPname;

    /**
     * 操作类型(1入库  2转移 3销毁)
     */
    private Integer operationType;

    /**
     * 操作人
     */
    private String operator;


}
