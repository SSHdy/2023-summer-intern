package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 租赁附件表
 * </p>
 *
 * @author mfl
 * @since 2019-03-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Enclosure implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 附件编号
     */
    @TableId(value = "enclosure_id", type = IdType.AUTO)
    private Long enclosureId;

    /**
     * 编号
     */
    private Long rentId;

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
