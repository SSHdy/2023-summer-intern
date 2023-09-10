package com.ryzw.housemanager.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 物业信息表
 * </p>
 *
 * @author mfl
 * @since 2019-05-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Estate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物业编号
     */
    @TableId(value = "estate_id", type = IdType.AUTO)
    private Long estateId;

    /**
     * 物业名称
     */
    private String estateName;

    /**
     * 物业类型   1：自管；2：第三方物业
     */
    private Long estateType;

    /**
     * 管理人
     */
    @NotNull(message = "管理人不能为空")
    private String manager;

    /**
     * 物业电话
     */
    @NotNull(message = "物业电话不能为空")
    private String estatePhone;

}
