package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *  领导人员办公信息
 * </p>
 *
 * @author lyx
 * @since 2018-12-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("leadingOffice")
public class LeadingOffice implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "leadingOffice_id", type = IdType.AUTO)
    private Long leadingofficeId;

    /**
     * 房间编号
     */
    private String houseId;

    /**
     * 单位序号
     */
    private Long unitId;

    /**
     * 使用人姓名
     */
    @TableField("leadingOffice_name")
    private String leadingofficeName;

    /**
     * 在职情况名称
     */
    @TableField("leadingOffice_workStatue")
    private String leadingofficeWorkstatue;

    /**
     * 在职情况编号
     */
    @TableField("leadingOffice_workStatue_id")
    private Integer leadingofficeWorkStatueId;

    /**
     * 是否为领导(0领导  1一般人员)
     */
    private Integer isLeading;

    /**
     * 级别
     */
    private Integer professionalId;

    /**
     * 使用单位id
     */
    @TableField("useunit_id")
    private Long useunitId;

    /**
     * 职务
     */
    private String duty;

    /**
     * 是否删除 0未删除  1已删除
     */
    private Integer isDelete;

    /**
     * 职务级别
     */
    private Integer professionalLevel;
}
