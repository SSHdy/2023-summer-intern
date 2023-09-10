package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.HouseType;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.HouseTypeMapper;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IHouseTypeService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.HouseTypeVo;
import com.ryzw.housemanager.vo.UnitAndBuildVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 房屋类型表 服务实现类
 * </p>
 *
 * @author lyx
 * @since 2018-12-13
 */
@Service
@Slf4j
public class HouseTypeServiceImpl extends ServiceImpl<HouseTypeMapper, HouseType> implements IHouseTypeService {
    @Autowired
    private HouseTypeMapper houseTypeMapper;

    @Autowired
    IAuthorityUnitService authorityUnitService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 房屋类型报表查询
     *
     * @param unitIds
     * @param basicVo
     * @return
     */
    @Override
    public List<HouseTypeReportDto> houseTypeReport(List<Long> unitIds, BasicVo<Long> basicVo) {

        //获取用户可以查看的单位信息
        return houseTypeMapper.houseTypeReport(unitIds);
    }

    /**
     * 服务用房报表查询
     *
     * @param houseTypeVo
     * @return
     */
    @Override
    public List<TypeReportDto> serviceHouseReport(HouseTypeVo houseTypeVo) {
        return houseTypeMapper.serviceHouseReport(houseTypeVo);
    }


    /**
     * 单位树房间信息
     *
     * @param basicVo
     * @return
     */
    @Override
    public HouseTypeReportDto unitDetail(BasicVo<Long> basicVo) {
        return houseTypeMapper.unitDetail(basicVo);
    }


    /**
     * 查询房屋类型报表
     *
     * @param unitAndBuildVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<HouseTypeDto> selectHouseType(UnitAndBuildVo unitAndBuildVo) {
        try {
            List<Long> unitIdList = new ArrayList<>();
            Long buildId = unitAndBuildVo.getBuildId();
            //判断查询条件
            if (CollectionUtils.isNotEmpty(unitAndBuildVo.getUnitIdList())) {
                unitIdList.addAll(unitAndBuildVo.getUnitIdList());
            } else {
                unitIdList.addAll(authorityUnitService.getUnitIdByToken());
            }
            List<HouseTypeDto> resHouseTypeDto = new ArrayList<>();
            //查询办公用房类型的面积
            List<CommonHouseTypeDto> houseByBanGong = houseTypeMapper.areaByType(unitIdList, 1, buildId);
            this.CommonHouseType(resHouseTypeDto, houseByBanGong, "办公室用房", 0);
            //查询服务用房类型的面积
            List<CommonHouseTypeDto> houseByFuWu = houseTypeMapper.areaByType(unitIdList, 2, buildId);
            this.CommonHouseType(resHouseTypeDto, houseByFuWu, "服务用房", 0);
            //查询设备用房类型的面积
            List<CommonHouseTypeDto> houseBySheBei = houseTypeMapper.areaByType(unitIdList, 3, buildId);
            this.CommonHouseType(resHouseTypeDto, houseBySheBei, "设备用房", 0);
            //查询附属用房类型的面积
            List<CommonHouseTypeDto> houseByFuShu = houseTypeMapper.areaByType(unitIdList, 4, buildId);
            this.CommonHouseType(resHouseTypeDto, houseByFuShu, "附属用房", 1);
            //查询业务用房类型的面积
            List<CommonHouseTypeDto> houseByYeWu = houseTypeMapper.areaByType(unitIdList, 5, buildId);
            this.CommonHouseType(resHouseTypeDto, houseByYeWu, "业务用房", 0);
            return resHouseTypeDto;
        } catch (Exception ex) {
            log.error("selectHouseTypeError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询各种类型用房面积的公共方法
     *
     * @param resHouseTypeDto
     * @param commonHouseType
     * @param houseTypeName
     */
    public void CommonHouseType(List<HouseTypeDto> resHouseTypeDto, List<CommonHouseTypeDto> commonHouseType, String houseTypeName, Integer useBuildArea) {
        try {
            if (CollectionUtils.isNotEmpty(commonHouseType)) {
                BigDecimal comHouseBuildArea = new BigDecimal("0");
                BigDecimal comHouseuseArea = new BigDecimal("0");
                Integer houseNum = 0;
                for (CommonHouseTypeDto commonHouseTypeDto : commonHouseType) {
                    //获取楼栋Id
                    Long buildId = commonHouseTypeDto.getBuildId();
                    //获取该楼栋的系数
                    BuildCoeDto buildCoeDto = (BuildCoeDto) this.selectOneBuildCoe(buildId);
                    //获取用房总使用面积
                    BigDecimal houseUseAreaSum = commonHouseTypeDto.getHouseUseAreaSum();
                    //计算该楼栋下用房的建筑面积  保留两位四舍五入
                    BigDecimal bigDecimal = houseUseAreaSum.divide(new BigDecimal(String.valueOf(buildCoeDto.getBuildCoe())), 2, BigDecimal.ROUND_HALF_UP);
                    //计算用房数量
                    houseNum += commonHouseTypeDto.getHouseNum();
                    //计算用房建筑面积总和
                    comHouseBuildArea = comHouseBuildArea.add(bigDecimal);
                    //计算用房使用面积总和
                    comHouseuseArea = comHouseuseArea.add(new BigDecimal(String.valueOf(houseUseAreaSum))).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                HouseTypeDto houseTypeDto = new HouseTypeDto();
                houseTypeDto.setHouseTypeName(houseTypeName);
                houseTypeDto.setHouseNum(houseNum);
                houseTypeDto.setTotalBuildArea(comHouseBuildArea);
                houseTypeDto.setTotalUseArea(comHouseuseArea);
                houseTypeDto.setUseBuildArea(useBuildArea);
                resHouseTypeDto.add(houseTypeDto);
            }
        } catch (Exception ex) {
            log.error("CommonHouseTypeError", ex);
            throw new ServiceException(ex.getMessage());
        }

    }

    /**
     * 查询各楼栋的系数
     *
     * @return
     */
    @Override
    public List<BuildCoeDto> selectBuildCoe() {
        try {
            List<BuildCoeDto> buildCoeDtos = houseTypeMapper.selectBuildCoe();
            return buildCoeDtos;
        } catch (Exception ex) {
            log.error("selectBuildCoeError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询单个楼栋的系数
     *
     * @param buildId
     * @return
     */
    @Override
    public Object selectOneBuildCoe(Long buildId) {
        try {
            if (redisTemplate.hasKey("buildCoe:" + buildId)) {
                return redisTemplate.opsForValue().get("buildCoe:" + buildId);
            } else {
                BuildCoeDto buildCoeDto = houseTypeMapper.selectOneBuildCoe(buildId);
                redisTemplate.opsForValue().set("buildCoe:" + buildId, buildCoeDto);
                return buildCoeDto;
            }
        } catch (Exception ex) {
            log.error("selectOneBuildCoeError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

}
