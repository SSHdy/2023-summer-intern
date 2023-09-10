package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 人员职称表
 * </p>
 *
 * @author mfl
 * @since 2018-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Professional implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 职称编号
     */
    @TableId(value = "professional_id", type = IdType.AUTO)
    private Integer professionalId;

    /**
     * 职称名字
     */
    private String professionalName;

    /**
     * 职务级别  1中央机关 2省级机关 3市级机关 4县级机关
     */
    private Integer professionalLevel;

    /**
     * 规定面积
     */
    private Float specifiedArea;

    /**
     * 单位级别
     */
    private Integer unitGrade;


}

