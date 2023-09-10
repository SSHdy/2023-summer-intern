package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 房屋信息表
 * </p>
 *
 * @author mfl
 * @since 2018-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@TableName("house")
public class House implements Serializable {
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


    /**
     * 是否闲置(0使用  1闲置)
     */
    private Integer isIdle;


}
