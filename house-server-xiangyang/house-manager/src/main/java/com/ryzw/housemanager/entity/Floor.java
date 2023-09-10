package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 院落楼座单位关系表
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Floor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "floor_id", type = IdType.AUTO)
    private Long floorId;

    /**
     * 楼座编号
     */
    private Long buildId;

    /**
     * 院落编号
     */
    private Long yardId;

    /**
     * 单位序号
     */
    private Long unitId;

    /**
     * 房间编号
     */
    private Long houseId;

    /**
     * 楼层
     */
    private String floorName;

    /**
     * 勤杂人员数量
     */
    private Integer staffNumber;

    /**
     * 外聘人员数量
     */
    private Integer externalNumber;


}
