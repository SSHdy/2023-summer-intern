package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.RepairDic;
import com.ryzw.housemanager.entity.Unit;
import com.ryzw.housemanager.entity.UnitProfessional;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.*;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IUnitService;
import com.ryzw.housemanager.service.IUseUnitService;
import com.ryzw.housemanager.util.ExcelUtil;
import com.ryzw.housemanager.vo.PageBasicVo;
import com.ryzw.housemanager.vo.PageVo;
import com.ryzw.housemanager.vo.UnitListVo;
import com.ryzw.housemanager.vo.UnitSubordinateVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 单位信息表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */

@Service
@Slf4j
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements IUnitService {
    @Autowired
    private UnitMapper unitMapper;

    @Autowired
    IAuthorityUnitService authorityUnitService;

    @Autowired
    UseUnitMapper useUnitMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    RepairUnitMapper repairUnitMapper;

    @Autowired
    private IUseUnitService useUnitService;

    @Autowired
    private UnitProfessionalMapper unitProfessionalMapper;

    @Autowired
    private HouseTypeServiceImpl houseTypeService;

    @Autowired
    private ProfessionalMapper professionalMapper;

    @Autowired
    private RepairDicMapper repairDicMapper;

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    /**
     * 查询一级单位和它下面的子级单位，并且按照一级单位进行分页，不需要查询条件
     *
     * @return
     */
    @Override
    public Page<UnitChildDto> unitList(PageVo pageVo) {
        try {
            Page page = new Page(pageVo.getCurrentPage(), pageVo.getPageSize());

            //按分页查询一级单位信息
            Page<UnitChildDto> pList = unitMapper.unitListByPage(page);

//        获取所有单位信息
            List<UnitChildDto> unitList = unitMapper.unitList();

            if (CollectionUtils.isNotEmpty(pList.getRecords())) {
                //为一级菜单设置子菜单准备递归
                for (UnitChildDto p : pList.getRecords()) {
//            获取父菜单下的子菜单
                    List<UnitChildDto> childDtoList = getChilds(Long.valueOf(p.getUnitId()), unitList);
                    p.setChildren(childDtoList);
                }
            }
            return pList;
        } catch (Exception ex) {
            log.error("unitListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    public List<UnitChildDto> getChilds(Long id, List<UnitChildDto> unitList) {
        try {
//        构建子菜单
            List<UnitChildDto> children = new ArrayList<>();
            //遍历传入的list
            for (UnitChildDto child : unitList) {
                if (id.equals(child.getUnitPid())) {
                    children.add(child);
                }
            }
//        递归
            for (UnitChildDto m : children) {
                m.setChildren(getChilds(m.getUnitId(), unitList));
            }
            //所有菜单的父id与传入的根节点id比较，若相等则为该级菜单的子菜单
//        退出循环条件
            if (children.size() == 0) {
                return null;
            }
            return children;
        } catch (Exception ex) {
            log.error("getChildsError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 通过id查询单位信息
     *
     * @param id
     * @return
     */
    @Override
    public UnitListDto findById(Long id) {
        UnitListDto unitListDto = unitMapper.findById(id);
        //查询编制信息和等级信息
        List<ProNumAndLevelDto> unitProfessionalList = unitMapper.getNumAndLevel(id);
        if (CollectionUtils.isNotEmpty(unitProfessionalList)) {
            unitListDto.setUnitProfessionalList(unitProfessionalList);
        }
        return unitListDto;
    }

    /**
     * 通过单位名称和类型，分页查询单位信息，如果没有查询条件，则分页显示所有单位，不区分一二级
     *
     * @param unitListVo
     * @return
     */
    @Override
    public Page<UnitListDto> unitAll(UnitListVo unitListVo) {
        Page<UnitListDto> page = new Page<>(unitListVo.getCurrentPage(), unitListVo.getPageSize());
        return unitMapper.unitAll(page, unitListVo);
    }

    /**
     * 通过角色接口传入的ids查找对应的unit单位名称
     *
     * @param ids
     * @return
     */
    @Override
    public List<Map<Object, Object>> findByRoleUnit(List<Long> ids) {
        return unitMapper.findByRoleUnit(ids);
    }

    /**
     * 查询单位树
     *
     * @param unitIds
     * @return
     */
    @Override
    public List<UnitTreeDto> unitTree(List<Long> unitIds) {
        return unitMapper.unitTree(unitIds);
    }

    /**
     * 查询单位树所有节点（无权限）
     *
     * @param unitId
     * @return
     */
    @Override
    public List<AllUnitTreeDto> allUnitTree(Long unitId) {
        return unitMapper.allUnitTree(unitId);
    }

    /**
     * 查询出所有的单位信息
     *
     * @return
     */
    @Override
    public List<AllUnitDto> infinityTree() {
        return unitMapper.infinityTree();
    }

    /**
     * 删除单位
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(Long id) {
        try {

            int roleCount = authorityUnitService.selectCountByUnit(id);
            Integer pidCount = unitMapper.selectCount(new QueryWrapper<Unit>().eq("unit_pid", id));
            if (pidCount > 0) {
                throw new ServiceException("该单位下有二级单位，不能删除!");
            }
            if (roleCount > 0) {
                throw new ServiceException("该单位有角色正在使用，不能删除!");
            }

            int useUnitCount = useUnitMapper.houseUseUnit(id);
            if (useUnitCount > 0) {
                throw new ServiceException("该单位有房间正在使用，不能删除!");
            }

            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("unit_id", id);
            int repairCount = repairUnitMapper.selectCount(queryWrapper);
            if (repairCount > 0) {
                throw new ServiceException("该单位有维修申请正在使用，不能删除!");
            }
            //获取用到该单位的楼栋Id集合
            List<Long> buildIdList = unitMapper.getBuildId(id);
            if (CollectionUtils.isNotEmpty(buildIdList)) {
                for (Long buildId : buildIdList) {
                    //删除redis中assert缓存
                    String detailKey = "build:" + buildId + ":assert";
                    if (redisTemplate.hasKey(detailKey)) {
                        redisTemplate.delete(detailKey);
                    }
                }
            }

            unitMapper.deleteById(id);
            List<Long> userIds = authorityUnitService.getUserIdByUnit(id);
            if (CollectionUtils.isNotEmpty(userIds)) {
                List<String> keys = userIds
                        .stream().map(p -> "user:" + p)
                        .collect(Collectors.toList());
                redisTemplate.delete(keys);
            }
            //删除单位职级关系
            unitProfessionalMapper.delete(new QueryWrapper<UnitProfessional>().eq("unit_id", id));
            return true;
        } catch (Exception ex) {
            log.error("unitSerivce Error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 单位报表信息查询
     *
     * @param unitId
     * @return
     */
    @Override
    public UnitReportInfoDto unitReport(Long unitId) {
        return unitMapper.unitReport(unitId);
    }

    /**
     * 查询房屋类型面积
     *
     * @param unitId
     * @return
     */
    @Override
    public List<TypeAreaDto> typeArea(Long unitId) {
        return unitMapper.typeArea(unitId);
    }

    /**
     * 下属单位信息信息报表查询
     *
     * @param unitSubordinateVo
     * @return
     */
    @Override
    public List<UnitSubordinateDto> unitSubordinate(UnitSubordinateVo unitSubordinateVo) {
        return unitMapper.unitSubordinate(unitSubordinateVo);
    }

    /**
     * 查询重新分配单位下拉列表
     *
     * @param
     * @return
     */
    @Override
    public List<ReapportionUnitListDto> reapportionUnitList() {
        try {
            List<Long> useUnitIds = useUnitService.useUnitIds();

            //获取用户可以查看的单位信息
            List<Long> unitIds = authorityUnitService.getUnitIdByToken();
            unitIds.retainAll(useUnitIds);
            return unitMapper.reapportionUnitList(unitIds);
        } catch (Exception ex) {
            log.error("reapportionUnitListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询单位超标信息
     *
     * @param pageVo
     * @return
     */
    @Override
    public Page<LeaderExcessDto> unitExcess(PageVo pageVo) {
        Page<LeaderExcessDto> page = new Page<>(pageVo.getCurrentPage(), pageVo.getPageSize());
        return unitMapper.unitExcess(page);
    }

    /**
     * 分页查询单位超标信息
     *
     * @param pageBasicVo
     * @return
     */
    @Override
    public Page<ExcessInfoDto> excessInfo(PageBasicVo<Long> pageBasicVo) {
        Page<ExcessInfoDto> page = new Page<>(pageBasicVo.getCurrentPage(), pageBasicVo.getPageSize());
        //获取用户可以查看的单位信息
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();
        return unitMapper.excessInfo(page, pageBasicVo, unitIds);
    }

    /**
     * 查询超标单位
     *
     * @param unitIdList
     * @return
     */
    @Override
    public List<ExcessUnitDto> selectExcessUnit(List<Long> unitIdList) {
        try {
            List<Long> unitIds = new ArrayList<>();
            //判断查询条件
            if (CollectionUtils.isNotEmpty(unitIdList)) {
                unitIds.addAll(unitIdList);
            } else {
                //获取用户可以查看的单位信息
                unitIds.addAll(authorityUnitService.getUnitIdByToken());
            }
            Integer number = 0;
            //查询单位的使用面积与附属用房面积
            List<ExcessUnitDto> list = unitMapper.selectExcessUnit(unitIds);
            if (CollectionUtils.isNotEmpty(list)) {
                for (ExcessUnitDto excessUnitDto : list) {
                    //获取单位Id
                    Long unitId = excessUnitDto.getUnitId();
                    //查询该单位下分布在不同楼栋的使用面积
                    List<CommonHouseTypeDto> commonHouseType = unitMapper.getAreaByBuild(unitIds, unitId);
                    BigDecimal comHouseBuildArea = new BigDecimal("0");
                    if (CollectionUtils.isNotEmpty(commonHouseType)) {
                        for (CommonHouseTypeDto commonHouseTypeDto : commonHouseType) {
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
                    excessUnitDto.setBuildAreaSum(comHouseBuildArea);
                    //查询该单位的编制信息和机关级别
                    Unit unitOffice = unitMapper.selectOne(new QueryWrapper<Unit>().eq("unit_id", unitId));
                    Integer officeLevel = unitOffice.getOfficeLevel();
                    List<UnitProfessional> unitProfessionalList = unitProfessionalMapper.selectList(new QueryWrapper<UnitProfessional>().eq("unit_id", unitId));
                    //编制人数
                    long professionalNum = 0;
                    //办公室用房核定面积
                    BigDecimal standardAreaByBanGong = new BigDecimal("0");
                    if (CollectionUtils.isNotEmpty(unitProfessionalList)) {
                        //计算该单位的编制人数
                        professionalNum = unitProfessionalList.stream().mapToLong(UnitProfessional::getProfessionalNum).sum();
                        //获取工勤编制数
                        Unit unit = unitMapper.selectById(unitId);
                        Integer unitWork = unit.getUnitWork();
                        if (unitWork != 0) {
                            professionalNum += unitWork;
                        }
                        //获取单位副职数
                        Integer unitDeputy = unit.getUnitDeputy();
                        //单位副职对应的面积--字典表获取
                        RepairDic repairDic = repairDicMapper.selectOne(new QueryWrapper<RepairDic>().eq("repair_dic_key", "unit_deputy"));
                        String repairDicValue = repairDic.getRepairDicValue();
                        Float specifiedArea = Float.valueOf(repairDicValue);
                        //获取办公室用房核定面积
                        standardAreaByBanGong = unitMapper.getStandardAreaByBanGong(unitId);
                        //单位副职数不为0的时候 加入到办公室核定面积中,加入到服务用房人数中
                        if (unitDeputy != 0) {
                            professionalNum += unitDeputy;
                            float unitDeputyArea = unitDeputy * specifiedArea;
                            standardAreaByBanGong = standardAreaByBanGong.add(new BigDecimal(String.valueOf(unitDeputyArea)));
                        }
                    }
                    //查询服务用房核定面积标准   机关级别(1市级机关 2县级机关 3乡级机关)
                    Long standardArea = 0L;
                    //判断服务用房核定面积计算标准
                    if (officeLevel.equals(1)) {
                        if (professionalNum <= 200) {
                            standardArea = 8L;
                        } else if (professionalNum >= 400) {
                            standardArea = 6L;
                        } else {
                            standardArea = (1000 - professionalNum) / 100;
                        }
                    } else if (officeLevel.equals(2) || officeLevel.equals(3)) {
                        if (professionalNum <= 200) {
                            standardArea = 8L;
                        } else if (professionalNum >= 400) {
                            standardArea = 6L;
                        } else {
                            standardArea = (500 - professionalNum) / 50;
                        }
                    }
                    //服务用房核定面积
                    BigDecimal standardAreaByFuWu = new BigDecimal(String.valueOf(standardArea)).multiply(new BigDecimal(String.valueOf(professionalNum)));
                    //办公室用房使用面积
                    BigDecimal useAreaByBanGong = unitMapper.getAreaByUnit(unitId, 1);
                    //服务用房使用面积
                    BigDecimal useAreaByFuWu = unitMapper.getAreaByUnit(unitId, 2);
                    //设备用房使用面积
                    BigDecimal useAreaBySheBei = unitMapper.getAreaByUnit(unitId, 3);
                    //计算设备用房的核定面积
                    BigDecimal standardAreaBySheBei = useAreaByBanGong.add(useAreaByFuWu).multiply(new BigDecimal(String.valueOf(0.09))).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //计算办公用房超标比列与超标面积
                    BigDecimal excessRatioByBanGong = new BigDecimal("0.00");
                    BigDecimal excessAreaByBanGong = new BigDecimal("0.00");
                    if (useAreaByBanGong.compareTo(standardAreaByBanGong) == 1) { //超标 使用大于核定
                        excessAreaByBanGong = useAreaByBanGong.subtract(standardAreaByBanGong);
                        excessRatioByBanGong = excessAreaByBanGong.multiply(new BigDecimal(String.valueOf(100))).divide(useAreaByBanGong, 2, BigDecimal.ROUND_HALF_UP);
                    }
                    excessUnitDto.setExcessRatioByBanGong(excessRatioByBanGong);
                    excessUnitDto.setExcessAreaByBanGong(excessAreaByBanGong);
                    //计算服务用房超标比列与超标面积
                    BigDecimal excessRatioByFuWu = new BigDecimal("0.00");
                    BigDecimal excessAreaByFuWu = new BigDecimal("0.00");
                    if (useAreaByFuWu.compareTo(standardAreaByFuWu) == 1) { //超标 使用大于核定
                        excessAreaByFuWu = useAreaByFuWu.subtract(standardAreaByFuWu);
                        excessRatioByFuWu = excessAreaByFuWu.multiply(new BigDecimal(String.valueOf(100))).divide(useAreaByFuWu, 2, BigDecimal.ROUND_HALF_UP);
                    }
                    excessUnitDto.setExcessRatioByFuWu(excessRatioByFuWu);
                    excessUnitDto.setExcessAreaByFuWu(excessAreaByFuWu);
                    //计算设备用房超标比列与超标面积
                    BigDecimal excessRatioBySheBei = new BigDecimal("0.00");
                    BigDecimal excessAreaBySheBei = new BigDecimal("0.00");
                    if (useAreaBySheBei.compareTo(standardAreaBySheBei) == 1) { //超标 使用大于核定
                        excessAreaBySheBei = useAreaBySheBei.subtract(standardAreaBySheBei);
                        excessRatioBySheBei = excessAreaBySheBei.multiply(new BigDecimal(String.valueOf(100))).divide(useAreaBySheBei, 2, BigDecimal.ROUND_HALF_UP);
                    }
                    excessUnitDto.setExcessRatioBySheBei(excessRatioBySheBei);
                    excessUnitDto.setExcessAreaBySheBei(excessAreaBySheBei);
                    //计算基本办公用房数据
                    BigDecimal jiBenUseArea = new BigDecimal("0");
                    jiBenUseArea = jiBenUseArea.add(useAreaByBanGong).add(useAreaByFuWu).add(useAreaBySheBei);
                    BigDecimal jiBenStandardArea = new BigDecimal("0");
                    jiBenStandardArea = jiBenStandardArea.add(standardAreaByBanGong).add(standardAreaByFuWu).add(standardAreaBySheBei);
                    BigDecimal jiBenExcessRatio = new BigDecimal("0.00");
                    BigDecimal jiBenExcessArea = new BigDecimal("0.00");
                    if (jiBenUseArea.compareTo(jiBenStandardArea) == 1) {
                        jiBenExcessArea = jiBenUseArea.subtract(jiBenStandardArea);
                        jiBenExcessRatio = jiBenExcessArea.multiply(new BigDecimal(String.valueOf(100))).divide(jiBenUseArea, 2, BigDecimal.ROUND_HALF_UP);
                    }
                    excessUnitDto.setJiBenExcessRatio(jiBenExcessRatio);
                    excessUnitDto.setJiBenExcessArea(jiBenExcessArea);

                    //设置参数
                    excessUnitDto.setStandardAreaByFuWu(standardAreaByFuWu);
                    excessUnitDto.setUseAreaByFuWu(useAreaByFuWu);
                    excessUnitDto.setStandardAreaByBanGong(standardAreaByBanGong);
                    excessUnitDto.setUseAreaByBanGong(useAreaByBanGong);
                    excessUnitDto.setUseAreaBySheBei(useAreaBySheBei);
                    excessUnitDto.setStandardAreaBySheBei(standardAreaBySheBei);
                    excessUnitDto.setJiBenUseArea(jiBenUseArea);
                    excessUnitDto.setJiBenStandardArea(jiBenStandardArea);

                    number++;
                    excessUnitDto.setNumber(String.valueOf(number));
                }
            }
            return list;
        } catch (Exception ex) {
            log.error("selectExcessUnitError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询房间明细汇总
     *
     * @return
     */
    @Override
    public List<HouseDetailByUnitDto> selectHouseDetailByUnit() {
        List<HouseDetailByUnitDto> houseDetailByUnitDtoList = new ArrayList<>();
        // 查询所有在使用房间的单位
        List<AllUseUnitDto> allUseList = unitMapper.selectAllUseUnit();
        if (CollectionUtils.isNotEmpty(allUseList)) {
            for (AllUseUnitDto allUseUnitDto : allUseList) {//所有单位
                Long unitId = allUseUnitDto.getUnitId();
                HouseDetailByUnitDto houseDetailByUnitDto = dozerBeanMapper.map(allUseUnitDto, HouseDetailByUnitDto.class);
                //获取面积明细数据--查询单位下楼栋分布
                List<AreaDetailByBuildDto> areaDetailDtoList = unitMapper.getAreaDetail(unitId);
                if (CollectionUtils.isNotEmpty(areaDetailDtoList)) {
                    //封装数据
                    List<AreaDetailDto> areaDetailDtos = new ArrayList<>();
                    //获取该单位下的每个楼栋
                    for (AreaDetailByBuildDto areaDetailByBuildDto : areaDetailDtoList) {//一个单位下的所有楼栋
                        String buildName = areaDetailByBuildDto.getBuildName();
                        String yardPosition = areaDetailByBuildDto.getYardPosition();
                        Long buildId = areaDetailByBuildDto.getBuildId();
                        BuildCoeDto buildCoeDto = (BuildCoeDto) houseTypeService.selectOneBuildCoe(buildId);
                        //该楼栋系数
                        Double buildCoe = buildCoeDto.getBuildCoe();
                        List<AreaDetailByHouseDto> areaDetailByHouseDtoList = areaDetailByBuildDto.getAreaDetailByHouseDtoList();
                        if (CollectionUtils.isNotEmpty(areaDetailByHouseDtoList)) {
                            AreaDetailDto areaDetailDto = new AreaDetailDto();
                            BigDecimal basicOfficeArea = new BigDecimal("0");
                            Float fuShuArea = 0.0f;
                            Float yeWuArea = 0.0f;
                            //获取该楼栋下房间使用面积
                            for (AreaDetailByHouseDto areaDetailByHouseDto : areaDetailByHouseDtoList) {//一个楼栋下的所有房间
                                //房屋类型 1办公室用房 2服务用房 3设备用房 4附属用房 5业务用房
                                Integer houseTypeId = areaDetailByHouseDto.getHouseTypeId();
                                //使用面积
                                Float houseUseAreaSum = areaDetailByHouseDto.getHouseUseAreaSum();
                                //计算基本办公用房面积
                                if (houseTypeId.equals(1) || houseTypeId.equals(2) || houseTypeId.equals(3)) {
                                    basicOfficeArea = basicOfficeArea.add(new BigDecimal(String.valueOf(houseUseAreaSum)));
                                }
                                if (houseTypeId.equals(4)) {
                                    fuShuArea = houseUseAreaSum;
                                }
                                if (houseTypeId.equals(5)) {
                                    yeWuArea = houseUseAreaSum;
                                }
                            }
                            //查询出租房间面积 1出租
                            Float rentArea = unitMapper.getRentOrLendArea("1", buildId);
                            //查询出借房间面积 2出借
                            Float lendArea = unitMapper.getRentOrLendArea("2", buildId);
                            //查询闲置房间面积
                            Float spareArea = unitMapper.getSpareArea(buildId);
                            //基本，业务，出租，出借，闲置使用面积之和
                            BigDecimal areaSum = new BigDecimal("0");
                            areaSum = areaSum.add(new BigDecimal(String.valueOf(basicOfficeArea)))
                                    .add(new BigDecimal(String.valueOf(yeWuArea))).add(new BigDecimal(String.valueOf(rentArea)))
                                    .add(new BigDecimal(String.valueOf(lendArea))).add(new BigDecimal(String.valueOf(spareArea)));
                            //计算建筑面积
                            BigDecimal buildAreaSum = new BigDecimal("0");
                            buildAreaSum = areaSum.divide(new BigDecimal(String.valueOf(buildCoe)), 2, BigDecimal.ROUND_HALF_UP).add(new BigDecimal(String.valueOf(fuShuArea)));
                            areaDetailDto.setBasicOfficeArea(basicOfficeArea);
                            areaDetailDto.setSubsidiaryArea(fuShuArea);
                            areaDetailDto.setBusinessArea(yeWuArea);
                            areaDetailDto.setRentArea(rentArea);
                            areaDetailDto.setLendArea(lendArea);
                            areaDetailDto.setSpareArea(spareArea);
                            areaDetailDto.setBuildArea(buildAreaSum);
                            areaDetailDto.setOfficeLocation(yardPosition + "-" + buildName);
                            areaDetailDtos.add(areaDetailDto);
                        }

                    }
                    houseDetailByUnitDto.setAreaDetailDtoList(areaDetailDtos);
                }
                houseDetailByUnitDtoList.add(houseDetailByUnitDto);
            }
        }
        return houseDetailByUnitDtoList;
    }

    /**
     * 房间明细信息导出
     *
     * @param response
     * @return
     */
    @Override
    public boolean exportHouseDetailExcel(List<HouseDetailByUnitDto> houseDetailByUnitDtoList, HttpServletResponse response) {

        try {
            ClassPathResource classPathResource = new ClassPathResource("static/excelTemplate/house.xls");
            InputStream inputStream = classPathResource.getInputStream();
            HSSFWorkbook hssfWorkbook = new HSSFWorkbook(inputStream);
            OutputStream output = null;
            try {
                HSSFCellStyle style = ExcelUtil.getHSSFCellStyle(hssfWorkbook);
                hssfWorkbook.setActiveSheet(0);
                hssfWorkbook.setSelectedTab(0);
                //设置文件内容及格式
                exportHouseDetail(hssfWorkbook, houseDetailByUnitDtoList, style);
                // 设置文件头
                String fileName = "房间明细.xls";
                // 设置文件头
                response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName, "UTF-8"));
                response.setHeader("File", URLEncoder.encode(fileName, "UTF-8"));
                // 输出Excel文件
                output = new BufferedOutputStream(response.getOutputStream());

                hssfWorkbook.write(output);
                return true;
            } catch (Exception ex) {
                log.error("exportHouseDetailExcelError", ex);
                throw new ServiceException(ex.getMessage());
            } finally {
                if (hssfWorkbook != null) {
                    hssfWorkbook.close();
                }
                if (output != null) {
                    output.flush();
                    output.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } catch (Exception ex) {
            log.error("exportHouseDetailExcelError", ex);
            throw new ServiceException(ex.getMessage());
        }

    }

    /**
     * 房间明细数据导出
     *
     * @param hssfWorkbook
     * @param houseDetailByUnitDtoList
     * @param style
     */
    public void exportHouseDetail(HSSFWorkbook hssfWorkbook, List<HouseDetailByUnitDto> houseDetailByUnitDtoList, HSSFCellStyle style) {
        try {
            int currentRow = 0;
            HSSFSheet sheet = hssfWorkbook.getSheet("明细汇总");
            sheet.setColumnWidth(5, 10000);
            if (CollectionUtils.isNotEmpty(houseDetailByUnitDtoList)) {
                for (int i = 0; i < houseDetailByUnitDtoList.size(); i++) {
                    HouseDetailByUnitDto houseDetailByUnitDto = houseDetailByUnitDtoList.get(i);
                    int lastRowNumber;
                    if (CollectionUtils.isNotEmpty(houseDetailByUnitDto.getAreaDetailDtoList())) {
                        for (int j = 0; j < houseDetailByUnitDto.getAreaDetailDtoList().size(); j++) {
                            //分布在多个楼栋
                            AreaDetailDto areaDetailDto = houseDetailByUnitDto.getAreaDetailDtoList().get(j);
                            lastRowNumber = sheet.getLastRowNum();
                            List contentCell = new ArrayList();
                            contentCell.add(i + 1);
                            contentCell.add(houseDetailByUnitDto.getUnitName());
                            contentCell.add(houseDetailByUnitDto.getUnitComplement());
                            contentCell.add(houseDetailByUnitDto.getUnitTypeName());
                            contentCell.add(houseDetailByUnitDto.getUnitGrade());
                            contentCell.add(areaDetailDto.getOfficeLocation());
                            contentCell.add(areaDetailDto.getBuildArea());
                            contentCell.add(areaDetailDto.getBasicOfficeArea());
                            contentCell.add(areaDetailDto.getSubsidiaryArea());
                            contentCell.add(areaDetailDto.getBusinessArea());
                            contentCell.add(areaDetailDto.getRentArea());
                            contentCell.add(areaDetailDto.getSpareArea());
                            contentCell.add(areaDetailDto.getLendArea());
                            contentCell.add("");
                            ExcelUtil.reportHSSFWorkbooks(sheet, lastRowNumber, contentCell, style);
                        }
                        if (houseDetailByUnitDto.getAreaDetailDtoList().size() > 1) {
                            mergedRegion(currentRow, sheet, houseDetailByUnitDto);
                            currentRow += houseDetailByUnitDto.getAreaDetailDtoList().size();
                        } else {
                            currentRow++;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("exportHouseDetailError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 按文件模板合并单元格
     *
     * @param currentRow
     * @param sheet
     * @param houseDetailByUnitDto
     */
    private void mergedRegion(int currentRow, HSSFSheet sheet, HouseDetailByUnitDto houseDetailByUnitDto) {
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 3, 3 + currentRow + houseDetailByUnitDto.getAreaDetailDtoList().size() - 1, 0, 0));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 3, 3 + currentRow + houseDetailByUnitDto.getAreaDetailDtoList().size() - 1, 1, 1));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 3, 3 + currentRow + houseDetailByUnitDto.getAreaDetailDtoList().size() - 1, 2, 2));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 3, 3 + currentRow + houseDetailByUnitDto.getAreaDetailDtoList().size() - 1, 3, 3));
        sheet.addMergedRegion(new CellRangeAddress(currentRow + 3, 3 + currentRow + houseDetailByUnitDto.getAreaDetailDtoList().size() - 1, 4, 4));
//        sheet.addMergedRegion(new CellRangeAddress(currentRow + 3, 3 + currentRow + houseDetailByUnitDto.getAreaDetailDtoList().size() - 1, 5, 5));

    }

}
