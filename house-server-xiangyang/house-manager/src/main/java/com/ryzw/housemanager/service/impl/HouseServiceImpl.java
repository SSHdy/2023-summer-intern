package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.*;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.*;
import com.ryzw.housemanager.service.*;
import com.ryzw.housemanager.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 房屋信息表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2018-12-11
 */
@Service
@Slf4j
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House> implements IHouseService {
    @Autowired
    HouseMapper houseMapper;
    @Autowired
    HousingUseMapper housingUseMapper;
    @Autowired
    UseRelationshipMapper useRelationshipMapper;
    @Autowired
    HousingUseServiceImpl housingUseService;
    @Autowired
    UseRelationshipServiceImpl useRelationshipService;
    @Autowired
    ILeadingOfficeService leadingOfficeService;
    @Autowired
    LeadingOfficeMapper leadingOfficeMapper;

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private ConfigurePlanDataMapper configurePlanDataMapper;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    RentRelationshipMapper rentRelationshipMapper;

    @Autowired
    private IUseUnitService useUnitService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    HandleModuleMapper handleModuleMapper;
    @Autowired
    IAuthorityUnitService authorityUnitService;
    @Autowired
    private HandleMapper handleMapper;

    @Autowired
    IConfigureService configureService;

    @Autowired
    IConfigureRoomService configureRoomService;

    @Autowired
    IHouseHistoryService houseHistoryService;

    /**
     * 房屋使用详情
     *
     * @param houseDetailVo
     * @return
     */
    @Override
    public HouseDto houseDetail(HouseDetailVo houseDetailVo) {
        try {
            return houseMapper.houseDetail(houseDetailVo);
        } catch (Exception ex) {
            log.error("houseDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 楼层房屋列表
     *
     * @param
     * @return
     */
    @Override
    public List<HouseDto> floorHouse(FloorHouseVo floorHouseVo) {
        try {
            return houseMapper.floorHouse(floorHouseVo);
        } catch (Exception ex) {
            log.error("floorHouseError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 新增UUID数组;修改房屋信息，单位关系，职称关系等数组;删除UUID数组
     *
     * @param addHouseIdList
     * @param houseVoList
     * @param delHouseIdList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addUpdateDeleteHouseAndPlan(Long yardId, Long buildId, Integer floorName, String recordHolder, Long recordHolderId, List<String> addHouseIdList, List<HouseVo> houseVoList, List<String> delHouseIdList, List<Long> configureIdList, List<String> rentHouseIds, JsonPlanVo jsonPlanVo) {
        try {
            /*************************配置相关操作*******************************/
            if (CollectionUtils.isNotEmpty(configureIdList)) {
                configureService.updateConfigureDate(configureIdList);
            }

            //删除房间，删除关联的配置相关数据
            FloorHouseListVo floorHouseListVo = new FloorHouseListVo();
            floorHouseListVo.setBuildId(buildId);
            List<Integer> floorNameList = new ArrayList<>();
            floorNameList.add(floorName);
            floorHouseListVo.setFloorNameList(floorNameList);
            List<String> houseIds = configureRoomService.floorHouse(floorHouseListVo);
            if (!Collections.disjoint(houseIds, delHouseIdList)) {
                houseIds.retainAll(delHouseIdList);
                configurePlanDataMapper.deleteByHouseId(houseIds);
            }

            /********************************************************/
            String detailKey = "build:" + buildId + ":assert";
            if (redisTemplate.hasKey(detailKey)) {
                redisTemplate.delete(detailKey);
            }

            //删除redis中楼栋系数
            String buildCoeKey = "buildCoe:" + buildId;
            if (redisTemplate.hasKey(buildCoeKey)) {
                redisTemplate.delete(buildCoeKey);
            }


            //批量删除多个UUID
            if (CollectionUtils.isNotEmpty(delHouseIdList)) {
                deleteAllList(delHouseIdList);
            }

            //新增多个UUID，其他值为null的房屋对象
            if (CollectionUtils.isNotEmpty(addHouseIdList)) {
                addUUIDList(addHouseIdList, buildId, floorName);
            }

            //修改house数组，修改对应的house，单位关系表，职称关系表
            if (CollectionUtils.isNotEmpty(houseVoList)) {
                for (HouseVo houseVo : houseVoList) {
                    //获取院落楼座单位关系信息数组
                    List<HousingUseVo> housingUseVoList = houseVo.getHousingUseVoList();
                    //新增house
                    House house = dozerBeanMapper.map(houseVo, House.class);
                    HouseHistory houseHistory = dozerBeanMapper.map(houseVo, HouseHistory.class);

                    Record record = new Record();
                    if (houseVo.getIsRecord() != null && houseVo.getIsRecord().equals(1)) {
                        //超标
                        house.setIsExcess(1);
                        houseHistory.setIsExcess(1);
                        record.setHouseId(house.getHouseId());
                        record.setRecordHolder(recordHolder);
                        record.setRecordHolderId(recordHolderId);
                        record.setRecordDate(new Date());
                        recordMapper.insert(record);
                        if (record.getRecordId() != null) {
                            house.setRecordId(record.getRecordId());
                            houseHistory.setRecordId(record.getRecordId());
                        }
                    }
                    if (houseVo.getIsRecord() != null && houseVo.getIsRecord().equals(0)) {
                        //未超标
                        house.setIsExcess(0);
                        house.setRecordId(0);
                        houseHistory.setIsExcess(0);
                        houseHistory.setRecordId(0);
                    }
                    //房间信息修改
                    houseMapper.updateById(house);
                    houseHistoryService.updateById(houseHistory);
                    //获取houseId
                    String houseId = house.getHouseId();
                    LeadingOfficeVo leadingOfficeVo = houseVo.getLeadingOfficeVo();


                    List<String> houseList = houseVoList.stream().filter(p -> p.getIsUse() != null && p.getIsUse().equals(0))
                            .map(HouseVo::getHouseId).collect(Collectors.toList());
                    //房间改为不使用的
                    if (CollectionUtils.isNotEmpty(houseList)) {
                        updateHouseUse(houseList);
                    }

                    HousingUse housingUse = dozerBeanMapper.map(houseVo, HousingUse.class);
                    //由出租改为自用房间
                    if (CollectionUtils.isNotEmpty(rentHouseIds)) {
                        updateHouseUse(rentHouseIds);

                        if (rentHouseIds.contains(houseId)) {
                            housingUse.setHousinguseId(null);
                        }
                    }

                    //把楼座编号和院落编号存储在关系表中
                    housingUse.setBuildId(buildId).setYardId(yardId).setHouseId(houseId).setFloorName(floorName).setIsUse(houseVo.getIsUse());
                    //新增院落楼座单位关系数据
                    housingUseService.saveOrUpdate(housingUse);

                    if (leadingOfficeVo != null) {
                        //新增领导人员办公信息以及房间人员职称关系信息
                        addLeadingOffice(leadingOfficeVo, houseId, buildId, yardId, floorName, houseVo.getIsUse(), houseVo.getIsIdle());
                    }

                    if (CollectionUtils.isNotEmpty(housingUseVoList) || (houseVo.getIsUse() != null && houseVo.getIsUse().equals(0))) {
                        if (CollectionUtils.isNotEmpty(housingUseVoList)) {
                            for (HousingUseVo housingUseVo : housingUseVoList) {
                                UseUnit useunit = dozerBeanMapper.map(housingUseVo, UseUnit.class);
                                if (!houseVo.getIsUse().equals(0) && CollectionUtils.isNotEmpty(housingUseVo.getLeadingOfficeList()) ||
                                        houseVo.getIsUse().equals(1) && CollectionUtils.isEmpty(housingUseVo.getLeadingOfficeList())) {
                                    if (useunit.getUseunitId() == null) {
                                        if(CollectionUtils.isEmpty(houseVo.getHousingUseUnitList())){
                                            useunit.setHousinguseId(housingUse.getHousinguseId());
                                            useUnitService.save(useunit);
                                        }
                                    } else {
                                        useUnitService.updateById(useunit);
                                    }
                                }

                                //获取单位序号并传给职称信息
                                Long unitId = housingUseVo.getUnitId();

                                //新增或修改一般人员办公信息
                                List<LeadingOffice> leadingOfficeList = housingUseVo.getLeadingOfficeList();
                                if (CollectionUtils.isNotEmpty(leadingOfficeList)) {
                                    addLeadingOfficeList(leadingOfficeList, unitId, houseId, useunit.getUseunitId());
                                }

                                //获取房间人员职称关系信息数组
                                List<UseRelationship> useRelationshipList = housingUseVo.getUseRelationshipList();
                                //新增房间人员职称关系信息
                                if (CollectionUtils.isNotEmpty(useRelationshipList)) {
                                    addUseRelationShipList(useRelationshipList, unitId, houseId);
                                }

                                //删除人员
                                if (CollectionUtils.isNotEmpty(housingUseVo.getDeleteLeadingOfficeIds())) {
                                    leadingOfficeMapper.deleteBatchIds(housingUseVo.getDeleteLeadingOfficeIds());
                                }
                            }
                        }
                    }

                    //删除一般人员使用信息
                    if (CollectionUtils.isNotEmpty(houseVo.getDeleteUseUnitIds())) {
                        useUnitService.removeByIds(houseVo.getDeleteUseUnitIds());
                    }

                    //非办公用房
                    if (CollectionUtils.isNotEmpty(houseVo.getHousingUseUnitList())) {
                        useRelationshipMapper.delete(new QueryWrapper<UseRelationship>().eq("house_id", houseId));
                        for (HouseUseUnitVo houseUseUnitVo : houseVo.getHousingUseUnitList()) {
                            UseUnit useunit = dozerBeanMapper.map(houseUseUnitVo, UseUnit.class);
                            useunit.setHousinguseId(housingUse.getHousinguseId());
                            useUnitService.saveOrUpdate(useunit);

                            UseRelationship useRelationship = new UseRelationship();
                            useRelationship.setHouseId(houseId);
                            useRelationship.setUnitId(houseUseUnitVo.getUnitId());
                            useRelationshipService.save(useRelationship);
                        }

                    }
                }
            }

            //平面图
            addJsonPlan(jsonPlanVo);
            return true;
        } catch (Exception ex) {
            log.error("addUpdateDeleteHouseAndPlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    public void updateHouseUse(List<String> houseList) {
        HousingUse updateUse = new HousingUse();
        updateUse.setIsUse(0);
        UpdateWrapper<HousingUse> updateWrapper = new UpdateWrapper<>();
        updateWrapper.in("house_id", houseList);
        updateWrapper.eq("is_use", 1);
        housingUseMapper.update(updateUse, updateWrapper);
    }

    /**
     * 删除所有关于房间的信息
     *
     * @param delHouseIdList
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllList(List<String> delHouseIdList) {
        try {
            int useCount = rentRelationshipMapper.selectRentCount(delHouseIdList);
            if (useCount > 0) {
                throw new ServiceException("房间存在租用关系，不能删除！");
            }
//            int recordCount = recordMapper.selectCount(new QueryWrapper<Record>().in("house_id", delHouseIdList));
//            if (recordCount > 0) {
//                throw new ServiceException("房间存在维修提醒关系，不能删除！");
//            }

            int configurePlanDataCount = configurePlanDataMapper.selectCount(new QueryWrapper<ConfigurePlanData>().in("house_id", delHouseIdList).eq("is_idle", 1));
            if (configurePlanDataCount > 0) {
                throw new ServiceException("房间存在配置方案数据关系，不能删除！");
            }

            int handleCount = handleMapper.handleCount(delHouseIdList);
            if (handleCount > 0) {
                throw new ServiceException("房间存在处置关系，不能删除！");
            }

            //删除房屋
            if (CollectionUtils.isNotEmpty(delHouseIdList)) {
                houseMapper.deleteByHouseId(delHouseIdList);
            }

        } catch (Exception ex) {
            log.error("deleteAllListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 新增UUID房间
     *
     * @param addHouseIdList
     * @param buildId
     * @param floorName
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addUUIDList(List<String> addHouseIdList, Long buildId, Integer floorName) {
        try {
            List<House> houseList = Lists.newArrayList();
            for (String str : addHouseIdList) {
                House house = new House().setHouseId(str).setBuildId(buildId).setFloorName(floorName);
                houseList.add(house);
            }

            List<HouseHistory> houseHistoryList = Lists.newArrayList();
            for (String str : addHouseIdList) {
                HouseHistory houseHistory = new HouseHistory().setHouseId(str).setBuildId(buildId).setFloorName(floorName);
                houseHistoryList.add(houseHistory);
            }
            this.saveBatch(houseList);
            houseHistoryService.saveBatch(houseHistoryList);
        } catch (Exception ex) {
            log.error("addUUIDListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 新增领导人员办公信息以及房间人员职称关系信息
     *
     * @param leadingOfficeVo
     * @param houseId
     * @param buildId
     * @param yardId
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addLeadingOffice(LeadingOfficeVo leadingOfficeVo, String houseId, Long buildId, Long yardId, Integer floorName, Integer isUse, Integer isIdle) {
        try {
            HousingUse updateUse = new HousingUse();
            updateUse.setIsUse(0);
            UpdateWrapper<HousingUse> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("house_id", houseId);
            updateWrapper.eq("is_use", 1);
            housingUseMapper.update(updateUse, updateWrapper);


            //获取领导相关的housingUse信息
            HousingUse housingUse = new HousingUse();
            //通过leadingOfficeVo对象获取对象关系表的值
            Date distributionTime = leadingOfficeVo.getDistributionTime();
            String useForm = leadingOfficeVo.getUseForm();
            Long unitId = leadingOfficeVo.getUnitId();
//            Integer professionalId = leadingOfficeVo.getProfessionalId();

            //把楼座编号和院落编号存储在关系表中
            housingUse.setBuildId(buildId).setYardId(yardId).setHouseId(houseId)
                    .setHousinguseId(leadingOfficeVo.getHousinguseId())
                    .setFloorName(floorName).setIsUse(isUse);
            //新增院落楼座单位关系数据
            housingUseService.saveOrUpdate(housingUse);

            UseUnit useUnit = new UseUnit();
            useUnit.setHousinguseId(housingUse.getHousinguseId()).setUnitId(unitId).setDistributionTime(distributionTime)
                    .setUseForm(useForm).setDepartmentName(leadingOfficeVo.getDepartmentName());
            useUnitService.saveOrUpdate(useUnit);

            //获取领导下面的房间人员职称关系信息
            if (CollectionUtils.isNotEmpty(leadingOfficeVo.getLeadingOfficeList())) {
                for (LeadingOffice leadingOffice : leadingOfficeVo.getLeadingOfficeList()) {
                    //这是保存单个的方法
//                    LeadingOffice leadingOffice = dozerBeanMapper.map(leadingOfficeVo, LeadingOffice.class);
                    //前端传过来的unitId是放在leadingoffice里面的，所以获取的时候，直接就有，不需要通过housingUse里面获取，因为约定里面传null
                    leadingOffice.setHouseId(houseId).setUnitId(leadingOfficeVo.getUnitId()).setIsLeading(LeadingTypeEnum.LEADING.getValue()).setUseunitId(useUnit.getUseunitId());
                    //新增或修改领导人员办公信息
                    leadingOfficeService.saveOrUpdate(leadingOffice);
                }
            }


            //领导只会对应一个职称关系
            if (CollectionUtils.isNotEmpty(leadingOfficeVo.getUseRelationshipList())) {
                List<UseRelationship> useRelationships = new ArrayList<>();
                for (UseRelationship useRelationship : leadingOfficeVo.getUseRelationshipList()) {
//                    UseRelationship useRelationship = new UseRelationship(leadingOfficeVo.getJobId(), houseId, professionalId, leadingOffice.getUnitId(), leadingOfficeVo.getRoomNum());

                    //职称人数为零，就进行删除操作
                    if (useRelationship.getRoomNum().equals(0)) {
                        if (useRelationship.getJobId() != null) {
                            useRelationshipService.removeById(useRelationship.getJobId());
                        } else {
                            throw new ServiceException("jobId不能为空！");
                        }
                    } else {
                        //新增或修改房间人员职称关系数据
                        useRelationship.setUnitId(unitId).setHouseId(houseId);
                        useRelationships.add(useRelationship);
                    }

//                    useRelationship.setHouseId(houseId).setUnitId(leadingOfficeVo.getUnitId());
                    //新增或修改房间人员职称关系
//                    useRelationshipService.saveOrUpdate(useRelationship);
                }
                if (CollectionUtils.isNotEmpty(useRelationships)) {
                    useRelationshipService.saveOrUpdateBatch(useRelationships);
                }
            }

            //删除人员
            if (CollectionUtils.isNotEmpty(leadingOfficeVo.getDeleteLeadingOfficeIds())) {
                leadingOfficeMapper.deleteBatchIds(leadingOfficeVo.getDeleteLeadingOfficeIds());
            }
        } catch (Exception ex) {
            log.error("addLeadingOfficeError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 新增房间人员职称关系信息
     *
     * @param useRelationshipList
     * @param unitId
     * @param houseId
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addUseRelationShipList(List<UseRelationship> useRelationshipList, Long unitId, String houseId) {
        try {
            List<UseRelationship> useRelationships = new ArrayList<>();
            for (UseRelationship useRelationship : useRelationshipList) {
                //职称人数为零，就进行删除操作
                if (useRelationship.getRoomNum().equals(0)) {
                    if (useRelationship.getJobId() != null) {
                        useRelationshipService.removeById(useRelationship.getJobId());
                    }
                } else {
                    //新增或修改房间人员职称关系数据
                    useRelationship.setUnitId(unitId).setHouseId(houseId);
                    useRelationships.add(useRelationship);
                }
            }
            if (CollectionUtils.isNotEmpty(useRelationships)) {
                useRelationshipService.saveOrUpdateBatch(useRelationships);
            }
        } catch (Exception ex) {
            log.error("addUseRelationShipListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 新增一般办公人员
     *
     * @param leadingOfficeList
     * @param unitId
     * @param houseId
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addLeadingOfficeList(List<LeadingOffice> leadingOfficeList, Long unitId, String houseId, Long useunitId) {
        try {
            List<LeadingOffice> leadingOfficeLists = new ArrayList<>();
            for (LeadingOffice leadingOffice : leadingOfficeList) {
                //新增或修改房间人员职称关系数据
                leadingOffice.setUnitId(unitId).setHouseId(houseId).setIsLeading(LeadingTypeEnum.GENERAL.getValue()).setUseunitId(useunitId);
                leadingOfficeLists.add(leadingOffice);
            }
            leadingOfficeService.saveOrUpdateBatch(leadingOfficeLists);
        } catch (Exception ex) {
            log.error("addLeadingOfficeListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 把jsonPlan数据新增到mongodb
     *
     * @param jsonPlanVo
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void addJsonPlan(JsonPlanVo jsonPlanVo) {
        try {
            Query query = Query.query(Criteria.where("buildId").is(jsonPlanVo.getBuildId())).
                    addCriteria(Criteria.where("floorName").is(jsonPlanVo.getFloorName()));
            if (MapUtils.isNotEmpty(jsonPlanVo.getJsonPlan())) {
                Update update = Update.update("jsonPlan", jsonPlanVo.getJsonPlan());
                mongoTemplate.upsert(query, update, JsonPlanVo.class);
            } else {
                mongoTemplate.remove(query, JsonPlanVo.class);
            }
        } catch (Exception ex) {
            log.error("addJsonPlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 根据builidId和floorName查询mongodb对应的json
     *
     * @param jsonPlanVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> selectJsonPlan(JsonPlanVo jsonPlanVo) {
        try {
            Query query = Query.query(Criteria.where("buildId").is(jsonPlanVo.getBuildId())).
                    addCriteria(Criteria.where("floorName").is(jsonPlanVo.getFloorName()));
            JsonPlanVo newJsonPlanVo = mongoTemplate.findOne(query, jsonPlanVo.getClass());

            Optional<JsonPlanVo> newJsonPlanVo1 = Optional.ofNullable(newJsonPlanVo);

            if (newJsonPlanVo1.isPresent()) {
                return newJsonPlanVo1.get().getJsonPlan();
            } else {
                return null;
            }

//            return Optional.ofNullable(newJsonPlanVo).orElse(null).getJsonPlan();
//                    .orElseThrow(() -> new ServiceException("此楼层暂无示意图！"))
//                    .getJsonPlan();
        } catch (Exception ex) {
            log.error("selectJsonPlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 房屋信息查询
     *
     * @param houseInfoListVo
     * @return
     */
    @Override
    public Page<HouseInfoListDto> houseInfoList(HouseInfoListVo houseInfoListVo) {
        try {
            Page<HouseInfoListDto> page = new Page<>(houseInfoListVo.getCurrentPage(), houseInfoListVo.getPageSize());
            return houseMapper.houseInfoList(page, houseInfoListVo);
        } catch (Exception ex) {
            log.error("houseInfoListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 房屋超标报表查询
     *
     * @param houseProofVo
     * @return
     */
    @Override
    public Page<HouseProofDto> houseProof(HouseProofVo houseProofVo) {
        try {
            Page<HouseProofDto> page = new Page<>(houseProofVo.getCurrentPage(), houseProofVo.getPageSize());
            //获取用户可以查看的单位信息
            List<Long> unitIdsByToken = authorityUnitService.getUnitIdByToken();
            List<Long> unitIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(houseProofVo.getUnitIdList())) {
                unitIds.addAll(houseProofVo.getUnitIdList());
            } else {
                unitIds.addAll(unitIdsByToken);
            }
            return houseMapper.houseProof(page, houseProofVo, unitIds);
        } catch (Exception ex) {
            log.error("houseProofError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 查询无人办公室
     *
     * @param noOfficeVo
     * @return
     */
    @Override
    public Page<NoOfficeDto> selectNoOffice(NoOfficeVo noOfficeVo) {
        try {
            Page<HouseInfoListDto> page = new Page<>(noOfficeVo.getCurrentPage(), noOfficeVo.getPageSize());
            return houseMapper.selectNoOffice(page, noOfficeVo);
        } catch (Exception ex) {
            log.error("selectNoOfficeError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 单位占用汇总
     *
     * @param unitNameVo
     * @return
     */
    @Override
    public Page<UnitUsageDto> selectUnitUsage(UnitNameVo unitNameVo) {
        try {
            Page<HouseInfoListDto> page = new Page<>(unitNameVo.getCurrentPage(), unitNameVo.getPageSize());
            return houseMapper.selectUnitUsage(page, unitNameVo);
        } catch (Exception ex) {
            log.error("selectUnitUsageError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 闲置用房树查询
     *
     * @return
     */
    @Override
    public List<SpareRoomTreeDto> spareRoomTree(SpareTreeDto spareTreeDto) {
        try {
            return houseMapper.spareRoomTree(spareTreeDto);
        } catch (Exception ex) {
            log.error("spareRoomTreeError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 闲置用房信息查询
     *
     * @return
     */
    @Override
    public SpareRoomAndSumDto spareRoom(SpareRoomVo spareRoomVo, List<Long> unitIds) {
        try {
            Page<SpareRoomDto> page = new Page<>(spareRoomVo.getCurrentPage(), spareRoomVo.getPageSize());
            //计算总数
            List<SpareRoomDto> spareRoomDtoList = houseMapper.spareRoom(spareRoomVo, unitIds);
            BigDecimal useAreaSum = new BigDecimal("0.00");
            BigDecimal buildAreaSum = new BigDecimal("0.00");
            if (CollectionUtils.isNotEmpty(spareRoomDtoList)) {
                for (SpareRoomDto spareRoomDto : spareRoomDtoList) {
                    if (spareRoomDto.getHouseUseArea() != null) {
                        // 不使用建筑面积 所以使用面积合计里要加上
                        if (spareRoomDto.getUseBuildArea() == 0) {
                            useAreaSum = useAreaSum.add(new BigDecimal(String.valueOf(spareRoomDto.getHouseUseArea()))).setScale(2, BigDecimal.ROUND_HALF_UP);
                        }
                        if (spareRoomDto.getBuildCoe() != null) {
                            // 为0代表需要用系数计算  否则直接加上建筑面积
                            if (spareRoomDto.getUseBuildArea() == 0) {
                                double buildArea = spareRoomDto.getHouseUseArea() / spareRoomDto.getBuildCoe();
                                buildAreaSum = buildAreaSum.add(new BigDecimal(String.valueOf(buildArea))).setScale(2, BigDecimal.ROUND_HALF_UP);
                            } else {
                                buildAreaSum = buildAreaSum.add(new BigDecimal(String.valueOf(spareRoomDto.getHouseUseArea())));
                            }
                        }
                    }
                }
            }
            Page<SpareRoomDto> spareRoomDtoPage = houseMapper.spareRoom(page, spareRoomVo, unitIds);
            SpareRoomAndSumDto spareRoomAndSumDto = new SpareRoomAndSumDto();
            spareRoomAndSumDto.setSpareRoomDtoPage(spareRoomDtoPage);
            spareRoomAndSumDto.setHouseUseAreaSum(useAreaSum);
            spareRoomAndSumDto.setHouseBuildAreaSum(buildAreaSum);
            return spareRoomAndSumDto;
        } catch (Exception ex) {
            log.error("spareRoomError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 租赁房屋报表查询
     *
     * @param rentRoomReportVo
     * @return
     */
    @Override
    public RentRoomAndSumDto rentRoomReport(RentRoomReportVo rentRoomReportVo) {
        try {
            //获取用户可以查看的单位信息
            List<Long> unitIds = authorityUnitService.getUnitIdByToken();
            List<Long> unitIdList = new ArrayList<>();
            if (CollectionUtils.isEmpty(rentRoomReportVo.getUnitIdList())) {
                unitIdList.addAll(unitIds);
                rentRoomReportVo.setUnitIdList(unitIdList);
            }
            Page<RentRoomReportDto> page = new Page<>(rentRoomReportVo.getCurrentPage(), rentRoomReportVo.getPageSize());
            List<RentRoomReportDto> rentRoomReportDtos = houseMapper.rentRoomReport(rentRoomReportVo);
            BigDecimal useAreaSum = new BigDecimal("0.00");
            BigDecimal buildAreaSum = new BigDecimal("0.00");
            if (CollectionUtils.isNotEmpty(rentRoomReportDtos)) {
                for (RentRoomReportDto rentRoomReportDto : rentRoomReportDtos) {
                    if (rentRoomReportDto.getHouseUseArea() != null) {
                        // 不使用建筑面积 所以使用面积合计里要加上
                        if (rentRoomReportDto.getUseBuildArea() == 0) {
                            useAreaSum = useAreaSum.add(new BigDecimal(String.valueOf(rentRoomReportDto.getHouseUseArea()))).setScale(2, BigDecimal.ROUND_HALF_UP);
                        }
                        if (rentRoomReportDto.getBuildCoe() != null) {
                            // 为0代表需要用系数计算  否则直接加上建筑面积
                            if (rentRoomReportDto.getUseBuildArea() == 0) {
                                double buildArea = rentRoomReportDto.getHouseUseArea() / rentRoomReportDto.getBuildCoe();
                                buildAreaSum = buildAreaSum.add(new BigDecimal(String.valueOf(buildArea))).setScale(2, BigDecimal.ROUND_HALF_UP);
                            } else {
                                buildAreaSum = buildAreaSum.add(new BigDecimal(String.valueOf(rentRoomReportDto.getHouseUseArea())));
                            }

                        }
                    }
                }
            }
            RentRoomAndSumDto rentRoomAndSumDto = new RentRoomAndSumDto();
            Page<RentRoomReportDto> rentRoomReportDtoPage = houseMapper.rentRoomReport(page, rentRoomReportVo);
            rentRoomAndSumDto.setSpareRoomDtoPage(rentRoomReportDtoPage);
            rentRoomAndSumDto.setHouseUseAreaSum(useAreaSum);
            rentRoomAndSumDto.setHouseBuildAreaSum(buildAreaSum);
            return rentRoomAndSumDto;
        } catch (Exception ex) {
            log.error("rentRoomReportError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 查询房间明细
     *
     * @param roomDetailVo
     * @return
     */
    @Override
    public Page<RoomDetailDto> roomDetail(RoomDetailVo roomDetailVo) {
        Page<RoomDetailDto> page = new Page<>(roomDetailVo.getCurrentPage(), roomDetailVo.getPageSize());
        //获取用户可以查看的单位信息
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();
        return houseMapper.roomDetail(page, roomDetailVo, unitIds);
    }

    /**
     * 信息档案楼层树
     *
     * @param buildAndYardVo
     * @return
     */
    @Override
    public MessageFloorDto messageFloorTree(BuildAndYardVo buildAndYardVo) {
        try {

            return houseMapper.messageFloorTree(buildAndYardVo);
        } catch (Exception ex) {
            log.error("rentRoomReportError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


}