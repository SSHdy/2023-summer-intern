package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同附件表
 * </p>
 *
 * @author yz
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ContractEnclosure implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同附件id
     */
    @TableId(value = "contract_enclosure_id", type = IdType.AUTO)
    private Long contractEnclosureId;

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 附件虚拟地址
     */
    private String enclosureVirtualUrl;

    /**
     * 附件物理地址
     */
    private String enclosurePhysicsUrl;

    /**
     * 附件类型
     */
    private Integer enclosureType;

    /**
     * 文件名称
     */
    private String enclosureName;


}
