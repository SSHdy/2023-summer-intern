package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 项目管理施工部位表
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RepairProjectParts implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 项目施工部位编号
     */
    @TableId(value = "repair_project_parts_id", type = IdType.AUTO)
    private Long repairProjectPartsId;

    /**
     * 维修项目管理编号
     */
    private Long repairProjectId;

    /**
     * 维修模块编号
     */
    private Long repairModuleId;

    /**
     * 维修部位
     */
    private Integer repairPart;


}
