package com.ryzw.housemanager.mapper;

import com.ryzw.housemanager.entity.Plan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 * 方案表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
public interface PlanMapper extends BaseMapper<Plan> {

    int deleteByPlanId(@Param("planId") Long planId);
}
