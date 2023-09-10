package com.ryzw.houseauthority.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户配置权限表
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserConfigure implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户配置权限编号
     */
    @TableId(value = "user_configure_id", type = IdType.AUTO)
    private Long userConfigureId;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 配置权限编号
     */
    private Long configureStep;


}
