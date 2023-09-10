package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 处置方式表
 * </p>
 *
 * @author mfl
 * @since 2019-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HandleWay implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 处置方式编号
     */
    @TableId(value = "handle_way_id", type = IdType.AUTO)
    private Long handleWayId;

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 处置方式
     */
    private Integer handleWay;

    /**
     * 房间编号
     */
    private String houseId;


}
