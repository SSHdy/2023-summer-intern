package com.ryzw.houseauthority.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *  角色单位类
 * </p>
 *
 * @author lyx
 * @since 2018-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
public class RoleUnit implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色单位编号
     */
    @TableId(value = "role_unit_id", type = IdType.AUTO)
    private Long roleUnitId;

    /**
     * 角色编号
     */
    private Integer roleId;

    /**
     * 单位编号
     */
    private Long unitId;


}
