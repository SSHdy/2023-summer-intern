package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.BusinessAreaDto;
import com.ryzw.housemanager.dto.BusinessHouseDto;
import com.ryzw.housemanager.dto.ConstructHouseDto;
import com.ryzw.housemanager.dto.ReportUnitDto;
import com.ryzw.housemanager.entity.ConfigureBuild;
import com.ryzw.housemanager.entity.ReportUnit;
import com.ryzw.housemanager.vo.ReportUnitVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 单位上报表 Mapper 接口
 * </p>
 *
 * @author yz
 * @since 2019-08-29
 */
public interface ReportUnitMapper extends BaseMapper<ReportUnit> {

    /**
     * 查询单位上报列表
     *
     * @param page
     * @param reportUnitVo
     * @return
     */
    Page<ReportUnitDto> reportUnitList(Page<ReportUnitDto> page, @Param("reportUnitVo") ReportUnitVo reportUnitVo);

    /**
     * 业务用房列表
     *
     * @param unitIds
     * @return
     */
    List<BusinessHouseDto> businessHousing(@Param("unitIds") List<Long> unitIds);

    /**
     * 查询主要使用单位集合
     *
     * @param houseIdList
     * @return
     */
    List<String> selectUnitList(@Param("houseIdList") List<String> houseIdList);

    /**
     * 查询相关面积
     *
     * @param yardId
     * @return
     */
    BusinessAreaDto selectArea(@Param("yardId") Long yardId);

    /**
     * 查询权属登记情况
     *
     * @param yardId
     * @return
     */
    String selectRemark(@Param("yardId") Long yardId);

    /**
     * 查询所有建设的配置楼栋
     *
     * @return
     */
    List<ConfigureBuild> selectBuildConfigure();

    /**
     * 查询建设楼栋相关信息
     *
     * @param buildId
     * @return
     */
    ConstructHouseDto constructHouseList(@Param("buildId") Long buildId, @Param("unitIds") List<Long> unitIds);

    /**
     * 查询建设楼栋主要使用单位
     *
     * @param buildId
     * @return
     */
    List<String> constructHouseUnitList(@Param("buildId") Long buildId);

    /**
     * 查询所有单位集合
     *
     * @param reportUnitVo
     * @return
     */
    List<ReportUnitDto> unitList(@Param("reportUnitVo") ReportUnitVo reportUnitVo);
}
