package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 租赁关系表
 * </p>
 *
 * @author zf
 * @since 2019-03-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RentRelationship implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 租赁关系编号
     */
    @TableId(value = "rent_relationship_id", type = IdType.AUTO)
    private Long rentRelationshipId;

    /**
     * 租赁房间信息编号
     */
    private Long rentId;

    /**
     * 院落楼座关系编号
     */
    @TableField("housingUse_id")
    private Long housinguseId;


}
