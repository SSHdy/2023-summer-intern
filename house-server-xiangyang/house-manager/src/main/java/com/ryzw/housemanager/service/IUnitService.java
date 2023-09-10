package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.Unit;
import com.ryzw.housemanager.vo.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 单位信息表 服务类
 * </p>
 *
 * @author mfl
 * @since 2018-11-21
 */
public interface IUnitService extends IService<Unit> {
    /**
     * 查询一级单位和它下面的子级单位，并且按照一级单位进行分页，不需要查询条件
     *
     * @return
     */
    Page<UnitChildDto> unitList(PageVo pageVo);


    /**
     * 通过id查询单位信息
     *
     * @param id
     * @return
     */
    UnitListDto findById(Long id);

    /**
     * 通过单位名称和类型，分页查询单位信息，如果没有查询条件，则分页显示所有单位，不区分一二级
     *
     * @param unitListVo
     * @return
     */
    Page<UnitListDto> unitAll(UnitListVo unitListVo);

    /**
     * 通过角色接口传入的ids查找对应的unit单位名称
     *
     * @param ids
     * @return
     */
    List<Map<Object, Object>> findByRoleUnit(List<Long> ids);

    /**
     * 单位树查询
     *
     * @param unitIds
     * @return
     */
    List<UnitTreeDto> unitTree(List<Long> unitIds);


    /**
     * 查询单位树所有节点（无权限）
     *
     * @param unitId
     * @return
     */
    List<AllUnitTreeDto> allUnitTree(Long unitId);

    /**
     * 查询出所有的单位信息
     *
     * @return
     */
    List<AllUnitDto> infinityTree();


    /**
     * 批量删除单位
     *
     * @param id
     * @return
     */
    boolean deleteByIds(Long id);

    /**
     * 单位报表信息查询
     *
     * @return
     */
    UnitReportInfoDto unitReport(Long unitId);

    /**
     * 查询房屋类型面积
     *
     * @param unitId
     * @return
     */
    List<TypeAreaDto> typeArea(Long unitId);

    /**
     * 下属单位信息信息报表查询
     *
     * @param unitSubordinateVo
     * @return
     */
    List<UnitSubordinateDto> unitSubordinate(UnitSubordinateVo unitSubordinateVo);

    /**
     * 查询重新分配单位下拉列表
     *
     * @param
     * @return
     */
    List<ReapportionUnitListDto> reapportionUnitList();

    /**
     * 查询单位超标信息
     *
     * @param pageVo
     * @return
     */
    Page<LeaderExcessDto> unitExcess(PageVo pageVo);

    /**
     * 分页查询单位超标信息
     *
     * @param pageBasicVo
     * @return
     */
    Page<ExcessInfoDto> excessInfo(PageBasicVo<Long> pageBasicVo);

    /**
     * 查询超标单位
     *
     * @param unitIdList
     * @return
     */
    List<ExcessUnitDto> selectExcessUnit(List<Long> unitIdList);

    /**
     * 查询房间明细汇总
     *
     * @return
     */
    List<HouseDetailByUnitDto> selectHouseDetailByUnit();

    /**
     * 房间明细信息导出
     *
     * @param houseDetailByUnitDtoList
     * @param response
     * @return
     */
    boolean exportHouseDetailExcel(List<HouseDetailByUnitDto> houseDetailByUnitDtoList, HttpServletResponse response);
}
