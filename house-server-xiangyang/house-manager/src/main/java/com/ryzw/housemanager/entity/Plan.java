package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 方案表
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Plan implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 方案编号
     */
    @TableId(value = "plan_id", type = IdType.AUTO)
    private Long planId;

    /**
     * 方案名称
     */
    @NotNull(message = "方案名称不能为空")
    private String planName;

    /**
     * 配置申请id
     */
    @NotNull(message = "配置申请Id不能为空")
    private Long configureId;


}
