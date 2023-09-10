package com.ryzw.houseauthority.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户处置权限表
 * </p>
 *
 * @author mfl
 * @since 2019-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserHandle implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户处置权限编号
     */
    @TableId(value = "user_handle", type = IdType.AUTO)
    private Long userHandle;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 处置权限编号
     */
    private Long handleStep;


}
