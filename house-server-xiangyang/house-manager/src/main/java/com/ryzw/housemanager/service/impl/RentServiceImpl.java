package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.*;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.*;
import com.ryzw.housemanager.service.IRentService;
import com.ryzw.housemanager.service.IRepairRemindService;
import com.ryzw.housemanager.vo.JsonPlanVo;
import com.ryzw.housemanager.vo.RentEnclosureVo;
import com.ryzw.housemanager.vo.RentPageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 租赁房间信息表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-03-13
 */
@Service
@Slf4j
public class RentServiceImpl extends ServiceImpl<RentMapper, Rent> implements IRentService {
    @Autowired
    private RentMapper rentMapper;

    @Autowired
    private EnclosureMapper enclosureMapper;

    @Autowired
    private HousingUseMapper housingUseMapper;

    @Autowired
    private RentServiceImpl rentService;

    @Autowired
    private RentRelationshipMapper rentRelationshipMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RepairDicMapper repairDicMapper;

    @Autowired
    private IRepairRemindService repairRemindService;

    @Autowired
    private ContractServiceImpl contractService;

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private LeadingOfficeMapper leadingOfficeMapper;

    @Autowired
    private UseUnitMapper useUnitMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UseRelationshipMapper useRelationshipMapper;

    @Value("${uploadUrl.url}")
    private String url;


    /**
     * 新增租赁信息,租赁附件信息,院落楼座关系,租赁关系
     *
     * @param rent
     * @param housingUseList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRent(Rent rent, List<Enclosure> addressList, List<HousingUse> housingUseList, Integer state, List<Long> soilList) {
        try {
            if (rent.getType().equals(1)) {
                Date paymentTime = this.getPaymentTime(rent);
                rent.setPaymentTime(paymentTime);
            }
            //新增租赁信息
            rentMapper.insert(rent);
            //获取新增Rent后的rentId
            Long rentId = rent.getRentId();
            if (CollectionUtils.isNotEmpty(addressList)) {
                for (Enclosure enclosure : addressList) {
                    //新增租赁附件信息
                    enclosure.setRentId(rentId);
                    enclosureMapper.insert(enclosure);
                }
            }
            if (CollectionUtils.isNotEmpty(housingUseList) || CollectionUtils.isNotEmpty(soilList)) {
                if (housingUseList != null) {
                    List<String> houseIdList = housingUseList.stream().map(p -> p.getHouseId()).collect(Collectors.toList());
                    //删除人员使用关系
                    this.deleteUserRea(houseIdList);
                    List<Long> buildIdList = housingUseList.stream().map(p -> p.getBuildId()).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(buildIdList)) {
                        for (Long buildId : buildIdList) {
                            //删除redis中楼栋asser信息
                            redisTemplate.delete("build:" + buildId + ":assert");
                        }
                    }
                }
                //循环新增院落楼座关系
                addRentRelationship(housingUseList, rentId, state, soilList);
            }
            updateJsonPlan(housingUseList.stream().map(HousingUse::getHouseId).collect(Collectors.toList()));
            return true;
        } catch (Exception ex) {
            log.error("addRentError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除人员使用关系
     *
     * @param houseIdList
     * @return
     */
    public void deleteUserRea(List<String> houseIdList) {
        if (CollectionUtils.isNotEmpty(houseIdList)) {
            useRelationshipMapper.delete(new QueryWrapper<UseRelationship>().in("house_id", houseIdList));
        }
    }

    /**
     * 获取付款时间公共方法
     *
     * @param rent
     * @return
     */
    public Date getPaymentTime(Rent rent) {
        Date firstPaymentTime = rent.getFirstPaymentTime();
        Date caTime = firstPaymentTime;
        Date lastTime = null;
        //如果付款时间小于等于当前时间就加上周期
        while (caTime.getTime() <= (new Date()).getTime()) {
            Calendar ca = Calendar.getInstance();
            ca.setTime(caTime);
            lastTime = ca.getTime();
            ca.add(Calendar.MONTH, rent.getPaymentCycle());
            long time = ca.getTime().getTime();
            long time1 = (rent.getRentEnd()).getTime();
            if (time <= time1) {
                caTime = ca.getTime();
            } else {
                caTime = lastTime;
                break;
            }
        }
        return caTime;
    }

    /**
     * 修改租赁信息,租赁附件信息，院落楼座关系
     *
     * @param rentEnclosureVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRent(RentEnclosureVo rentEnclosureVo) {
        try {
            Rent rent = dozerBeanMapper.map(rentEnclosureVo, Rent.class);
            //获取租赁编号Id
            Long rentId = rent.getRentId();
            if (CollectionUtils.isNotEmpty(rentEnclosureVo.getHousingUseList()) || CollectionUtils.isNotEmpty(rentEnclosureVo.getSoilList())) {
                if (rentEnclosureVo.getHousingUseList() != null) {
                    List<String> houseIdList = rentEnclosureVo.getHousingUseList().stream().map(p -> p.getHouseId()).collect(Collectors.toList());
                    //删除人员使用关系
                    this.deleteUserRea(houseIdList);
                    Set<String> keys = redisTemplate.keys("build:" + "*" + ":assert");
                    // 删除redis中楼栋asser信息
                    redisTemplate.delete(keys);
                }
                //删除数据库原来的数据
                List<RentRelationship> rentRelationshipList = rentRelationshipMapper.selectList(new QueryWrapper<RentRelationship>().eq("rent_id", rentId));
                for (RentRelationship rentRelationship : rentRelationshipList) {
                    housingUseMapper.deleteById(rentRelationship.getHousinguseId());
                }
                rentRelationshipMapper.delete(new QueryWrapper<RentRelationship>().eq("rent_id", rentId));
                addRentRelationship(rentEnclosureVo.getHousingUseList(), rentId, rentEnclosureVo.getState(), rentEnclosureVo.getSoilList());
            }
            if (CollectionUtils.isNotEmpty(rentEnclosureVo.getDelHouseIdList())) {
                //获取减少的房间Id
                List<String> delHouseIdList = rentEnclosureVo.getDelHouseIdList();
                //查询出减少房间对应的最大的housingUseId
                List<Long> housingUseIdList = housingUseMapper.getMaxHouseId(delHouseIdList);
                if (CollectionUtils.isNotEmpty(housingUseIdList)) {
                    //删除使用单位关系
                    useUnitMapper.delete(new QueryWrapper<UseUnit>().in("housingUse_id", housingUseIdList));
                }
                //把减少的房间改回原来的颜色
                updateWithdrawalJsonPlan(rentEnclosureVo.getDelHouseIdList());
                for (String houseId : rentEnclosureVo.getDelHouseIdList()) {
                    UpdateWrapper<LeadingOffice> wrapper = new UpdateWrapper<>();
                    wrapper.set("is_delete", 0);
                    wrapper.eq("house_id", houseId);
                    //修改是否删除状态
                    leadingOfficeMapper.update(new LeadingOffice(), wrapper);
                }
            }
            //只有出租有付款时间
            if (rent.getType().equals(1)) {
                //修改租赁信息
                Date paymentTime = this.getPaymentTime(rent);
                rent.setPaymentTime(paymentTime);
                rent.setIsRead(0);
            }
            rentMapper.updateById(rent);
            //删除旧租赁照片服务器所在图片
            if (CollectionUtils.isNotEmpty(rentEnclosureVo.getDeleteEnclosureIdList())) {
                for (Long id : rentEnclosureVo.getDeleteEnclosureIdList()) {
                    Enclosure enclosure = enclosureMapper.selectById(id);
                    if (enclosure != null) {
                        rentService.deleteEnclosure(enclosure);
                    }
                }
            }
            //删除数据库原来的租赁附件
            if (CollectionUtils.isNotEmpty(rentEnclosureVo.getDeleteEnclosureIdList())) {
                for (Long enclosureId : rentEnclosureVo.getDeleteEnclosureIdList()) {
                    enclosureMapper.deleteById(enclosureId);
                }
            }
            if (CollectionUtils.isNotEmpty(rentEnclosureVo.getAddressList())) {
                //新增租赁附件
                for (Enclosure enclosure : rentEnclosureVo.getAddressList()) {
                    //新增租赁附件信息
                    enclosure.setRentId(rentId);
                    enclosureMapper.insert(enclosure);
                }
            }
            updateJsonPlan(rentEnclosureVo.getHousingUseList().stream().map(HousingUse::getHouseId).collect(Collectors.toList()));
            return true;
        } catch (Exception ex) {
            log.error("updateRentError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 修改图形颜色，将闲置房间类型改成出租房间类型
     *
     * @param list
     */
    private void updateJsonPlan(List<String> list) {
        try {
            if (CollectionUtils.isNotEmpty(list)) {
                Query query = new Query();
                query.addCriteria(Criteria.where("jsonPlan.objects.uuid").in(list)
                        .andOperator(Criteria.where("jsonPlan.objects").elemMatch(Criteria.where("objects.type").is("rect"))));
                List<JsonPlanVo> jsonPlanVoList = mongoTemplate.find(query, JsonPlanVo.class);
                BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, JsonPlanVo.class);
                if (CollectionUtils.isNotEmpty(jsonPlanVoList)) {
                    for (JsonPlanVo jsonPlanVo : jsonPlanVoList) {
                        Map<String, Object> objectMap = jsonPlanVo.getJsonPlan();
                        if (MapUtils.isNotEmpty(objectMap)) {
                            List<Map<String, Object>> mapList = (List<Map<String, Object>>) objectMap.get("objects");
                            if (CollectionUtils.isNotEmpty(mapList)) {
                                for (Map<String, Object> map : mapList) {
                                    if (map.containsKey("objects") && list.contains(map.get("uuid"))) {
                                        List<Map<String, Object>> childList = (List<Map<String, Object>>) map.get("objects");
                                        for (Map<String, Object> child : childList) {
                                            if (MapUtils.isNotEmpty(child) && "rect".equals(child.get("type"))) {
                                                child.replace("fill", "#fe8200");
                                            }
                                        }
                                        map.replace("isUse", true);
                                    }
                                }
                            }
                        }
                        Query updateQuery = new Query();
                        updateQuery.addCriteria(Criteria.where("buildId").is(jsonPlanVo.getBuildId())
                                .and("floorName").is(jsonPlanVo.getFloorName()));

                        Update update = new Update();
                        update.set("jsonPlan", objectMap);
                        bulkOperations.updateMulti(updateQuery, update);
                    }
                    bulkOperations.execute();
                }
            }
        } catch (Exception ex) {
            log.error("updateJsonPlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 新增租赁关系
     *
     * @param housingUseList
     * @param rentId
     */
    private void addRentRelationship(List<HousingUse> housingUseList, Long rentId, Integer
            state, List<Long> soiList) {
        try {
            UpdateWrapper<HousingUse> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("is_use", 0);
            updateWrapper.eq("is_use", 1);
            if (state.equals(1)) {
                updateWrapper.in("house_id", housingUseList.stream().map(p -> p.getHouseId()).collect(Collectors.toList()));
            } else if (state.equals(2)) {
                updateWrapper.isNull("house_id");
                updateWrapper.in("yard_id", soiList);
            }
            HousingUse updateHousingUse = new HousingUse();
            housingUseMapper.update(updateHousingUse, updateWrapper);
            //新增院落类型的租赁关系
            if (state.equals(1)) {
                for (HousingUse housingUse : housingUseList) {
                    //获取houseId
                    String houseId = housingUse.getHouseId();
                    UpdateWrapper<LeadingOffice> wrapper = new UpdateWrapper<>();
                    wrapper.set("is_delete", 1);
                    wrapper.eq("house_id", houseId);
                    //修改是否删除状态
                    leadingOfficeMapper.update(new LeadingOffice(), wrapper);

                    housingUse.setIsUse(1);
                    //新增新的数据
                    housingUseMapper.insert(housingUse);
                    //获取新增后的housinguseId
                    Long housinguseId = housingUse.getHousinguseId();
                    RentRelationship rentRelationship = new RentRelationship();
                    rentRelationship.setHousinguseId(housinguseId);
                    rentRelationship.setRentId(rentId);
                    //新增租赁关系表
                    rentRelationshipMapper.insert(rentRelationship);
                }
            }
            //新增土地类型的租赁关系
            else if (state.equals(2)) {
                for (Long soiId : soiList) {
                    HousingUse housingUse = new HousingUse();
                    housingUse.setIsUse(1);
                    housingUse.setYardId(soiId);
                    //新增新的数据
                    housingUseMapper.insert(housingUse);
                    //获取新增后的housinguseId
                    Long housinguseId = housingUse.getHousinguseId();
                    RentRelationship rentRelationship = new RentRelationship();
                    rentRelationship.setHousinguseId(housinguseId);
                    rentRelationship.setRentId(rentId);
                    //新增租赁关系表
                    rentRelationshipMapper.insert(rentRelationship);
                }
            }
        } catch (Exception ex) {
            log.error("addRentRelationshipError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 租赁详情
     *
     * @param rentId
     * @return
     */
    @Override
    public RentDetailDto selectRentDetail(Long rentId) {
        try {
            RentDetailDto rentDetailDto = rentMapper.rentDetail(rentId);
            return rentDetailDto;
        } catch (Exception ex) {
            log.error("selectRentDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除租赁信息
     *
     * @param rentId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRent(Long rentId) {
        try {
            //删除租赁信息
            rentMapper.deleteById(rentId);
            List<Enclosure> enclosureList = enclosureMapper.selectList(new QueryWrapper<Enclosure>().eq("rent_id", rentId));
            //删除服务器上对应的租赁照片
            if (CollectionUtils.isNotEmpty(enclosureList)) {
                for (Enclosure enclosure : enclosureList) {
                    rentService.deleteEnclosure(enclosure);
                }
            }
            //删除数据库租赁附件信息
            enclosureMapper.delete(new QueryWrapper<Enclosure>().eq("rent_id", rentId));
            //删除楼座关系信息
            List<RentRelationship> rentRelationships = rentRelationshipMapper.selectList(new QueryWrapper<RentRelationship>().eq("rent_id", rentId));
            for (RentRelationship rentRelationship : rentRelationships) {
                Long housinguseId = rentRelationship.getHousinguseId();
                housingUseMapper.deleteById(housinguseId);
            }
            //删除租赁关系
            rentRelationshipMapper.delete(new QueryWrapper<RentRelationship>().eq("rent_id", rentId));
            return true;
        } catch (Exception ex) {
            log.error("deleteRentError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 房屋租赁信息列表
     *
     * @param rentPageVo
     * @return
     */
    @Override
    public Page<RentListDto> getlist(RentPageVo rentPageVo) {
        Page<RentListDto> page = new Page<>(rentPageVo.getCurrentPage(), rentPageVo.getPageSize());
        return rentMapper.getList(page, rentPageVo);
    }

    /**
     * 查询租赁附件Url
     *
     * @param enclosureIdList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DownLoadDto> selectEnclosureUrl(List<Long> enclosureIdList) {
        try {
            //查询要下载的租赁附件
            List<Enclosure> enclosures = enclosureMapper.selectBatchIds(enclosureIdList);
            //找到对应的url，循环添加到数组
            List<DownLoadDto> enclosureUrlList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(enclosures)) {
                for (Enclosure enclosure : enclosures) {
                    String enclosurePhysicsUrl = enclosure.getEnclosurePhysicsUrl();
                    if (enclosures.size() > 1) {
                        DownLoadDto downLoadDto = new DownLoadDto();
                        downLoadDto.setPath("file:///" + enclosurePhysicsUrl);
                        enclosureUrlList.add(downLoadDto);
                    } else {
                        DownLoadDto downLoadDto = new DownLoadDto();
                        String str = enclosure.getEnclosureName();
                        downLoadDto.setDownLoadName(str);
                        downLoadDto.setPath(enclosurePhysicsUrl);
                        enclosureUrlList.add(downLoadDto);

                    }
                }
            }
            return enclosureUrlList;
        } catch (Exception ex) {
            log.error("selectEnclosureUrlError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 删除公共服务器上的租赁信息图片方法
     *
     * @param enclosure
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteEnclosure(Enclosure enclosure) throws Exception {
        try {
            enclosureMapper.deleteById(enclosure);
            String enclosureUrl = enclosure.getEnclosurePhysicsUrl();
            // 图片的文件路径
            String imagePath = enclosureUrl;
            if (StringUtils.isNotEmpty(imagePath)) {
                synchronized (this) {
                    Files.deleteIfExists(Paths.get(imagePath));
                }
            }
        } catch (Exception ex) {
            log.error("deleteEnclosureError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 退租
     *
     * @param rentId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int rentWithdrawal(Long rentId) {
        try {
            List<String> houseIdList = new ArrayList<>();
            RentDetailDto rentDetailDto = this.selectRentDetail(rentId);
            //获取退租的房间编号集合
            if (rentDetailDto != null) {
                List<YardRentDto> yardList = rentDetailDto.getYardList();
                if (CollectionUtils.isNotEmpty(yardList)) {
                    for (YardRentDto yardRentDto : yardList) {
                        List<BuildRentDto> buildList = yardRentDto.getBuildList();
                        if (CollectionUtils.isNotEmpty(buildList)) {
                            for (BuildRentDto buildRentDto : buildList) {
                                Long buildId = buildRentDto.getBuildId();
                                //删除redis中楼栋asser信息
                                redisTemplate.delete("build:" + buildId + ":assert");
                                List<FloorRentDto> floorNameList = buildRentDto.getFloorNameList();
                                if (CollectionUtils.isNotEmpty(floorNameList)) {
                                    for (FloorRentDto floorRentDto : floorNameList) {
                                        List<HouseRentDto> houseNameList = floorRentDto.getHouseNameList();
                                        if (CollectionUtils.isNotEmpty(houseNameList)) {
                                            for (HouseRentDto houseRentDto : houseNameList) {
                                                String houseId = houseRentDto.getHouseId();
                                                houseIdList.add(houseId);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            updateWithdrawalJsonPlan(houseIdList);
            return rentMapper.rentWithdrawal(rentId);
        } catch (Exception ex) {
            log.error("rent withdrawal error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 退租时修改图形颜色,将出租改为闲置
     *
     * @param houseIdList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWithdrawalJsonPlan(List<String> houseIdList) {
        try {
            if (CollectionUtils.isEmpty(houseIdList)) {
                return;
            }
            //查询房间颜色对象集合
            List<HouseColorDto> houseColorDtoList = rentMapper.selectHouseList(houseIdList);
            if (CollectionUtils.isNotEmpty(houseColorDtoList)) {
                for (HouseColorDto houseColorDto : houseColorDtoList) {
                    Query query = new Query();
                    query.addCriteria(Criteria.where("jsonPlan.objects.uuid").in(houseColorDto.getHouseId())
                            .andOperator(Criteria.where("jsonPlan.objects").elemMatch(Criteria.where("objects.type").is("rect"))));
                    List<JsonPlanVo> jsonPlanVoList = mongoTemplate.find(query, JsonPlanVo.class);
                    BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, JsonPlanVo.class);
                    if (CollectionUtils.isNotEmpty(jsonPlanVoList)) {
                        for (JsonPlanVo jsonPlanVo : jsonPlanVoList) {
                            Map<String, Object> objectMap = jsonPlanVo.getJsonPlan();
                            if (MapUtils.isNotEmpty(objectMap)) {
                                List<Map<String, Object>> mapList = (List<Map<String, Object>>) objectMap.get("objects");
                                if (CollectionUtils.isNotEmpty(mapList)) {
                                    for (Map<String, Object> map : mapList) {
                                        if (map.containsKey("objects") && houseColorDtoList.stream().anyMatch(p -> p.getHouseId().equals(map.get("uuid")))) {
                                            List<Map<String, Object>> childList = (List<Map<String, Object>>) map.get("objects");
                                            for (Map<String, Object> child : childList) {
                                                if (MapUtils.isNotEmpty(child) && "rect".equals(child.get("type"))) {
                                                    child.replace("fill", houseColorDto.getColorType());
                                                }
                                            }
                                            map.replace("isUse", false);
                                        }
                                    }
                                }
                            }
                            Query updateQuery = new Query();
                            updateQuery.addCriteria(Criteria.where("buildId").is(jsonPlanVo.getBuildId())
                                    .and("floorName").is(jsonPlanVo.getFloorName()));

                            Update update = new Update();
                            update.set("jsonPlan", objectMap);
                            bulkOperations.updateMulti(updateQuery, update);
                        }
                        bulkOperations.execute();
                    }
                }
            }
        } catch (Exception ex) {
            log.error("updateWithdrawalJsonPlanError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 查询出租租金提醒数量
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer rentRemindNum() {
        try {
            Map<String, String> dateMap = this.getDate();
            String rentRemindDay = dateMap.get("rentRemindDay");
            return rentMapper.rentRemindNum(rentRemindDay);
        } catch (Exception ex) {
            log.error("rent rentRemindNum error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询提醒数量
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<Object, Object> remindNum() {
        HashMap<Object, Object> hashMap = new HashMap<>();
        Integer rentCount = this.rentRemindNum();
        Integer rentExpireCount = this.rentExpireNum();
        Integer repairCount = repairRemindService.repairRemindNum();
        Integer contractExpireCount = contractService.contractExpireAmount();
        Integer contractMoneyCount = contractService.contractMoneyAmount();
        hashMap.put("rentCount", rentCount);
        hashMap.put("repairCount", repairCount);
        hashMap.put("rentExpireCount", rentExpireCount);
        hashMap.put("contractExpireCount", contractExpireCount);
        hashMap.put("contractMoneyCount", contractMoneyCount);
        return hashMap;
    }

    /**
     * 计算出租租金提醒时间区间
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> getDate() {
        RepairDic repairDic = repairDicMapper.selectOne(new QueryWrapper<RepairDic>().eq("repair_dic_key", "rent_dic_days"));
        String rentRemindDay = repairDic.getRepairDicValue();
        Map<String, String> map = new HashMap<>();
        map.put("rentRemindDay", rentRemindDay);
        return map;
    }

    /**
     * 出租租金提醒列表
     *
     * @param rentPageVo
     * @return
     */
    @Override
    public Page<RentListDto> rentRmindList(RentPageVo rentPageVo) {
        try {
            Page<RentListDto> page = new Page<>(rentPageVo.getCurrentPage(), rentPageVo.getPageSize());
            Map<String, String> dateMap = this.getDate();
            String rentRemindDay = dateMap.get("rentRemindDay");
            return rentMapper.rentRmindList(page, rentPageVo, rentRemindDay);
        } catch (Exception ex) {
            log.error("rentRmindListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 计算出租到期提醒时间区间
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, String> getExpireDate() {
        RepairDic repairDic = repairDicMapper.selectOne(new QueryWrapper<RepairDic>().eq("repair_dic_key", "rent_expire_days"));
        String rentExpireDay = repairDic.getRepairDicValue();
        Map<String, String> map = new HashMap<>();
        map.put("rentExpireDay", rentExpireDay);
        return map;
    }

    /**
     * 出租到期提醒列表
     *
     * @param rentPageVo
     * @return
     */
    @Override
    public Page<RentListDto> rentExpireList(RentPageVo rentPageVo) {
        try {
            Page<RentListDto> page = new Page<>(rentPageVo.getCurrentPage(), rentPageVo.getPageSize());
            Map<String, String> dateMap = this.getExpireDate();
            String rentExpireDay = dateMap.get("rentExpireDay");
            return rentMapper.rentExpireList(page, rentPageVo, rentExpireDay);
        } catch (Exception ex) {
            log.error("rentExpireListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 查询出租到期提醒数量
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer rentExpireNum() {
        try {
            Map<String, String> dateMap = this.getExpireDate();
            String rentExpireDay = dateMap.get("rentExpireDay");
            return rentMapper.rentExpireNum(rentExpireDay);
        } catch (Exception ex) {
            log.error("rentExpireNumError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


}
