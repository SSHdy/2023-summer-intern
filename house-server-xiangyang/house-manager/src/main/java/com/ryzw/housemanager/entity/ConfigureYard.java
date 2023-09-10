package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配置院落中间表
 * </p>
 *
 * @author mfl
 * @since 2019-07-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigureYard implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置院落id
     */
    @TableId(value = "configure_yard_id", type = IdType.AUTO)
    private Long configureYardId;

    /**
     * 配置id
     */
    private Long configureId;

    /**
     * 院落id
     */
    private Long yardId;


}
