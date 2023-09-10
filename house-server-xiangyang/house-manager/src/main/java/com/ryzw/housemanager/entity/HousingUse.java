package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 院落楼座单位关系表
 * </p>
 *
 * @author mfl
 * @since 2018-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("housingUse")
public class HousingUse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "housingUse_id", type = IdType.AUTO)
    private Long housinguseId;

    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 房间编号
     */
    private String houseId;


    /**
     * 楼层
     */
    private Integer floorName;

    /**
     * 是否在使用（1在使用，0未使用）
     */
    private Integer isUse;
}
