package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配置附件表
 * </p>
 *
 * @author zf
 * @since 2019-07-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigureEnclosure implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置附件编号
     */
    @TableId(value = "configure_enclosure_id", type = IdType.AUTO)
    private Long configureEnclosureId;

    /**
     * 配置编号
     */
    private Long configureId;

    /**
     * 附件虚拟地址
     */
    private String enclosureVirtualUrl;

    /**
     * 附件物理地址
     */
    private String enclosurePhysicsUrl;

    /**
     * 附件类型：1(照片)，2(文件)
     */
    private Integer enclosureType;

    /**
     * 文件名称
     */
    private String enclosureName;


}
