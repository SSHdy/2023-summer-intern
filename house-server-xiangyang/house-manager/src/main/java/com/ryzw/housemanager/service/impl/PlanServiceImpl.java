package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryzw.housemanager.dto.ConfigurePlanUnitDto;
import com.ryzw.housemanager.entity.*;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.*;
import com.ryzw.housemanager.service.IPlanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.ConfigurePlanDataVo;
import com.ryzw.housemanager.vo.ConfigurePlanVo;
import com.ryzw.housemanager.vo.JsonPlanVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * <p>
 * 方案表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-06-04
 */
@Service
@Slf4j
public class PlanServiceImpl extends ServiceImpl<PlanMapper, Plan> implements IPlanService {

    @Autowired
    private PlanMapper planMapper;

    @Autowired
    private ConfigurePlanDataMapper configurePlanDataMapper;

    @Autowired
    private ConfigurePlanOldUnitMapper configurePlanOldUnitMapper;

    @Autowired
    private ConfigurePlanNewUnitMapper configurePlanNewUnitMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ConfigurePlanChangeDataMapper configurePlanChangeDataMapper;


    @Override
    public List<Plan> configurePlanList(ConfigurePlanVo configurePlanVo) {
        try {
            QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("configure_id", configurePlanVo.getConfigureId());
            List<Plan> planList = planMapper.selectList(queryWrapper);
            return planList;
        } catch (Exception ex) {
            log.error("configurePlanListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 新增配置方案
     *
     * @param plan
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addConfigurePlan(Plan plan) {
        try {
            QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("configure_id", plan.getConfigureId());
            queryWrapper.eq("plan_name", plan.getPlanName());
            Integer planCount = planMapper.selectCount(queryWrapper);
            if (planCount > 0) {
                throw new ServiceException("该配置申请下方案名称不能重复！");
            }
            planMapper.insert(plan);
            return true;
        } catch (Exception ex) {
            log.error("addConfigurePlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改配置方案
     *
     * @param plan
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfigurePlan(Plan plan) {
        try {
            if (plan.getPlanId() != null) {
                planMapper.updateById(plan);
                QueryWrapper<Plan> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("configure_id", plan.getConfigureId());
                queryWrapper.eq("plan_name", plan.getPlanName());
                Integer planCount = planMapper.selectCount(queryWrapper);
                if (planCount > 1) {
                    throw new ServiceException("该配置申请下方案名称不能重复！");
                }
                return true;
            } else {
                throw new ServiceException("配置方案ID不能为空！");
            }
        } catch (Exception ex) {
            log.error("updateConfigurePlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除配置方案
     *
     * @param basicVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConfigurePlan(BasicVo<Long> basicVo) {
        try {
            planMapper.deleteByPlanId(basicVo.getId());
            return true;
        } catch (Exception ex) {
            log.error("deleteConfigurePlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询配置方案相关联的配置方案数据
     *
     * @param basicVo
     * @return
     */
    @Override
    public Integer configurePlanDataCount(BasicVo<Long> basicVo) {
        try {
            Integer configurePlanDataCount = configurePlanDataMapper.selectCount(new QueryWrapper<ConfigurePlanData>().eq("plan_id", basicVo.getId()));
            return configurePlanDataCount;
        } catch (Exception ex) {
            log.error("configurePlanDataCountError", ex);
            throw new ServiceException(ex.getMessage());
        }

    }

    /**
     * 新增或修改方案配数据
     *
     * @param configurePlanDataVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean aueConfigurePlanData(ConfigurePlanDataVo configurePlanDataVo) {
        try {
            //方案数据
            if (CollectionUtils.isNotEmpty(configurePlanDataVo.getConfigurePlanDataList())) {
                for (ConfigurePlanData configurePlanData : configurePlanDataVo.getConfigurePlanDataList()) {
                    if (configurePlanData.getConfigurePlanDataId() == null) {
                        configurePlanData.setPlanId(configurePlanDataVo.getPlanId()).setConfigureId(configurePlanDataVo.getConfigureId()).setPlanType(2);
                        configurePlanDataMapper.insert(configurePlanData);
                    } else {
                        configurePlanDataMapper.updateById(configurePlanData);
                        //修改删除重新分配单位
                        if (CollectionUtils.isNotEmpty(configurePlanData.getDeleteCPNUnitIds())) {
                            configurePlanNewUnitMapper.deleteBatchIds((configurePlanData.getDeleteCPNUnitIds()));
                        }

                        //删除原使用单位
                        if(CollectionUtils.isNotEmpty(configurePlanData.getDeleteCPOUnitIds())){
                            configurePlanOldUnitMapper.deleteBatchIds(configurePlanData.getDeleteCPOUnitIds());
                        }
                    }

                    if ((CollectionUtils.isNotEmpty(configurePlanData.getConfigurePlanOldUnits()) && CollectionUtils.isNotEmpty(configurePlanData.getConfigurePlanNewUnits()))) {
                        //原使用单位
                        List<Long> oldUnitIds = new ArrayList<>();
                        for (ConfigurePlanOldUnit configurePlanOldUnit : configurePlanData.getConfigurePlanOldUnits()) {
                            if (configurePlanOldUnit.getUnitId() != null) {
                                oldUnitIds.add(configurePlanOldUnit.getUnitId());
                            }
                        }

                        //新使用单位
                        List<Long> newUnitIds = new ArrayList<>();
                        for (ConfigurePlanNewUnit configurePlanNewUnit : configurePlanData.getConfigurePlanNewUnits()) {
                            newUnitIds.add(configurePlanNewUnit.getUnitId());
                        }

                        if (CollectionUtils.isNotEmpty(oldUnitIds) && CollectionUtils.isNotEmpty(newUnitIds)) {
                            if (oldUnitIds.size() == newUnitIds.size()) {
                                Collections.sort(oldUnitIds);
                                Collections.sort(newUnitIds);
                                if (oldUnitIds.containsAll(newUnitIds)) {
                                    throw new ServiceException("新分配单位不能与原使用单位一样！");
                                }
                            }
                        }
                    }


                    if (CollectionUtils.isNotEmpty(configurePlanData.getConfigurePlanOldUnits())) {
                        for (ConfigurePlanOldUnit configurePlanOldUnit : configurePlanData.getConfigurePlanOldUnits()) {
                            if (configurePlanOldUnit.getConfigurePlanOldUnitId() == null) {
                                configurePlanOldUnit.setConfigurePlanDataId(configurePlanData.getConfigurePlanDataId());
                                configurePlanOldUnitMapper.insert(configurePlanOldUnit);
                            }
                        }
                    }

                    if (CollectionUtils.isNotEmpty(configurePlanData.getConfigurePlanNewUnits())) {
                        for (ConfigurePlanNewUnit configurePlanNewUnit : configurePlanData.getConfigurePlanNewUnits()) {
                            if (configurePlanNewUnit.getConfigurePlanNewUnitId() == null) {
                                configurePlanNewUnit.setConfigurePlanDataId(configurePlanData.getConfigurePlanDataId());
                                configurePlanNewUnitMapper.insert(configurePlanNewUnit);
                            }
                        }
                    }
                }
            }

            //删除原先有的发生改变的关联方案数据
            List<ConfigurePlanChangeData> configurePlanChangeDataList = configurePlanChangeDataMapper.
                    selectList(new QueryWrapper<ConfigurePlanChangeData>().eq("plan_id",configurePlanDataVo.getPlanId()));
            if(CollectionUtils.isNotEmpty(configurePlanChangeDataList)){
                configurePlanChangeDataMapper.delete(new QueryWrapper<ConfigurePlanChangeData>().eq("plan_id",configurePlanDataVo.getPlanId()));
            }

            //方案数据改变的数据
            if(CollectionUtils.isNotEmpty(configurePlanDataVo.getConfigurePlanChangeDataList())){
                for(ConfigurePlanChangeData configurePlanChangeData : configurePlanDataVo.getConfigurePlanChangeDataList()){
                    configurePlanChangeData.setPlanId(configurePlanDataVo.getPlanId());
                    configurePlanChangeDataMapper.insert(configurePlanChangeData);
                }
            }

            //撤销操作
            if (CollectionUtils.isNotEmpty(configurePlanDataVo.getDeleteCPDIds())) {
                configurePlanDataMapper.deleteByCPDataIds(configurePlanDataVo.getDeleteCPDIds());
            }
            return true;
        } catch (Exception ex) {
            log.error("aueConfigurePlanDataError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 根据buildId，floorName查询mongodb的对应的json
     *
     * @param jsonPlanVo
     * @return
     */
    @Override
    public Map<String, Object> selectJsonPlan(JsonPlanVo jsonPlanVo) {
        try {
            Query query = Query.query(Criteria.where("buildId").is(jsonPlanVo.getBuildId())).
                    addCriteria(Criteria.where("floorName").is(jsonPlanVo.getFloorName()));
            JsonPlanVo newJsonPlanVo = mongoTemplate.findOne(query, jsonPlanVo.getClass());
            if (newJsonPlanVo != null) {
                Map<String, Object> jsonPlan = newJsonPlanVo.getJsonPlan();
                if (jsonPlan.get("objects") != null) {
                    List<Map<String, Object>> objectList = (List<Map<String, Object>>) jsonPlan.get("objects");
                    if (CollectionUtils.isNotEmpty(objectList)) {
                        for (Map<String, Object> object : objectList) {
                          /*  List<ConfigurePlanUnitDto> configurePlanUnitList = new ArrayList<>();
                            if(object.get("uuid")!=null){
                               configurePlanUnitList = configurePlanDataMapper.selectConfigurePlanUnit(String.valueOf(object.get("uuid")));
                            }*/

                            if (object.get("type") != null && object.get("type").equals("group")) {
                                List<Map<String, Object>> objectDataList = (List<Map<String, Object>>) object.get("objects");
   /*                             if (object.get("objects") != null) {
                                    if (CollectionUtils.isNotEmpty(objectDataList)) {
                                        for (Map<String, Object> objectData : objectDataList) {
                                            if (objectData.get("text") != null && String.valueOf(objectData.get("text")).contains("核定面积")) {
                                                if(CollectionUtils.isNotEmpty(configurePlanUnitList)){
                                                    if(configurePlanUnitList.size()==1){
                                                        objectData.put("text","使用单位："+configurePlanUnitList.get(0).getUnitName());
                                                    }else{
                                                        objectData.put("text","使用单位："+configurePlanUnitList.get(0).getUnitName()+"...");
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }*/

                                if(object.get("isUse") != null && object.get("isUse").equals(false)){
                                    Map<String,Object> xzMap = new HashMap<>();
                                    xzMap.put("type","text");
                                    xzMap.put("version","3.1.0");
                                    xzMap.put("originX","center");
                                    xzMap.put("originY","center");
                                    xzMap.put("left",0);
                                    xzMap.put("top",30);
                                    xzMap.put("width",141.55);
                                    xzMap.put("height",22.6);
                                    xzMap.put("fill","rgb(0,0,0)");
                                    xzMap.put("stroke",null);
                                    xzMap.put("strokeWidth",1);
                                    xzMap.put("strokeDashArray",null);
                                    xzMap.put("strokeLineCap","butt");
                                    xzMap.put("strokeDashOffset",0);
                                    xzMap.put("strokeLineJoin","miter");
                                    xzMap.put("strokeMiterLimit",4);
                                    xzMap.put("scaleX",1);
                                    xzMap.put("scaleY",1);
                                    xzMap.put("angle",0);
                                    xzMap.put("flipX",false);
                                    xzMap.put("flipY",false);
                                    xzMap.put("opacity",1);
                                    xzMap.put("shadow",null);
                                    xzMap.put("visible",true);
                                    xzMap.put("clipTo",null);
                                    xzMap.put("backgroundColor","");
                                    xzMap.put("fillRule","nonzero");
                                    xzMap.put("paintFirst","fill");
                                    xzMap.put("globalCompositeOperation","source-over");
                                    xzMap.put("transformMatrix",null);
                                    xzMap.put("skewX",0);
                                    xzMap.put("skewY",0);
                                    xzMap.put("text","闲置");
                                    xzMap.put("fontSize",20);
                                    xzMap.put("fontWeight","normal");
                                    xzMap.put("fontFamily","Times New Roman");
                                    xzMap.put("fontStyle","normal");
                                    xzMap.put("lineHeight",1.16);
                                    xzMap.put("underline",false);
                                    xzMap.put("overline",false);
                                    xzMap.put("linethrough",false);
                                    xzMap.put("textAlign","left");
                                    xzMap.put("textBackgroundColor","");
                                    xzMap.put("charSpacing",0);
                                    xzMap.put("textType","approvedArea");
                                    objectDataList.add(xzMap);
                                }
                            }
                        }
                    }
                }
            }

            return Optional.ofNullable(newJsonPlanVo)
                    .orElseThrow(() -> new ServiceException("此楼层暂无示意图！"))
                    .getJsonPlan();
        } catch (Exception ex) {
            log.error("selectJsonPlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


}
