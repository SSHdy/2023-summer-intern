package com.ryzw.housemanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.*;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.*;
import com.ryzw.housemanager.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 左侧树形菜单显示
 * </p >
 *
 * @author mfl
 * @since 2018/12/8
 */
@RestController
@Slf4j
@Api(value = "树形菜单", description = "树形菜单")
@RequestMapping("/tree")
public class TreeNodeController {

    @Autowired
    private IAuthorityUnitService authorityUnitService;

    @Autowired
    private IYardService yardService;
    @Autowired
    private IBuildService buildService;
    @Autowired
    private IUnitService unitService;
    @Autowired
    private IHouseService houseService;
    @Autowired
    DozerBeanMapper dozerBeanMapper;

    /*************************** 不含单位的院落、楼栋、楼层、房屋树形菜单查询start*******************/

    /**
     * 不含单位的院落、楼栋、楼层、房屋树形菜单查询
     *
     * @param treeVo
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "yard/list", method = RequestMethod.POST)
    @ApiOperation(value = "不含单位的院落、楼栋、楼层、房屋树形菜单查询", notes = "不含单位的院落、楼栋、楼层、房屋树形菜单查询")
    public List<Map<String, Object>> treeNode(@RequestBody @Valid TreeVo treeVo) throws Throwable {
        List<Map<String, Object>> list = new ArrayList<>();
        //获取用户可以查看的单位信息
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();
        if (CollectionUtils.isEmpty(unitIds)) {
            return list;
        }
//        查找根节点院落
        if (TreeNode.CAUSE.getValue().equals(treeVo.getType())) {
            List<YardResultVo> yardList = yardService.getAllByUnitID(unitIds, treeVo.getSearchYardId());
            ShowYard(yardList, list);
        }
//        查找楼栋节点
        else if (TreeNode.AMT.getValue().equals(treeVo.getType())) {
            buildNode(treeVo, list, unitIds);
        }
//        查找楼层节点
        else if (TreeNode.FLO.getValue().equals(treeVo.getType())) {
            floorNode(treeVo, list, unitIds);
        }
        //        查找用户权限下的所有房间
        else if (TreeNode.HOU.getValue().equals(treeVo.getType())) {
            houseNode(treeVo, list, unitIds);
        }
        return list;
    }

    //        查找用户权限下的所有房间
    private void houseNode(@RequestBody @Valid TreeVo treeVo, List<Map<String, Object>> list, List<Long> unitIds) {

        List<BuildUnitDto> houseList = buildService.houseList(treeVo.getId(), treeVo.getBuildId(), unitIds);
        houseList.forEach(house -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", house.getHouseId());
            map.put("_id", house.getYardId() + ":" + house.getBuildId() + ":" + house.getFloorName() + ":" + house.getHouseId());
            map.put("value", house.getHouseId());
            map.put("text", house.getHouseNumber());
            map.put("type", TreeNode.END.getValue());
            map.put("expand", false);
            map.put("address", house.getYardName() + "-" + house.getBuildName() + "-" + house.getFloorName() + "层" + "-" + house.getHouseNumber());
            list.add(map);
        });
    }

    //        查找用户权限下的所有楼层
    private void floorNode(@RequestBody @Valid TreeVo treeVo, List<Map<String, Object>> list, List<Long> unitIds) {
        List<BuildUnitDto> floorList = buildService.floorList(treeVo.getId(), unitIds);
        floorList.forEach(build -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", build.getYardId() + ":" + build.getBuildId() + ":" + build.getFloorName());
            map.put("value", build.getFloorName());
            map.put("text", build.getFloorName() + "层");
            map.put("type", TreeNode.HOU.getValue());
            map.put("expand", true);
            map.put("address", build.getYardName() + "-" + build.getBuildName() + "-" + build.getFloorName() + "层");
            list.add(map);
        });
    }

    //        查找用户权限下的楼栋节点
    private void buildNode(@RequestBody @Valid TreeVo treeVo, List<Map<String, Object>> list, List<Long> unitIds) {
        List<BuildUnitDto> buildList = buildService.buildUnit(treeVo.getId(), unitIds);
        buildList.forEach(build -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", build.getYardId() + ":" + build.getBuildId());
            map.put("value", build.getBuildId());
            map.put("text", build.getBuildName());
            map.put("type", TreeNode.FLO.getValue());
            map.put("expand", build.getFloorName() > 0);
            map.put("address", build.getYardName() + "-" + build.getBuildName());
            list.add(map);
        });
    }

    /**
     * 获取楼座的楼层
     *
     * @param floor
     * @param build
     * @return
     */
    private Map<String, Object> getFloorLevel(int floor, BuildFloorDto build) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", build.getBuildId() + ":" + floor);
        map.put("value", floor);
        map.put("text", floor + "层");
        map.put("yardId", build.getYardId());
        map.put("buildId", build.getBuildId());
        map.put("type", TreeNode.HOU.getValue());
        map.put("expand", build.getFloorName().stream().anyMatch(p -> p.equals(floor)));
        return map;
    }


    /**
     * 显示院落信息
     *
     * @param yardList
     * @param list
     */
    private void ShowYard(List<YardResultVo> yardList, List<Map<String, Object>> list) {
        yardList.forEach(yard -> {
            if (list.stream().allMatch(p -> !p.values().contains(yard.getYardId()))) {
                Map<String, Object> map = new HashMap<>();
                map.put("value", yard.getYardId());
                map.put("text", yard.getYardName());
                map.put("type", TreeNode.AMT.getValue());
                map.put("expand", yard.getBuildCount() > 0);
                map.put("id", yard.getYardId());
                map.put("address", yard.getYardName());
                list.add(map);
            }
        });
    }

    /*************************** 查询树形菜单 院落->楼栋->楼层end*******************/


    /*************************** 查询树形菜单 单位->院落->楼栋->楼层start*******************/
    /**
     * 查询树形菜单 单位->院落->楼栋->楼层
     *
     * @param unitTreeVo
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "unit/list", method = RequestMethod.POST)
    @ApiOperation(value = "单位树形菜单", notes = "单位树形菜单")
    public List<Map<String, Object>> unitTreeNode(@RequestBody @Valid UnitTreeVo unitTreeVo) throws Throwable {
        List<Map<String, Object>> list = new ArrayList<>();

//        //获取用户可以查看的单位信息
        List<Long> authUnitIds = authorityUnitService.getUnitIdByToken();

        List<Long> unitIds = unitTreeVo.getSearchUnitId() != null ?
                authUnitIds.stream().filter(p -> unitTreeVo.getSearchUnitId().equals(p)).collect(Collectors.toList())
                : authUnitIds;

        if (CollectionUtils.isEmpty(unitIds)) {
            return list;
        }


        //        查找根节点单位
        if (UnitTreeNode.NONUNIT.getValue().equals(unitTreeVo.getType()) ||
                UnitTreeNode.FIRSTUNIT.getValue().equals(unitTreeVo.getType())) {

            List<UnitTreeDto> unitList = unitService.unitTree(unitIds);

            Set<Long> treeIdSet = unitTreeVo.getUnitId() == null ? unitList.stream().filter(p -> StringUtils.isEmpty(p.getUnitTreeId()))
                    .map(UnitTreeDto::getUnitId).collect(Collectors.toSet()) : new HashSet<>();

            Map<Long, String> unitTreeMap = unitList.stream()
                    .filter(p -> StringUtils.isNotEmpty(p.getUnitTreeId())).collect(
                            Collectors.toMap(UnitTreeDto::getUnitId, UnitTreeDto::getUnitTreeId));

            for (Map.Entry<Long, String> unitTree : unitTreeMap.entrySet()) {
                if (Strings.isEmpty(unitTree.getValue())) {
                    treeIdSet.add(unitTree.getKey());
                } else {
                    //点击树unitTreeId
                    List<Long> uIds = Arrays.stream(unitTree.getValue().split(":")).filter(p -> Strings.isNotBlank(p)).map(p -> Long.valueOf(p)).collect(Collectors.toList());
                    if (unitTreeVo.getUnitId() == null) {
                        treeIdSet.add(uIds.get(0));
                    } else {
                        int index = uIds.indexOf(unitTreeVo.getUnitId());
                        if (index >= 0) {
                            Long value = index + 1 < uIds.size() ? uIds.get(index + 1) : unitTree.getKey();
                            treeIdSet.add(value);
                        }
                    }
                }
            }

            if (CollectionUtils.isNotEmpty(treeIdSet)) {
                Collection<Unit> unitLists = unitService.listByIds(treeIdSet);
                List<YardResultVo> yardList = yardService.getAllByUnitID(unitIds);
                unitLists.forEach(unit -> {
                    Map<String, Object> map = new HashMap<>(unitLists.size());
                    String idString = Strings.isNotBlank(unitTreeVo.getId()) ?
                            unitTreeVo.getId() + ":" + unit.getUnitId()
                            : String.valueOf(unit.getUnitId());
                    map.put("id", idString);
                    map.put("value", unit.getUnitId());
                    map.put("text", unit.getUnitName());
                    map.put("unitId", unit.getUnitId());
                    //有无权限
                    map.put("authority", (!UnitTreeNode.NONUNIT.getValue().equals(unit.getUnitClassify()) && StringUtils.isEmpty(unit.getUnitTreeId())) || unitIds.stream().anyMatch(p -> p.equals(unit.getUnitId())));
                    map.put("unitIds", unitList.stream().filter(p -> StringUtils.isNotEmpty(p.getUnitTreeId()) && p.getUnitTreeId().contains(String.valueOf(unit.getUnitId()))).map(UnitTreeDto::getUnitId).collect(Collectors.toList()));
                    map.put("type", unit.getUnitClassify());

                    map.put("expand", unitTreeMap.values().stream().anyMatch(p -> p.contains(String.valueOf(unit.getUnitId())))
                            || yardList.stream().anyMatch(p -> unit.getUnitId().equals(p.getUnitId())));
                    list.add(map);
                });
            }

        }
        //        查找对应单位下院落
        if (UnitTreeNode.YARD.getValue().equals(unitTreeVo.getType()) ||
                (UnitTreeNode.FIRSTUNIT.getValue().equals(unitTreeVo.getType())
                        && unitTreeVo.isAuthority())) {
            Optional.ofNullable(unitTreeVo).map(m -> m.getUnitId()).orElseThrow(() -> new ServiceException("单位编号不能为空"));

            if (!unitIds.contains(unitTreeVo.getUnitId())) {
                throw new ServiceException("该单位没有查询权限!");
            }

            List<YardResultVo> yardList = yardService.getAllByUnitID(Arrays.asList(unitTreeVo.getUnitId()));
            yardList.forEach(yard -> {
                Map<String, Object> map = new HashMap<>(yardList.size());
                map.put("id", unitTreeVo.getUnitId() + ":" + yard.getYardId());
                map.put("unitId", unitTreeVo.getUnitId());
                map.put("value", yard.getYardId());
                map.put("text", yard.getYardName());
                map.put("type", UnitTreeNode.BUILD.getValue());
                map.put("expand", yard.getBuildCount() > 0);
                list.add(map);
            });
        }

        //        查找对应单位院落下的楼栋节点
        if (UnitTreeNode.BUILD.getValue().equals(unitTreeVo.getType())) {
            Optional<UnitTreeVo> unitTreeVoOptional = Optional.ofNullable(unitTreeVo);
            unitTreeVoOptional.map(m -> m.getUnitId()).orElseThrow(() -> new ServiceException("单位编号不能为空"));
            unitTreeVoOptional.map(m -> m.getId()).orElseThrow(() -> new ServiceException("院落编号不能为空"));
            if (!unitIds.contains(unitTreeVo.getUnitId())) {
                throw new ServiceException("该单位没有查询权限!");
            }
            List<BuildUnitDto> buildList = buildService.buildUnit(Long.valueOf(unitTreeVo.getId()), Arrays.asList(unitTreeVo.getUnitId()));
            buildList.forEach(build -> {
                Map<String, Object> map = new HashMap<>(buildList.size());
                map.put("id", unitTreeVo.getUnitId() + ":" + build.getYardId() + ":" + build.getBuildId());
                map.put("value", build.getBuildId());
                map.put("text", build.getBuildName());
                map.put("type", UnitTreeNode.FLO.getValue());
                map.put("expand", build.getFloorName() > 0);
                map.put("unitId", unitTreeVo.getUnitId());
                list.add(map);
            });
        }

        //       查找对应单位院落楼栋下的楼层
        if (UnitTreeNode.FLO.getValue().equals(unitTreeVo.getType())) {
            Optional<UnitTreeVo> unitTreeVoOptional = Optional.ofNullable(unitTreeVo);
            unitTreeVoOptional.map(m -> m.getUnitId()).orElseThrow(() -> new ServiceException("单位编号不能为空"));
            unitTreeVoOptional.map(m -> m.getId()).orElseThrow(() -> new ServiceException("楼座编号不能为空"));
            if (!unitIds.contains(unitTreeVo.getUnitId())) {
                throw new ServiceException("该单位没有查询权限!");
            }
            List<BuildUnitDto> floorList = buildService.floorList(Long.valueOf(unitTreeVo.getId()), Arrays.asList(unitTreeVo.getUnitId()));
            floorList.forEach(floor -> {
                Map<String, Object> map = new HashMap<>(floorList.size());
                map.put("id", unitTreeVo.getUnitId() + ":" + floor.getYardId() + ":" + floor.getBuildId() + ":" + floor.getFloorName());
                map.put("text", floor.getFloorName() + "层");
                map.put("value", floor.getFloorName());
                map.put("yardId", floor.getYardId());
                map.put("expand", false);
                map.put("type", UnitTreeNode.HOUSE.getValue());
                map.put("unitId", unitTreeVo.getUnitId());
                map.put("buildId", floor.getBuildId());
                list.add(map);
            });
        }
        return list;
    }


    @RequestMapping(value = "yard/all", method = RequestMethod.POST)
    @ApiOperation(value = "树形菜单", notes = "树形菜单")
    public List<Map<String, Object>> treeAllNode(@RequestBody @Valid AllTreeVo treeVo) throws Throwable {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();
        if(CollectionUtils.isEmpty(unitIds)){
            return list;
        }
//        查找根节点院落
        if (TreeNode.CAUSE.getValue().equals(treeVo.getType())) {
            List<YardResultVo> yardList = yardService.getAll(treeVo.getSearchYardId(),unitIds);
            ShowYard(yardList, list);
        }
//        查找楼栋节点
        else if (TreeNode.AMT.getValue().equals(treeVo.getType())) {
            QueryWrapper<Build> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("build_id", "build_name", "overground_floor", "underground_floor");
            queryWrapper.orderByAsc("build_id");
            Optional optional = Optional.ofNullable(treeVo.getId());
            optional.ifPresent(p -> {
                queryWrapper.eq("yard_id", p);
                List<Build> buildList = buildService.list(queryWrapper);
                buildList.forEach(build -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", build.getBuildId());
                    map.put("value", build.getBuildId());
                    map.put("text", build.getBuildName());
                    map.put("type", TreeNode.FLO.getValue());
                    map.put("expand", !(Optional.ofNullable(build.getOvergroundFloor()).orElse(0) == 0 && Optional.ofNullable(build.getUndergroundFloor()).orElse(0) == 0));
                    list.add(map);
                });
            });
            optional.orElseThrow(() -> new ServiceException("id不能为空"));
        }

        //        查找楼层
        else if (TreeNode.FLO.getValue().equals(treeVo.getType())) {
            Optional<Long> optional = Optional.ofNullable(treeVo.getId());
            optional.ifPresent(p -> {
                BuildFloorDto build = buildService.getBuildFloor(p);
                if (build != null) {
                    int overGroundFloor = Optional.ofNullable(build.getOvergroundFloor()).orElse(0);
                    int undergroundFloor = Optional.ofNullable(build.getUndergroundFloor()).orElse(0);
                    for (int k = -Math.abs(undergroundFloor); k <= overGroundFloor; k++) {
                        if (k != 0) {
                            list.add(getFloorLevel(k, build));
                        }
                    }
                }
            });
            optional.orElseThrow(() -> new ServiceException("id不能为空"));
        }

        // 查找房间
        else if (TreeNode.HOU.getValue().equals(treeVo.getType())) {
            QueryWrapper<House> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("floor_name", treeVo.getId());
            queryWrapper.eq("build_id", Optional.ofNullable(treeVo.getBuildId()).orElseThrow(() -> new ServiceException("楼栋不能为空")));
            queryWrapper.select("house_id", "house_number");
            List<House> housesList = houseService.list(queryWrapper);
            housesList.forEach(house -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", house.getHouseId());
                map.put("value", house.getHouseId());
                map.put("text", house.getHouseNumber());
                list.add(map);
            });
        }
        return list;
    }
    /*************************** 查询树形菜单 单位->院落->楼栋->楼层end*******************/


    /*************************** 闲置用房树形菜单查询模块start*******************/

    /**
     * 闲置用房树形菜单查询
     *
     * @return
     */
    @RequestMapping(value = "/spareRoomTree", method = RequestMethod.POST)
    @ApiOperation(value = "闲置用房树形菜单查询", notes = "闲置用房树形菜单查询")
    public List<TreeDto> spareRoomTree(@RequestBody SpareTreeDto spareTreeDto) {
        List<SpareRoomTreeDto> spareHouseDtos = houseService.spareRoomTree(spareTreeDto);

        return spareHouseDtos.stream().map(spareHouseDto -> {
            TreeDto treeDto = new TreeDto();
            treeDto.setId(spareHouseDto.getYardId());
            treeDto.setName(spareHouseDto.getYardName());
            treeDto.setPId(0);
            treeDto.setLevel(0);
            treeDto.setChildren(getBuild(spareHouseDtos, spareHouseDto.getYardId()));
            return treeDto;
        }).distinct().collect(Collectors.toList());
    }


    /**
     * 获取楼座信息
     *
     * @param spareHouseDtos
     * @param yardId
     * @return
     */
    private List<TreeDto> getBuild(List<SpareRoomTreeDto> spareHouseDtos, Long yardId) {
        return spareHouseDtos.stream().filter(p -> yardId.equals(p.getYardId()))
                .map(spareHouseDto -> {
                    TreeDto treeDto = new TreeDto();
                    treeDto.setId(spareHouseDto.getBuildId());
                    treeDto.setName(spareHouseDto.getBuildName());
                    treeDto.setPId(spareHouseDto.getYardId());
                    treeDto.setLevel(1);
                    treeDto.setChildren(getFloor(spareHouseDtos, spareHouseDto.getBuildId()));
                    return treeDto;
                })
                .distinct()
                .collect(Collectors.toList());
    }


    /**
     * 获取楼座信息
     *
     * @param spareHouseDtos
     * @param buildId
     * @return
     */
    private List<TreeDto> getFloor(List<SpareRoomTreeDto> spareHouseDtos, Long buildId) {
        return spareHouseDtos.stream().filter(p -> buildId.equals(p.getBuildId()))
                .map(spareHouseDto -> {
                    TreeDto treeDto = new TreeDto();
                    treeDto.setId(spareHouseDto.getBuildId() + ":" + spareHouseDto.getFloorName());
                    treeDto.setName(spareHouseDto.getFloorName() + "楼");
                    treeDto.setChildren(getHouse(spareHouseDtos, spareHouseDto.getFloorName(), spareHouseDto.getBuildId()));
                    treeDto.setPId(spareHouseDto.getBuildId());
                    treeDto.setLevel(2);
                    return treeDto;
                })
                .distinct().collect(Collectors.toList());
    }


    /**
     * 获取楼座信息
     *
     * @param spareHouseDtos
     * @param buildId
     * @return
     */
    private List<TreeDto> getHouse(List<SpareRoomTreeDto> spareHouseDtos, Integer floorName, Long buildId) {
        return spareHouseDtos.stream().filter(p -> buildId.equals(p.getBuildId()) && floorName.equals(p.getFloorName()))
                .map(spareHouseDto -> {
                    TreeDto treeDto = new TreeDto();
                    treeDto.setId(spareHouseDto.getYardId() + ":" + spareHouseDto.getBuildId() + ":" + spareHouseDto.getFloorName()
                            + ":" + spareHouseDto.getHouseId());
                    treeDto.setName(spareHouseDto.getHouseNumber());
                    treeDto.setPId(spareHouseDto.getBuildId() + ":" + spareHouseDto.getFloorName());
                    treeDto.setLevel(3);
                    treeDto.setDisabled(spareHouseDto.getDisabled());
                    return treeDto;
                }).distinct().collect(Collectors.toList());
    }

    /*************************** 闲置用房树形菜单查询模块end*******************/


    /*************************** 获得单位无穷树start*******************/
    /**
     * 获得单位无穷树
     *
     * @return
     */
    @ApiOperation(value = "获得单位无穷树", notes = "获得单位无穷树", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "/unitTree", method = RequestMethod.POST)
    public List<UnitInfinityTreeDto> getListMenu() {
//        加载全部功能菜单
        List<AllUnitDto> rootlist = unitService.infinityTree();
        List<UnitInfinityTreeDto> getList = new LinkedList<>();
        getMenus(rootlist, null, getList);
        return getList;
    }

    /**
     * 递归获取单位信息
     *
     * @param allMenus
     * @param id
     * @param list
     */
    private void getMenus(List<AllUnitDto> allMenus, Long id, List<UnitInfinityTreeDto> list) {
        long parentId = Optional.ofNullable(id).orElse(0L);
        allMenus.stream().filter(p -> p.getUnitPid() == parentId).forEach(p -> {
            UnitInfinityTreeDto menuDto = dozerBeanMapper.map(p, UnitInfinityTreeDto.class);
            list.add(menuDto);
            getMenus(allMenus, p.getUnitId(), menuDto.getChildrens());
        });
    }

    /*************************** 获得单位无穷树end*******************/


    /*************************** 不含单位的院落、楼栋、楼层、房屋树形菜单查询start*******************/

    /**
     * 不含单位的院落、楼栋、楼层、房屋树形菜单查询(关于处置的)
     *
     * @param treeVo
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "yard/listHandle", method = RequestMethod.POST)
    @ApiOperation(value = "不含单位的院落、楼栋、楼层、房屋树形菜单查询(关于处置的)", notes = "不含单位的院落、楼栋、楼层、房屋树形菜单查询(关于处置的)")
    public List<Map<String, Object>> treeNodeHandle(@RequestBody @Valid TreeVo treeVo) throws Throwable {

        List<Map<String, Object>> list = new ArrayList<>();

        //获取用户可以查看的单位信息
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();

        if (CollectionUtils.isEmpty(unitIds)) {
            return list;
        }
//        查找根节点院落
        if (TreeNode.CAUSE.getValue().equals(treeVo.getType())) {
            List<YardResultVo> yardList = yardService.getYardHandle(unitIds, treeVo.getSearchYardId());
            ShowYard(yardList, list);
        }
        //        查找楼栋节点
        else if (TreeNode.AMT.getValue().equals(treeVo.getType())) {
            List<BuildUnitDto> buildList = buildService.buildUnitHandle(treeVo.getId(), unitIds);
            buildList.forEach(build -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", build.getYardId() + ":" + build.getBuildId());
                map.put("value", build.getBuildId());
                map.put("text", build.getBuildName());
                map.put("type", TreeNode.FLO.getValue());
                map.put("expand", build.getFloorName() > 0);
                map.put("applyStatus", build.getApplyStatus());
                map.put("handleWay", build.getHandleWay());
                map.put("address", build.getYardName() + "-" + build.getBuildName());
                list.add(map);
            });
        }

//        查找用户权限下的所有楼层
        else if (TreeNode.FLO.getValue().equals(treeVo.getType())) {
            List<BuildUnitDto> floorList = buildService.floorListHandle(treeVo.getId(), unitIds);
            floorList.forEach(build -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", build.getYardId() + ":" + build.getBuildId() + ":" + build.getFloorName());
                map.put("value", build.getFloorName());
                map.put("text", build.getFloorName() + "层");
                map.put("type", TreeNode.HOU.getValue());
                map.put("expand", true);
                map.put("handle", build.getApplyStatus());
                map.put("handleWay", build.getHandleWay());
                map.put("address", build.getYardName() + "-" + build.getBuildName() + "-" + build.getFloorName() + "层");
                list.add(map);
            });
        }
//        查找用户权限下的所有房间
        else if (TreeNode.HOU.getValue().equals(treeVo.getType())) {
            List<BuildUnitDto> houseList = buildService.houseListHandle(treeVo.getId(), treeVo.getBuildId(), unitIds);
            houseList.forEach(house -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", house.getHouseId());
                map.put("_id", house.getYardId() + ":" + house.getBuildId() + ":" + house.getFloorName() + ":" + house.getHouseId());
                map.put("value", house.getHouseId());
                map.put("text", house.getHouseNumber());
                map.put("type", TreeNode.END.getValue());
                map.put("expand", false);
                map.put("address", house.getYardName() + "-" + house.getBuildName() + "-" + house.getFloorName() + "层" + "-" + house.getHouseNumber());
                list.add(map);
            });
        }
        return list;
    }

    /**
     * 产权楼栋树
     *
     * @return
     */
    @ApiOperation(value = "产权楼栋树", notes = "产权楼栋树")
    @RequestMapping(value = "/rightBuildTree", method = RequestMethod.POST)
    public List<Map<String, Object>> rightBuildTree() {
        List<Map<String, Object>> mapList = new ArrayList<>();
        List<HandlePositionDto> handlePositionDtoList = buildService.rightBuildTree();
        if (CollectionUtils.isNotEmpty(handlePositionDtoList)) {
            for (HandlePositionDto handlePositionDto : handlePositionDtoList) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", handlePositionDto.getYardName());
                map.put("value", handlePositionDto.getYardId());
                map.put("id", handlePositionDto.getYardId());
                map.put("type", 0);
                if (CollectionUtils.isNotEmpty(handlePositionDto.getBuildList())) {
                    List<Map<String, Object>> childList = new ArrayList<>();
                    for (BuildPositionDto buildPositionDto : handlePositionDto.getBuildList()) {
                        Map<String, Object> childMap = new HashMap<>();
                        childMap.put("name", buildPositionDto.getBuildName());
                        childMap.put("value", buildPositionDto.getBuildId());
                        childMap.put("pid", handlePositionDto.getYardId());
                        childMap.put("id", handlePositionDto.getYardId() + ":" + buildPositionDto.getBuildId());
                        childMap.put("type", 1);
                        if (CollectionUtils.isNotEmpty(buildPositionDto.getFloorNameDtoList())) {
                            List<Map<String, Object>> floorList = new ArrayList<>();
                            for (FloorNameDto floorNameDto : buildPositionDto.getFloorNameDtoList()) {
                                Map<String, Object> floorMap = new HashMap<>();
                                floorMap.put("name", floorNameDto.getFloorName());
                                floorMap.put("value", floorNameDto.getFloorName());
                                floorMap.put("id", handlePositionDto.getYardId() + ":" + buildPositionDto.getBuildId() + ":" + floorNameDto.getFloorName());
                                floorMap.put("pid", buildPositionDto.getBuildId());
                                floorMap.put("type", 2);
                                floorList.add(floorMap);
                            }
                            childMap.put("children", floorList);
                        }
                        childList.add(childMap);
                        map.put("children", childList);
                    }
                }
                mapList.add(map);
            }
        }
        return mapList;
    }

    /**
     * 信息档案楼层树
     *
     * @param buildAndYardVo
     * @return
     */
    @RequestMapping(value = "/messageFloorTree", method = RequestMethod.POST)
    @ApiOperation(value = "信息档案楼层树", notes = "信息档案楼层树")
    public List<Map<String, Object>> messageFloorTree(@RequestBody BuildAndYardVo buildAndYardVo) {
        MessageFloorDto messageFloorDto = houseService.messageFloorTree(buildAndYardVo);
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> childList = new ArrayList<>();
        if (messageFloorDto != null) {
            map.put("name", messageFloorDto.getYardName());
            map.put("value", messageFloorDto.getYardId());
            map.put("id", messageFloorDto.getYardId());
            map.put("type", 0);
            Map<String, Object> buildMap = new HashMap<>();
            List<Map<String, Object>> floorListMap = new ArrayList<>();
            List<Integer> floorList = messageFloorDto.getFloorList();
            if (CollectionUtils.isNotEmpty(floorList)) {
                for (Integer floorName : floorList) {
                    Map<String, Object> floorMap = new HashMap<>();
                    floorMap.put("name", floorName);
                    floorMap.put("value", floorName);
                    floorMap.put("id", messageFloorDto.getYardId() + ":" + messageFloorDto.getBuildId() + ":" + floorName);
                    floorMap.put("pid", messageFloorDto.getBuildId());
                    floorMap.put("type", 2);
                    floorListMap.add(floorMap);
                }
            }
            buildMap.put("name", messageFloorDto.getBuildName());
            buildMap.put("value", messageFloorDto.getBuildId());
            buildMap.put("id", messageFloorDto.getYardId() + ":" + messageFloorDto.getBuildId());
            buildMap.put("pid", messageFloorDto.getYardId());
            buildMap.put("type", 1);
            buildMap.put("children", floorListMap);
            childList.add(buildMap);
            map.put("children", childList);
            listMap.add(map);
        }
        return listMap;
    }


    /**
     * 不含单位的院落、楼栋、楼层、房屋树形菜单查询
     *
     * @param treeVo
     * @return
     * @throws Throwable
     */
    @RequestMapping(value = "yard/contractList", method = RequestMethod.POST)
    @ApiOperation(value = "不含单位的院落、楼栋、楼层、房屋树形菜单查询", notes = "不含单位的院落、楼栋、楼层、房屋树形菜单查询")
    public List<Map<String, Object>> contractTree(@RequestBody @Valid TreeVo treeVo) throws Throwable {
        List<Map<String, Object>> list = new ArrayList<>();
        //获取用户可以查看的单位信息
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();
        if (CollectionUtils.isEmpty(unitIds)) {
            return list;
        }
//        查找根节点院落
        if (TreeNode.CAUSE.getValue().equals(treeVo.getType())) {
            List<YardResultVo> yardList = yardService.getAllByUnitID(unitIds, treeVo.getSearchYardId());
            ShowYard(yardList, list);
        }
//        查找楼栋节点
        else if (TreeNode.AMT.getValue().equals(treeVo.getType())) {
            List<BuildUnitDto> buildList = buildService.buildContract(treeVo.getId(), unitIds);
            buildList.forEach(build -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", build.getYardId() + ":" + build.getBuildId());
                map.put("value", build.getBuildId());
                map.put("text", build.getBuildName());
                map.put("type", TreeNode.FLO.getValue());
                map.put("expand", build.getFloorName() > 0);
                map.put("address", build.getYardName() + "-" + build.getBuildName());
                list.add(map);
            });
        }
//        查找楼层节点
        else if (TreeNode.FLO.getValue().equals(treeVo.getType())) {
            floorNode(treeVo, list, unitIds);
        }
        //        查找用户权限下的所有房间
        else if (TreeNode.HOU.getValue().equals(treeVo.getType())) {
            houseNode(treeVo, list, unitIds);
        }
        return list;
    }

}
