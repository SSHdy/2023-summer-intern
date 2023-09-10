package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 房屋信息历史表
 * </p>
 *
 * @author yz
 * @since 2019-08-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HouseHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 房间编号
     */
    @TableId(type = IdType.INPUT,value = "house_id")
    private String houseId;

    /**
     * 房屋类型编号
     */
    @TableField("houseType_id")
    private Integer housetypeId;

    /**
     * 楼栋id
     */
    private Long buildId;

    /**
     * 房间号
     */
    private String houseNumber;

    /**
     * 楼层
     */
    private Integer floorName;

    /**
     * 使用面积
     */
    private Float houseUseArea;

    /**
     * 建筑面积
     */
    private Float houseBuildArea;

    /**
     * 是否超标(0未超标,1超标)
     */
    private Integer isExcess;

    /**
     * 备案id
     */
    private Integer recordId;

    /**
     * 使用类型
     */
    private String useType;


}
