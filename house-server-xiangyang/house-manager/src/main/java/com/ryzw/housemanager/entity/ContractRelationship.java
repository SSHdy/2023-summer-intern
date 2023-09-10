package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 合同与院落楼座关系表
 * </p>
 *
 * @author yz
 * @since 2019-08-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ContractRelationship implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同关系id
     */
    @TableId(value = "contract_relationship_id", type = IdType.AUTO)
    private Long contractRelationshipId;

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 合同id
     */
    private Long contractId;

    /**
     * 房间Id
     */
    private String houseId;

    /**
     * 楼层
     */
    private Integer floorName;


}
