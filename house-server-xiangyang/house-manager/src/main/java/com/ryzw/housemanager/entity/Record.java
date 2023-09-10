package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 备案表
 * </p>
 *
 * @author wj
 * @since 2019-01-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @TableId(value = "record_id", type = IdType.AUTO)
    private Integer recordId;

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 是否备案(0未备案  1已备案)
     */
    private Integer isRecord;

    /**
     * 备案人
     */
    private String recordHolder;

    /**
     * 备案人id
     */
    private Long recordHolderId;

    /**
     * 备案时间
     */
    private Date recordDate;


}
