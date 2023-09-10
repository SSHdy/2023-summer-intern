package com.ryzw.housemanager.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2019/7/9
 */
@Data
public class AllocationConfDto {
    /**
     * 配置方案数据编号
     */
    private Long configurePlanDataId;

    /**
     * 配置申请编号
     */
    private Long configureId;

    /**
     * 方案编号
     */
    private Long planId;

    /**
     * 方案类型
     */
    private Integer planType;

    /**
     * 是否闲置(0闲置 1非闲置)
     */
    private Integer isIdle;

}
