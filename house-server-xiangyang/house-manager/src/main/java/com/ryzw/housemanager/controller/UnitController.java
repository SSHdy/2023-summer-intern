package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.Unit;
import com.ryzw.housemanager.entity.UnitProfessional;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.*;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.PageBasicVo;
import com.ryzw.housemanager.vo.PageVo;
import com.ryzw.housemanager.vo.UnitListVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 单位信息管理
 *
 * @author yz
 * @since 2018-10-22
 */
@RestController
@RequestMapping("/unit")
@Api(value = "UnitController", description = "单位房屋管理")
@Slf4j
public class UnitController {
    @Autowired
    private IUnitService unitService;
    @Autowired
    private IAuthorityUnitService authorityUnitService;

    @Autowired
    DozerBeanMapper dozerBeanMapper;
    @Autowired
    private IUseUnitService useUnitService;
    @Autowired
    private IHouseTypeService houseTypeService;
    @Autowired
    private IUnitProfessionalService unitProfessionalService;

    /**
     * 删除单个单位信息
     *
     * @param
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除单个单位信息", notes = "删除单个单位信息")
    public boolean deleteUnit(@RequestBody Unit unit) {
        return unitService.deleteByIds(unit.getUnitId());
    }


    /**
     * 查询单位信息
     *
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    @ApiOperation(value = "查询单位信息", notes = "查询单位信息")
    public List<Unit> getUnit() {
        QueryWrapper<Unit> wrapper = new QueryWrapper<>();
        wrapper.select("unit_id", "unit_name");
        //只查为单位的(unit_classify不等于0的)
        wrapper.ne("unit_classify", 0);
        wrapper.orderByDesc("unit_id");
        return unitService.list(wrapper);
    }

    /**
     * 用户权限下的单位列表(禁用单位显示)
     *
     * @return
     */
    @ApiOperation(value = "用户权限下的单位列表", notes = "用户权限下的单位列表")
    @RequestMapping(value = "/getUserUnit", method = RequestMethod.POST)
    public List<UnitDisableDto> getUserUnit() {
        //获取用户可以查看的单位信息
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();
        QueryWrapper<Unit> wrapper = new QueryWrapper<>();

        wrapper.select("unit_id", "unit_name");
        wrapper.orderByAsc("unit_id");
        List<Unit> units = unitService.list(wrapper);
        return units.stream().map(p -> unit2UnitDisable(p, unitIds)).collect(Collectors.toList());
    }

    /**
     * 用户权限下的单位列表(只显示可见单位)
     *
     * @return
     */
    @ApiOperation(value = "用户权限下的单位列表", notes = "用户权限下的单位列表")
    @RequestMapping(value = "/getUnitByUser", method = RequestMethod.POST)
    public List<Unit> getUnitByUser() {
        //获取用户可以查看的单位信息
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();
        QueryWrapper<Unit> wrapper = new QueryWrapper<>();
        if (CollectionUtils.isNotEmpty(unitIds)) {
            wrapper.in("unit_id", unitIds);
        } else {
            wrapper.eq("unit_id", 0);
        }
        wrapper.select("unit_id", "unit_name");
        wrapper.orderByDesc("unit_id");
        List<Unit> units = unitService.list(wrapper);
        return units;
    }


    /**
     * 查询重新分配的单位列表
     *
     * @return
     */
    @ApiOperation(value = "查询重新分配的单位列表", notes = "查询重新分配的单位列表")
    @RequestMapping(value = "/reapportionUnit", method = RequestMethod.POST)
    public List<Unit> reapportionUnit() {
        List<Long> useUnitIds = useUnitService.useUnitIds();

        //获取用户可以查看的单位信息
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();
        unitIds.retainAll(useUnitIds);

        QueryWrapper<Unit> wrapperUnit = new QueryWrapper<>();
        wrapperUnit.select("unit_id", "unit_name");
        wrapperUnit.in("unit_id", unitIds);
        wrapperUnit.orderByDesc("unit_id");
        return unitService.list(wrapperUnit);
    }

    /**
     * 查询重新分配单位下拉列表
     *
     * @return
     */
    @ApiOperation(value = "查询重新分配单位下拉列表", notes = "查询重新分配单位下拉列表")
    @RequestMapping(value = "/reapportionUnitList", method = RequestMethod.POST)
    public List<ReapportionUnitListDto> reapportionUnitList() {

        return unitService.reapportionUnitList();
    }


    /**
     * 将Unit转换成UnitDisableDto对象
     *
     * @param unit
     * @param unitIds
     * @return
     */
    private UnitDisableDto unit2UnitDisable(Unit unit, List<Long> unitIds) {
        UnitDisableDto unitDisableDto = dozerBeanMapper.map(unit, UnitDisableDto.class);
        unitDisableDto.setDisabled(CollectionUtils.isEmpty(unitIds) || unitIds.stream().allMatch(p -> !p.equals(unit.getUnitId())));
        return unitDisableDto;
    }

    /**
     * 新增单位信息
     *
     * @param unitProfessAddDto
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "新增或保存单位信息", notes = "新增或保存单位信息")
    public boolean addUnit(@RequestBody @Valid UnitProfessAddDto unitProfessAddDto) {
        try {
            Unit unit = unitProfessAddDto.getUnit();
            List<UnitProfessional> unitProfessionalList = unitProfessAddDto.getUnitProfessionalList();
            if (unit.getUnitId() == null) {
                List<Unit> allUnitList = unitService.list(new QueryWrapper<Unit>());
                List<Unit> unitList = getAllUnit(allUnitList, unit.getUnitPid());
                List<String> unitTreeIds = new ArrayList();
                if (unitList != null && unitList.size() > 0) {
                    Collections.reverse(unitList);
                    for (Unit units : unitList) {
                        unitTreeIds.add(units.getUnitId().toString());
                    }
                }
                unit.setUnitTreeId(String.join(":", unitTreeIds));
            }
            unitService.saveOrUpdate(unit);
            if (unitProfessionalList != null) {
                //如果之前存在单位职级关系就删除
                unitProfessionalService.remove(new QueryWrapper<UnitProfessional>().eq("unit_id", unit.getUnitId()));
                for (UnitProfessional unitProfessional : unitProfessionalList) {
                    unitProfessional.setUnitId(unit.getUnitId());
                    //新增新的单位职级关系
                    unitProfessionalService.save(unitProfessional);
                }
            }

            return true;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 递归得到父级id
     *
     * @param allUnitList
     * @param unitPid
     * @return
     */
    private List<Unit> getAllUnit(List<Unit> allUnitList, Long unitPid) {
        List<Unit> unitList = new ArrayList<>();
        if (unitPid != 0 || unitPid != null) {
            getParentUnit(allUnitList, unitPid, unitList);
        }
        return unitList;
    }

    private static void getParentUnit(List<Unit> allUnitList, Long unitPid, List<Unit> unitList) {
        for (Unit unit : allUnitList) {
            // 判断是否存在父节点
            if (unit.getUnitId().equals(unitPid)) {
                unitList.add(unit);
                getParentUnit(allUnitList, unit.getUnitPid(), unitList);
            }
        }
    }

    /**
     * 通过id查询单位信息
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/findById", method = RequestMethod.POST)
    @ApiOperation(value = "通过id查询单位信息", notes = "通过id查询单位信息")
    public UnitListDto findById(@RequestBody BasicVo<Long> basicVo) {

        return unitService.findById(basicVo.getId());
    }

    /**
     * 查询一级单位和它下面的子级单位，并且按照一级单位进行分页，不需要查询条件
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "查询一级单位和它下面的子级单位，并且按照一级单位进行分页，不需要查询条件", notes = "查询一级单位和它下面的子级单位，并且按照一级单位进行分页，不需要查询条件")
    public Page<UnitChildDto> unitList(@RequestBody PageVo pageVo) {
        return unitService.unitList(pageVo);
    }

    /**
     * 通过单位名称和类型，分页查询单位信息，如果没有查询条件，则分页显示所有单位，不区分一二级
     *
     * @return
     */
    @RequestMapping(value = "/unitAll", method = RequestMethod.POST)
    @ApiOperation(value = "通过单位名称和类型，分页查询单位信息，如果没有查询条件，则分页显示所有单位，不区分一二级", notes = "通过单位名称和类型，分页查询单位信息，如果没有查询条件，则分页显示所有单位，不区分一二级")
    public Page<UnitListDto> unitAllList(@RequestBody UnitListVo unitListVo) {
        return unitService.unitAll(unitListVo);
    }

    /**
     * 通过id查询单位信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/findByRoleUnit", method = RequestMethod.POST)
    @ApiOperation(value = "查询单位id返回给角色", notes = "查询单位id返回给角色")
    public List<Map<Object, Object>> findByRoleUnit(@RequestBody List<Long> ids) {
        List<Map<Object, Object>> unitList = unitService.findByRoleUnit(ids);
        if (unitList != null) {
            return unitList;
        } else {
            throw new ServiceException("没找到！");
        }
    }


    /**
     * 查询单位树形菜单
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "all/tree", method = RequestMethod.POST)
    @ApiOperation(value = "树形菜单", notes = "树形菜单")
    public List<Map<String, Object>> treeNode(@RequestBody @Valid BasicVo<Long> basicVo) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<AllUnitTreeDto> unitList = unitService.allUnitTree(basicVo.getId());
        unitList.forEach(unit -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", unit.getUnitId());
            map.put("text", unit.getUnitName());
            map.put("type", unit.getUnitClassify());
            map.put("expand", unit.getUnitPcount() > 0);
            list.add(map);
        });
        return list;
    }

    /**
     * 单位树房间信息
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "unitTreeDetail", method = RequestMethod.POST)
    @ApiOperation(value = "单位树房间信息", notes = "单位树房间信息")
    public HouseTypeReportDto unitTreeDetail(@RequestBody @Valid BasicVo<Long> basicVo) {
        return houseTypeService.unitDetail(basicVo);
    }


    /**
     * 查询单位超标信息
     *
     * @param pageVo
     * @return
     */
    @RequestMapping(value = "unitExcess", method = RequestMethod.POST)
    @ApiOperation(value = "查询单位超标信息", notes = "查询单位超标信息")
    public Page<LeaderExcessDto> unitExcess(@RequestBody @Valid PageVo pageVo) {
        return unitService.unitExcess(pageVo);
    }

    /**
     * 分页查询单位超标信息
     *
     * @param pageBasicVo
     * @return
     */
    @RequestMapping(value = "excessInfo", method = RequestMethod.POST)
    @ApiOperation(value = "分页查询单位超标信息", notes = "分页查询单位超标信息")
    public Page<ExcessInfoDto> excessInfo(@RequestBody @Valid PageBasicVo<Long> pageBasicVo) {
        return unitService.excessInfo(pageBasicVo);
    }
}