package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 配置房间表
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ConfigureRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 配置房间编号
     */
    @TableId(value = "configure_room_id", type = IdType.AUTO)
    private Long configureRoomId;

    /**
     * 配置申请编号
     */
    private Long configureId;

    /**
     * 房间数量
     */
    private Integer roomNumber;

    /**
     * 房间面积最小值
     */
    private Float roomAreaMin;

    /**
     * 房间面积最大值
     */
    private Float roomAreaMax;


}
