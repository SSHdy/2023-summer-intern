package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.BuildCoeDto;
import com.ryzw.housemanager.dto.HouseTypeDto;
import com.ryzw.housemanager.dto.HouseTypeReportDto;
import com.ryzw.housemanager.dto.TypeReportDto;
import com.ryzw.housemanager.entity.HouseType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.HouseTypeVo;
import com.ryzw.housemanager.vo.PageBasicVo;
import com.ryzw.housemanager.vo.UnitAndBuildVo;

import java.util.List;

/**
 * <p>
 * 房屋类型表 服务类
 * </p>
 *
 * @author lyx
 * @since 2018-12-13
 */
public interface IHouseTypeService extends IService<HouseType> {


    /**
     * 房屋类型报表查询
     *
     * @param unitIds
     * @param basicVo
     * @return
     */
    List<HouseTypeReportDto> houseTypeReport(List<Long> unitIds, BasicVo<Long> basicVo);

    /**
     * 服务用房报表查询
     *
     * @param houseTypeVo
     * @return
     */
    List<TypeReportDto> serviceHouseReport(HouseTypeVo houseTypeVo);

    /**
     * 单位树房间信息
     *
     * @param basicVo
     * @return
     */
    HouseTypeReportDto unitDetail(BasicVo<Long> basicVo);

    /**
     * 查询房屋类型报表
     *
     * @param unitAndBuildVo
     * @return
     */
    List<HouseTypeDto> selectHouseType(UnitAndBuildVo unitAndBuildVo);

    /**
     * 查询各楼栋的系数
     *
     * @return
     */
    List<BuildCoeDto> selectBuildCoe();

    /**
     * 查询单个楼栋的系数
     *
     * @param buildId
     * @return
     */
    Object selectOneBuildCoe(Long buildId);
}
