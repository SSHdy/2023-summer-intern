package com.ryzw.housemanager.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author wj
 * @since 2019-02-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UseUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "useunit_id", type = IdType.AUTO)
    private Long useunitId;

    private Long unitId;

    private String departmentName;

    @TableField(value = "distribution_time",strategy = FieldStrategy.IGNORED)
    private Date distributionTime;

    private String useForm;

    @TableField("housingUse_id")
    private Long housinguseId;


}
