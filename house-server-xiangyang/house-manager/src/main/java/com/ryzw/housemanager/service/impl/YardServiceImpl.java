package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.*;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.*;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IBuildService;
import com.ryzw.housemanager.service.IHouseTypeService;
import com.ryzw.housemanager.service.IYardService;
import com.ryzw.housemanager.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>
 * 院落信息表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
@Service
@Slf4j
public class YardServiceImpl extends ServiceImpl<YardMapper, Yard> implements IYardService {
    @Autowired
    private YardMapper yardMapper;
    @Autowired
    private BuildMapper buildMapper;
    @Autowired
    private RepairRemindMapper repairRemindMapper;
    @Autowired
    private YardEstateMapper yardEstateMapper;
    @Autowired
    private ServiceContentMapper serviceContentMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private HandleModuleMapper handleModuleMapper;
    @Autowired
    private IBuildService buildService;
    @Autowired
    private HousingUseMapper housingUseMapper;

    @Autowired
    private IAuthorityUnitService authorityUnitService;

    @Autowired
    private HouseTypeMapper houseTypeMapper;

    @Autowired
    private IHouseTypeService houseTypeService;

    /**
     * 查询院落列表
     *
     * @param yard
     * @return
     */
    @Override
    public Page<Yard> list(YardVo yard) {
        Page<Yard> page = new Page<>(yard.getCurrentPage(), yard.getPageSize());
        return yardMapper.findInfoById(page, yard);
    }


    /**
     * 查询单位集合中的院落
     *
     * @param unitIds
     * @param searchYardId
     * @return
     */
    @Override
    public List<YardResultVo> getAllByUnitID(List<Long> unitIds, Long searchYardId) {
        return yardMapper.getAllByUnitID(unitIds, searchYardId);
    }

    /**
     * 查询人工分房的院落信息
     *
     * @param unitIds
     * @param searchYardId
     * @return
     */
    @Override
    public List<YardResultVo> getYardHandle(List<Long> unitIds, Long searchYardId) {
        return yardMapper.getAllByUnitID(unitIds, searchYardId);
    }

    /**
     * 查询单位集合中的院落
     *
     * @param unitIds
     * @return
     */
    @Override
    public List<YardResultVo> getAllByUnitID(List<Long> unitIds) {
        return yardMapper.getYardByUnitID(unitIds);
    }

    /**
     * 查询所有院落
     *
     * @return
     */
    @Override
    public List<YardResultVo> getAll(Long searchId, List<Long> unitIds) {
        return yardMapper.getAll(searchId, unitIds);
    }

    /**
     * 删除院落
     *
     * @param yardId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteYard(Long yardId) {
        try {
            Integer count = buildMapper.selectCount(new QueryWrapper<Build>().eq("yard_id", yardId));
            if (count > 0) {
                throw new ServiceException("院落下面挂有楼栋信息，无法删除");
            }
            int remindCount = repairRemindMapper.selectCount(new QueryWrapper<RepairRemind>().eq("yard_id", yardId)
                    .gt("remaind_time", new Date()));
            if (remindCount > 0) {
                throw new ServiceException("院落有未到期维修提醒，无法删除！");
            }
            Integer handleYardCount = this.isHaveRent(yardId);
            if (handleYardCount > 0) {
                throw new ServiceException("该院落(土地)下有出租出借,无法删除！");
            }
            int i = yardMapper.deleteById(yardId);
            if (i == 0) {
                throw new ServiceException("没有数据可以删除");
            }
            List<YardEstate> estateList = yardEstateMapper.selectList(new QueryWrapper<YardEstate>().eq("yard_id", yardId));
            if (CollectionUtils.isNotEmpty(estateList)) {
                for (YardEstate yardEstate : estateList) {
                    Long yardEstateId = yardEstate.getYardEstateId();
                    //删除物业服务内容关系
                    serviceContentMapper.delete(new QueryWrapper<ServiceContent>().eq("yard_estate_id", yardEstateId));
                }
            }
            //删除院落物业关系
            yardEstateMapper.delete(new QueryWrapper<YardEstate>().eq("yard_id", yardId));
            return true;
        } catch (Exception ex) {
            log.error("deleteYardError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询单位下面的院落信息
     *
     * @param unitTreeVo
     * @return
     */
    @Override
    public List<UnitYardDto> unitYard(UnitTreeVo unitTreeVo) {
        return yardMapper.unitYard(unitTreeVo);
    }


    /**
     * 查询院落关联的楼座
     *
     * @param basicVo
     * @return
     */
    @Override
    public YardBuildDto yardBuilds(BasicVo<Long> basicVo) {
        return yardMapper.yardBuilds(basicVo);
    }

    /**
     * 查询用户权限下的院落位置信息
     *
     * @param unitIds
     * @return
     */
    @Override
    public List<YardPositionDto> yardPosition(List<Long> unitIds) {
        return yardMapper.yardPosition(unitIds);
    }

    /**
     * 添加院落信息
     *
     * @param yardAddVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addYard(YardAddVo yardAddVo) {
        try {
            //新增院落信息
            yardMapper.insert(yardAddVo.getYard());
            Long yardId = yardAddVo.getYard().getYardId();
            //获取院落物业关系集合
            List<YardAndestateVo> yardAndestateVoList = yardAddVo.getYardAndestateVoList();
            if (CollectionUtils.isNotEmpty(yardAndestateVoList)) {
                for (YardAndestateVo yardAndestateVo : yardAndestateVoList) {
                    YardEstate yardEstate = new YardEstate();
                    yardEstate.setYardId(yardId);
                    yardEstate.setEstateId(yardAndestateVo.getEstateId());
                    yardEstate.setStartTime(yardAndestateVo.getStartTime());
                    yardEstate.setEndTime(yardAndestateVo.getEndTime());
                    yardEstate.setContractAmount(yardAndestateVo.getContractAmount());
                    yardEstate.setMoneyFrom(yardAndestateVo.getMoneyFrom());
                    //新增院落物业表
                    yardEstateMapper.insert(yardEstate);
                    //获取新增后的yardEstateId
                    Long yardEstateId = yardEstate.getYardEstateId();
                    ServiceContent content = new ServiceContent();
                    content.setYardEstateId(yardEstateId);
                    List<Long> serviceContentList = yardAndestateVo.getServiceContentList();
                    if (CollectionUtils.isNotEmpty(serviceContentList)) {
                        for (Long serviceContent : serviceContentList) {
                            content.setServiceContent(serviceContent);
                            //新增服务关系
                            serviceContentMapper.insert(content);
                        }
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("addYardError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 添加院落信息
     *
     * @param yardAddVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Long addConfigureYard(YardAddVo yardAddVo) {
        try {
            //新增院落信息
            if (yardAddVo.getYard() != null) {
                if (StringUtils.isNotBlank(yardAddVo.getYard().getYardName())) {
                    Integer yardCount = yardMapper.selectCount(new QueryWrapper<Yard>().eq("yard_name", yardAddVo.getYard().getYardName()));
                    if (yardCount > 0) {
                        throw new ServiceException("院落名称不能重复！");
                    }
                }
            } else {
                throw new ServiceException("院落信息不能为空！");
            }

            yardMapper.insert(yardAddVo.getYard());
            Long yardId = yardAddVo.getYard().getYardId();
            //获取院落物业关系集合
            List<YardAndestateVo> yardAndestateVoList = yardAddVo.getYardAndestateVoList();
            if (CollectionUtils.isNotEmpty(yardAndestateVoList)) {
                for (YardAndestateVo yardAndestateVo : yardAndestateVoList) {
                    YardEstate yardEstate = new YardEstate();
                    yardEstate.setYardId(yardId);
                    yardEstate.setEstateId(yardAndestateVo.getEstateId());
                    yardEstate.setStartTime(yardAndestateVo.getStartTime());
                    yardEstate.setEndTime(yardAndestateVo.getEndTime());
                    yardEstate.setContractAmount(yardAndestateVo.getContractAmount());
                    yardEstate.setMoneyFrom(yardAndestateVo.getMoneyFrom());
                    //新增院落物业表
                    yardEstateMapper.insert(yardEstate);
                    //获取新增后的yardEstateId
                    Long yardEstateId = yardEstate.getYardEstateId();
                    ServiceContent content = new ServiceContent();
                    content.setYardEstateId(yardEstateId);
                    List<Long> serviceContentList = yardAndestateVo.getServiceContentList();
                    if (CollectionUtils.isNotEmpty(serviceContentList)) {
                        for (Long serviceContent : serviceContentList) {
                            content.setServiceContent(serviceContent);
                            //新增服务关系
                            serviceContentMapper.insert(content);
                        }
                    }
                }
            }
            return yardId;
        } catch (Exception ex) {
            log.error("addConfigureYardError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改院落信息
     *
     * @param yardAddVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateYard(YardAddVo yardAddVo) {
        try {
            List<YardAndestateVo> yardAndestateVoList = yardAddVo.getYardAndestateVoList();

            Long yardId = yardAddVo.getYard().getYardId();
            if (yardId != null) {
                QueryWrapper<Yard> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("yard_id", yardId);
                Yard yard = this.getOne(queryWrapper);
                Integer regionId = yard.getRegionId();

                //修改院落表
                yardMapper.updateById(yardAddVo.getYard());

                if (regionId != yardAddVo.getYard().getRegionId()) {
                    QueryWrapper<Build> wrapper = new QueryWrapper<>();
                    wrapper.eq("yard_id", yardAddVo.getYard().getYardId());
                    List<Build> buildList = buildService.list(wrapper);
                    if (CollectionUtils.isNotEmpty(buildList)) {
                        for (Build build : buildList) {
                            Long buildId = build.getBuildId();
                            String detailKey = "build:" + buildId + ":detail";
                            if (redisTemplate.hasKey(detailKey)) {
                                redisTemplate.delete(detailKey);
                            }
                        }
                    }
                }

            }

            //删除院落物业关系以及服务关系
            if (CollectionUtils.isNotEmpty(yardAddVo.getDelYardEstateIdList())) {
                for (Long yardEstateId : yardAddVo.getDelYardEstateIdList()) {
                    yardEstateMapper.deleteById(yardEstateId);
                    serviceContentMapper.delete(new QueryWrapper<ServiceContent>().eq("yard_estate_id", yardEstateId));
                }
            }

            if (CollectionUtils.isNotEmpty(yardAndestateVoList)) {
                ServiceContent content = new ServiceContent();
                for (YardAndestateVo yardAndestateVo : yardAndestateVoList) {
                    Long yardEstateId = yardAndestateVo.getYardEstateId();
                    //如果之前有关联过物业信息就进行修改
                    if (yardEstateId != null) {
                        //先删除原有数据
                        serviceContentMapper.delete(new QueryWrapper<ServiceContent>().eq("yard_estate_id", yardEstateId));
                        content.setYardEstateId(yardEstateId);
                        YardEstate updateEstate = new YardEstate();
                        updateEstate.setYardEstateId(yardEstateId);
                        updateEstate.setEstateId(yardAndestateVo.getEstateId());
                        updateEstate.setStartTime(yardAndestateVo.getStartTime());
                        updateEstate.setEndTime(yardAndestateVo.getEndTime());
                        updateEstate.setContractAmount(yardAndestateVo.getContractAmount());
                        updateEstate.setMoneyFrom(yardAndestateVo.getMoneyFrom());
                        //修改院落物业表
                        yardEstateMapper.updateById(updateEstate);
                    } else {
                        content.setYardEstateId(yardEstateId);
                        YardEstate updateEstate = new YardEstate();
                        updateEstate.setEstateId(yardAndestateVo.getEstateId());
                        updateEstate.setYardId(yardAddVo.getYard().getYardId());
                        updateEstate.setStartTime(yardAndestateVo.getStartTime());
                        updateEstate.setEndTime(yardAndestateVo.getEndTime());
                        updateEstate.setContractAmount(yardAndestateVo.getContractAmount());
                        updateEstate.setMoneyFrom(yardAndestateVo.getMoneyFrom());
                        //新增院落物业表
                        yardEstateMapper.insert(updateEstate);
                        content.setYardEstateId(updateEstate.getYardEstateId());
                    }
                    List<Long> serviceContentList = yardAndestateVo.getServiceContentList();
                    if (CollectionUtils.isNotEmpty(serviceContentList)) {
                        for (Long serviceContent : serviceContentList) {
                            content.setServiceContent(serviceContent);
                            //新增服务关系
                            serviceContentMapper.insert(content);
                        }
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("updateYardError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 根据院落编号查询物业关系
     *
     * @param yardId
     * @return
     */
    @Override
    public YardEstateDto selectEstate(Long yardId) {
        try {
            return yardMapper.selectEstate(yardId);
        } catch (Exception ex) {
            log.error("selectEstateError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询院落详情
     *
     * @param basicVo
     * @return
     */
    @Override
    public YardDetailDto yardDetail(BasicVo<Long> basicVo) {
        return yardMapper.yardDetail(basicVo);
    }

    /**
     * 判断院落/土地下是否有出租出借
     *
     * @param yardId
     * @return
     */
    @Override
    public Integer isHaveRent(Long yardId) {
        try {
            return yardMapper.isHaveRent(yardId);
        } catch (Exception ex) {
            log.error("selectEstateError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 市（州、盟）办公用房报表信息查询
     *
     * @return
     */
    @Override
    public List<YardUnitProfessionalDto> yardUnitReport() {
        try {
            List<YardUnitProfessionalDto> list = new ArrayList<>();
            //获取用户可以查看的单位信息
            List<Long> unitIds = authorityUnitService.getUnitIdByToken();

            // 获取院落的信息
            List<YardOneDto> yardOneList = yardMapper.yardOne(unitIds);
            //房屋类型面积查询
            List<YardTypeTwoDto> yardTypeList = yardMapper.yardTypeTwo(unitIds);
            /**
             * 查询院落楼座建成时间
             */
            List<YardBuildDateDto> yardBuildDateDtoList = yardMapper.selectYardBuildDate(unitIds);

            //查询单位信息
            List<YardUnitDto> unitList = yardMapper.unitRankList(unitIds);

            Integer id = 1;

            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            for (YardOneDto yardOne : yardOneList) {
                YardUnitProfessionalDto yardUnitProfessionalDto = new YardUnitProfessionalDto();
                Long yardId = yardOne.getYardId();
                yardUnitProfessionalDto.setPosition(yardOne.getYardPosition());
                yardUnitProfessionalDto.setSiteArea(yardOne.getSiteArea());
                yardUnitProfessionalDto.setTotalCoveredArea(yardOne.getTotalCoveredArea());
                yardUnitProfessionalDto.setId(id);

                List<YardTypeTwoDto> yardTypeTwo = yardTypeList.stream().filter(p -> p.getYardId().equals(yardId)).collect(Collectors.toList());


                //查询办公用房类型的面积
                List<CommonHouseTypeDto> houseByBanGong = houseTypeMapper.areaByTypeAndYard(unitIds, 1,yardId);
                BigDecimal officeCoveredArea = this.commonHouseType(houseByBanGong);
                yardUnitProfessionalDto.setOfficeCoveredArea(officeCoveredArea.doubleValue());

                //查询技术业务用房类型的面积
                List<CommonHouseTypeDto> houseByYeWu = houseTypeMapper.areaByTypeAndYard(unitIds, 5,yardId);
                BigDecimal technologyCoveredArea = this.commonHouseType(houseByYeWu);
                yardUnitProfessionalDto.setTechnologyCoveredArea(technologyCoveredArea.doubleValue());


                BigDecimal officeUseArea = yardTypeTwo.stream().filter(p -> p.getHouseTypeId().equals(1))
                        .map(p -> new BigDecimal(new Double(p.getTotalUseArea()).toString()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal serviceUseArea = yardTypeTwo.stream().filter(p -> p.getHouseTypeId().equals(2))
                        .map(p -> BigDecimal.valueOf(p.getTotalUseArea()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal equipmentUseArea = yardTypeTwo.stream().filter(p -> p.getHouseTypeId().equals(3))
                        .map(p -> BigDecimal.valueOf(p.getTotalUseArea()))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);


                BigDecimal totleUseArea = new BigDecimal(0.0);
                totleUseArea = totleUseArea.add(officeUseArea).add(serviceUseArea).add(equipmentUseArea);
                yardUnitProfessionalDto.setOfficeUseArea(officeUseArea.doubleValue());
                yardUnitProfessionalDto.setServiceUseArea(serviceUseArea.doubleValue());
                yardUnitProfessionalDto.setEquipmentUseArea(equipmentUseArea.doubleValue());
                yardUnitProfessionalDto.setTotalUseArea(totleUseArea.doubleValue());

                //查询附属用房类型的面积
                List<CommonHouseTypeDto> houseByFuShu = houseTypeMapper.areaByTypeAndYard(unitIds, 4,yardId);
                BigDecimal affiliatedCoveredArea = this.commonHouseType(houseByFuShu);
                yardUnitProfessionalDto.setAffiliatedCoveredArea(affiliatedCoveredArea.doubleValue());


                Optional<YardBuildDateDto> optionalYardBuildDateDto = yardBuildDateDtoList.stream()
                        .filter(p -> p.getYardId().equals(yardId) && p.getBuildDate() != null)
                        .min((x, y) -> x.getBuildDate().compareTo(y.getBuildDate()));
                if (optionalYardBuildDateDto.isPresent()) {
                    YardBuildDateDto yardBuildDateDto = optionalYardBuildDateDto.get();
                    yardUnitProfessionalDto.setBuildingAge(format.format(yardBuildDateDto.getBuildDate()));
                    yardUnitProfessionalDto.setRemark(yardBuildDateDto.getRemark());
                    yardUnitProfessionalDto.setRegistration(yardBuildDateDto.getRegistration());
                    yardUnitProfessionalDto.setIsRent(yardBuildDateDto.getIsRent().equals(1) ? "是" : "否");

                }


                List<YardUnitDto> yardUnitDtos = unitList.stream().filter(p -> p.getYardId().equals(yardId)).collect(Collectors.toList());
                List<UseUnitAndSituationDto> units = new ArrayList<>();
                Map<UnitGroup, List<YardUnitDto>> yardUnitGroups = yardUnitDtos.stream().collect(Collectors.groupingBy(p -> new UnitGroup(p.getUnitId(), p.getUnitName(), p.getUnitCategory())));

                for (Map.Entry<UnitGroup, List<YardUnitDto>> entry : yardUnitGroups.entrySet()) {
                    UseUnitAndSituationDto unitAndSituationDto = new UseUnitAndSituationDto();
                    unitAndSituationDto.setUnitName(entry.getKey().getUnitName());
                    unitAndSituationDto.setUnitType(entry.getKey().getUnitCategory());


                    //市级正职
                    List<YardUnitDto> cityDtoList = entry.getValue().stream().filter(p -> p.getYardId().equals(yardId) && p.getUnitId().equals(entry.getKey().getUnitId()) && StringUtils.isNotBlank(p.getProfessionalName()) && p.getProfessionalName().contains("市级正职")).collect(Collectors.toList());
                    int cityOfficialStaffNum = CollectionUtils.isNotEmpty(cityDtoList) ? cityDtoList.stream().map(YardUnitDto::getPersonNum).flatMapToInt(IntStream::of).sum() : 0;
                    unitAndSituationDto.setCityOfficialStaffNum(cityOfficialStaffNum);

                    //市级副职
                    List<YardUnitDto> cityDeputyDtos = entry.getValue().stream().filter(p -> p.getYardId().equals(yardId) && p.getUnitId().equals(entry.getKey().getUnitId()) && StringUtils.isNotBlank(p.getProfessionalName()) && p.getProfessionalName().contains("市级副职")).collect(Collectors.toList());
                    int cityDeputyStaffNum = CollectionUtils.isNotEmpty(cityDeputyDtos) ? cityDeputyDtos.stream().map(YardUnitDto::getPersonNum).flatMapToInt(IntStream::of).sum() : 0;
                    unitAndSituationDto.setCityDeputyStaffNum(cityDeputyStaffNum);

                    //正局（处）级
                    List<YardUnitDto> bureauOfficialStaffList = entry.getValue().stream().filter(p -> p.getYardId().equals(yardId) && p.getUnitId().equals(entry.getKey().getUnitId()) && StringUtils.isNotBlank(p.getProfessionalName()) && p.getProfessionalName().contains("正局（处）级")).collect(Collectors.toList());
                    int bureauOfficialStaffNum = CollectionUtils.isNotEmpty(bureauOfficialStaffList) ? bureauOfficialStaffList.stream().map(YardUnitDto::getPersonNum).flatMapToInt(IntStream::of).sum() : 0;
                    unitAndSituationDto.setBureauOfficialStaffNum(bureauOfficialStaffNum);


                    //副局（处）级
                    List<YardUnitDto> bureauDeputyStaffList = entry.getValue().stream().filter(p -> p.getYardId().equals(yardId) && p.getUnitId().equals(entry.getKey().getUnitId()) && StringUtils.isNotBlank(p.getProfessionalName()) && p.getProfessionalName().contains("副局（处）级")).collect(Collectors.toList());
                    int bureauDeputyStaffNum = CollectionUtils.isNotEmpty(bureauDeputyStaffList) ? bureauDeputyStaffList.stream().map(YardUnitDto::getPersonNum).flatMapToInt(IntStream::of).sum() : 0;
                    unitAndSituationDto.setBureauDeputyStaffNum(bureauDeputyStaffNum);

 /*                   //正处级
                    List<YardUnitDto> directorList = entry.getValue().stream().filter(p -> p.getYardId().equals(yardId) && p.getUnitId().equals(entry.getKey().getUnitId()) && StringUtils.isNotBlank(p.getProfessionalName()) && p.getProfessionalName().contains("正处级")).collect(Collectors.toList());
                    int directorNum = CollectionUtils.isNotEmpty(directorList) ? directorList.stream().map(YardUnitDto::getPersonNum).flatMapToInt(IntStream::of).sum() : 0;
                    unitAndSituationDto.setDirectorNum(directorNum);

                    //副处级
                    List<YardUnitDto> deputyDirectorList = entry.getValue().stream().filter(p -> p.getYardId().equals(yardId) && p.getUnitId().equals(entry.getKey().getUnitId()) && StringUtils.isNotBlank(p.getProfessionalName()) && p.getProfessionalName().contains("副处级")).collect(Collectors.toList());
                    int deputyDirectorNum = CollectionUtils.isNotEmpty(deputyDirectorList) ? deputyDirectorList.stream().map(YardUnitDto::getPersonNum).flatMapToInt(IntStream::of).sum() : 0;
                    unitAndSituationDto.setDeputyDirectorNum(deputyDirectorNum);

                    //处级以下
                    List<YardUnitDto> totleList = entry.getValue().stream().filter(p -> p.getYardId().equals(yardId) && p.getUnitId().equals(entry.getKey().getUnitId())).collect(Collectors.toList());
                    int total = 0;
                    int underDirectorNum = 0;
                    if(CollectionUtils.isNotEmpty(totleList)){
                        for(YardUnitDto yardUnitDto :totleList){
                            if(yardUnitDto.getPersonNum() !=null){
                                total += yardUnitDto.getPersonNum();
                            }
                        }
                    }
                    if(total !=0){
                        underDirectorNum = total - cityOfficialStaffNum - cityDeputyStaffNum - bureauOfficialStaffNum - bureauDeputyStaffNum - directorNum - deputyDirectorNum;
                    }*/

                    //局（处）级以下
                    List<YardUnitDto> underDirectorList = entry.getValue().stream().filter(p -> p.getYardId().equals(yardId) && p.getUnitId().equals(entry.getKey().getUnitId()) && StringUtils.isNotBlank(p.getProfessionalName()) && p.getProfessionalName().contains("局（处）级以下")).collect(Collectors.toList());
                    int underDirectorNum = CollectionUtils.isNotEmpty(underDirectorList) ? underDirectorList.stream().map(YardUnitDto::getPersonNum).flatMapToInt(IntStream::of).sum() : 0;

                    unitAndSituationDto.setUnderDirectorNum(underDirectorNum);
                    units.add(unitAndSituationDto);
                }
                yardUnitProfessionalDto.setUseUnitAndSituation(units);
                list.add(yardUnitProfessionalDto);

                id++;
            }
            return list;
        }catch (Exception ex){
            log.error("yardUnitReportError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 查询各种类型用房建筑面积的公共方法
     *
     * @param commonHouseType
     */
    public BigDecimal commonHouseType(List<CommonHouseTypeDto> commonHouseType) {
        try {
            BigDecimal comHouseBuildArea = new BigDecimal("0");
            if (CollectionUtils.isNotEmpty(commonHouseType)) {
                for (CommonHouseTypeDto commonHouseTypeDto : commonHouseType) {
                    //获取楼栋Id
                    Long buildId = commonHouseTypeDto.getBuildId();
                    //获取该楼栋的系数
                    BuildCoeDto buildCoeDto = (BuildCoeDto) houseTypeService.selectOneBuildCoe(buildId);
                    //获取用房总使用面积
                    BigDecimal houseUseAreaSum = commonHouseTypeDto.getHouseUseAreaSum();
                    //计算该楼栋下用房的建筑面积  保留两位四舍五入
                    BigDecimal bigDecimal = houseUseAreaSum.divide(new BigDecimal(String.valueOf(buildCoeDto.getBuildCoe())), 2, BigDecimal.ROUND_HALF_UP);
                    //计算用房建筑面积总和
                    comHouseBuildArea = comHouseBuildArea.add(bigDecimal);
                }
            }
            return comHouseBuildArea;
        } catch (Exception ex) {
            log.error("CommonHouseTypeError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}
