package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.Unit;
import com.ryzw.housemanager.vo.PageBasicVo;
import com.ryzw.housemanager.vo.UnitListVo;
import com.ryzw.housemanager.vo.UnitSubordinateVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 单位信息表 Mapper 接口
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
public interface UnitMapper extends BaseMapper<Unit> {

    /**
     * 查询一级单位和它下面的子级单位，并且按照一级单位进行分页，不需要查询条件
     *
     * @return
     */
    List<UnitChildDto> unitList();

    /**
     * 查询一级单位和它下面的子级单位，并且按照一级单位进行分页，不需要查询条件
     *
     * @param page
     * @return
     */
    Page<UnitChildDto> unitListByPage(Page page);

    /**
     * 通过单位名称和类型，分页查询单位信息，如果没有查询条件，则分页显示所有单位，不区分一二级
     *
     * @param page
     * @param unitListVo
     * @return
     */
    Page<UnitListDto> unitAll(Page page, @Param("Unit") UnitListVo unitListVo);

    /**
     * 通过id查询单位信息
     *
     * @param id
     * @return
     */
    UnitListDto findById(@Param("id") Long id);

    /**
     * 通过角色接口传入的ids查找对应的unit单位名称
     *
     * @param ids
     * @return
     */
    List<Map<Object, Object>> findByRoleUnit(List<Long> ids);

    /**
     * 查询单位树列表
     *
     * @param unitIds
     * @return
     */
    List<UnitTreeDto> unitTree(@Param("unitIds") List<Long> unitIds);

    /**
     * 查询单位树所有节点（无权限）
     *
     * @param unitId
     * @return
     */
    List<AllUnitTreeDto> allUnitTree(@Param("unitId") Long unitId);

    /**
     * 查询出所有的单位信息
     *
     * @return
     */
    List<AllUnitDto> infinityTree();

    /**
     * 单位报表信息查询
     *
     * @return
     */
    UnitReportInfoDto unitReport(@Param("unitId") Long unitId);

    /**
     * 查询房屋类型面积
     *
     * @param unitId
     * @return
     */
    List<TypeAreaDto> typeArea(@Param("unitId") Long unitId);

    /**
     * 下属单位信息信息报表查询
     *
     * @param unitSubordinateVo
     * @return
     */
    List<UnitSubordinateDto> unitSubordinate(@Param("unitId") UnitSubordinateVo unitSubordinateVo);

    /**
     * 查询重新分配单位下拉列表
     *
     * @param unitIds
     * @return
     */
    List<ReapportionUnitListDto> reapportionUnitList(@Param("unitId") List<Long> unitIds);

    /**
     * 查询单位超标信息
     *
     * @param page
     * @return
     */
    Page<LeaderExcessDto> unitExcess(Page page);

    /**
     * 分页查询单位超标信息
     *
     * @param page
     * @return
     */
    Page<ExcessInfoDto> excessInfo(Page page, @Param("excess") PageBasicVo<Long> pageBasicVo, @Param("unitIds") List<Long> unitIds);

    /**
     * 查询单位的使用面积与附属用房面积
     *
     * @param unitIds
     * @return
     */
    List<ExcessUnitDto> selectExcessUnit(@Param("unitIds") List<Long> unitIds);

    /**
     * 查询该单位下分布在不同楼栋的使用面积
     *
     * @param unitIds
     * @param unitId
     * @return
     */
    List<CommonHouseTypeDto> getAreaByBuild(@Param("unitIds") List<Long> unitIds, @Param("unitId") Long unitId);

    /**
     * 查询该单位下使用的办公室或服务或设备用房的使用面积之和
     *
     * @param unitId
     * @param type
     * @return
     */
    BigDecimal getAreaByUnit(@Param("unitId") Long unitId, @Param("type") Integer type);

    /**
     * 获取办公室用房核定面积
     *
     * @param unitId
     * @return
     */
    BigDecimal getStandardAreaByBanGong(@Param("unitId") Long unitId);

    /**
     * 查询编制信息和等级信息
     *
     * @param id
     * @return
     */
    List<ProNumAndLevelDto> getNumAndLevel(@Param("id") Long id);

    /**
     * 获取用到该单位的楼栋Id集合
     *
     * @param unitId
     * @return
     */
    List<Long> getBuildId(@Param("unitId") Long unitId);

    /**
     * 查询所有在使用房间的单位
     *
     * @return
     */
    List<AllUseUnitDto> selectAllUseUnit();

    /**
     * 获取面积明细数据
     *
     * @param unitId
     * @return
     */
    List<AreaDetailByBuildDto> getAreaDetail(@Param("unitId") Long unitId);

    /**
     * 查询出租或出借房间面积
     *
     * @param flag
     * @param buildId
     * @return
     */
    Float getRentOrLendArea(@Param("flag") String flag, @Param("buildId") Long buildId);

    /**
     * 查询闲置房间面积
     *
     * @param buildId
     * @return
     */
    Float getSpareArea(@Param("buildId") Long buildId);
}
