package com.ryzw.housemanager.service;

import com.ryzw.housemanager.entity.Plan;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.ConfigurePlanDataVo;
import com.ryzw.housemanager.vo.ConfigurePlanVo;
import com.ryzw.housemanager.vo.JsonPlanVo;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 方案表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
public interface IPlanService extends IService<Plan> {

    /**
     * 配置方案列表
     * @param configurePlanVo
     * @return
     */
    List<Plan> configurePlanList(ConfigurePlanVo configurePlanVo);

    /**
     *新增配置方案
     * @param plan
     * @return
     */
    boolean addConfigurePlan(Plan plan);

    /**
     * 修改配置方案
     * @param plan
     * @return
     */
    boolean updateConfigurePlan(Plan plan);

    /**
     * 删除配置方案
     * @param basicVo
     * @return
     */
    boolean deleteConfigurePlan(BasicVo<Long> basicVo);

    /**
     * 查询配置方案相关联的配置方案数据
     * @param basicVo
     * @return
     */
    Integer configurePlanDataCount(BasicVo<Long> basicVo);

    /**
     * 新增或修改方案配数据
     * @param configurePlanDataVo
     * @return
     */
    boolean aueConfigurePlanData(ConfigurePlanDataVo configurePlanDataVo);

    /**
     * 根据buildId，floorName查询mongodb的对应的json
     * @param jsonPlanVo
     * @return
     */
    Map<String,Object> selectJsonPlan(JsonPlanVo jsonPlanVo);
}
