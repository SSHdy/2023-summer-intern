package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.BuildDto;
import com.ryzw.housemanager.dto.BuildPositionDto;
import com.ryzw.housemanager.dto.EquipmentBuildDto;
import com.ryzw.housemanager.entity.Build;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IBuildService;
import com.ryzw.housemanager.util.MongoDbUtil;
import com.ryzw.housemanager.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.bson.Document;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 楼座基本信息表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2018-11-16
 */
@RestController
@Slf4j
@Api(value = "楼栋信息管理", description = "楼栋信息管理")
@RequestMapping("/build")
public class BuildController {

    @Autowired
    private IAuthorityUnitService authorityUnitService;
    @Autowired
    private IBuildService buildService;
    @Autowired
    private DozerBeanMapper dozerBeanMapper;
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 楼栋信息列表
     *
     * @param buildListVo
     * @return
     */
    @RequestMapping(value = "list", method = RequestMethod.POST)
    @ApiOperation(value = "楼栋信息列表", notes = "楼栋信息列表")
    public Page<BuildDto> buildList(@RequestBody @Valid BuildListVo buildListVo) {
        return buildService.list(buildListVo);
    }

    /**
     * 楼栋信息详情.
     *
     * @param basicVo
     * @return
     */
    @ApiOperation(value = "楼栋信息详情", notes = "楼栋信息详情")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public Object buildDetails(@RequestBody @Valid BasicVo<Long> basicVo) {
        return buildService.detail(basicVo.getId());
    }

    /**
     * 添加楼栋信息
     *
     * @param buildVo
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加楼栋信息", notes = "添加楼栋信息")
    public boolean add(@RequestBody @Valid BuildVo buildVo) {
        Build build = dozerBeanMapper.map(buildVo, Build.class);
        buildService.addBuild(build, buildVo.getNewFloorImgUrlList(), buildVo.getOutRent(), buildVo.getFloorDrawingList());
        return true;
    }

    /**
     * 修改楼栋信息
     *
     * @param buildVo
     * @return
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ApiOperation(value = "修改楼栋信息", notes = "修改楼栋信息")
    public boolean update(@RequestBody BuildVo buildVo) {
        Build build = dozerBeanMapper.map(buildVo, Build.class);
        return buildService.updateFloorImgByBuild(build, buildVo);
    }

    /**
     * 删除楼座信息，判断楼座下面是否有房屋，有就返回fasle，没有返回true，并删除楼座
     *
     * @param build
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除楼栋信息", notes = "删除楼栋信息，如果楼栋下面有房屋信息，则不能删除")
    public boolean delete(@RequestBody Build build) {
        return buildService.deleteBuildAndFloorImg(build);
    }


    /**
     * 根据院落id楼座id查询对应的重大设备信息
     */
    @RequestMapping(value = "selectBuildAndEquipment", method = RequestMethod.POST)
    @ApiOperation(value = "根据院落id楼座id查询对应的重大设备信息", notes = "根据院落id楼座id查询对应的重大设备信息")
    public List<EquipmentBuildDto> selectBuildAndEquipment(@RequestBody BuildVo buildVo) {
        return buildService.selectBuildAndEquipment(buildVo);
    }

    /**
     * 查询某个楼栋下的单位信息和人员职称信息
     */
    @RequestMapping(value = "unitPersonInfo", method = RequestMethod.POST)
    @ApiOperation(value = "查询某个楼栋下的单位信息和人员职称信息", notes = "查询某个楼栋下的单位信息和人员职称信息")
    public Map<Object, Object> unitPersonInfo(@RequestBody @Valid BasicVo<Long> basicVo) {
        return buildService.unitPersonInfo(basicVo);
    }

    /**
     * 用户权限下的楼栋位置信息查询
     *
     * @param buildPositionVo
     * @return
     */
    @ApiOperation(value = "用户权限下的楼栋位置信息查询", notes = "用户权限下的楼栋位置信息查询")
    @RequestMapping(value = "/position", method = RequestMethod.POST)
    public List<BuildPositionDto> position(@RequestBody @Valid BuildPositionVo buildPositionVo) {
        List<BuildPositionDto> buildPositionDtos = new ArrayList<>();

        //获取用户可以查看的单位信息
        List<Long> pUnitIds = authorityUnitService.getUnitIdByToken();

        if (CollectionUtils.isNotEmpty(buildPositionVo.getUnitIds())) {
            pUnitIds.retainAll(buildPositionVo.getUnitIds());
        }
        if (CollectionUtils.isNotEmpty(pUnitIds)) {
            buildPositionDtos = buildService.buildPosition(buildPositionVo.getYardId(), pUnitIds);
        }
        return buildPositionDtos;
    }

    /**
     * 删除楼栋信息，如果楼栋下面有房屋信息，则不能删除
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "unitNameList", method = RequestMethod.POST)
    @ApiOperation(value = "删除楼栋信息", notes = "删除楼栋信息，如果楼栋下面有房屋信息，则不能删除")
    public List<String> unitNameList(@RequestBody @Valid BasicVo<Long> basicVo) {
        return buildService.unitNameList(basicVo);
    }

    /**
     * 查询楼栋产权信息
     *
     * @param buildRightVo
     * @return
     */
    @RequestMapping(value = "buildRight", method = RequestMethod.POST)
    @ApiOperation(value = "查询楼栋产权信息", notes = "查询楼栋产权信息")
    public List<Document> buildRight(@RequestBody @Valid BuildRightVo buildRightVo) {
        Query query = new Query();
        Criteria criteria = Criteria.where("buildNum").in(buildRightVo.getBuildId()).and("propertyType").is(buildRightVo.getType());
        query.addCriteria(criteria);
//        query.fields().include("buildNum")
//                .include("buildArea");
        List<Document> documents = mongoTemplate.find(query, Document.class, "propertyRight");
        List<Document> newItems = documents.stream().map(p ->
        {
            p.replace("_id", p.getObjectId("_id").toString());
            return p;
        }).collect(Collectors.toList());
        List<Document> documentList = MongoDbUtil.DocumentToMap(documents).stream().allMatch(MapUtils::isEmpty) ? null : newItems;
        return documentList;
    }


    /**
     * 判断楼层集合中是否存在房间，如果有，就不能减少楼栋的楼层
     *
     * @param floorNameVo
     * @return
     */
    @RequestMapping(value = "noHouseCharNull", method = RequestMethod.POST)
    @ApiOperation(value = "判断楼层集合中是否存在房间，如果有，就不能减少楼栋的楼层", notes = "判断楼层集合中是否存在房间，如果有，就不能减少楼栋的楼层")
    public boolean noHouseCharNull(@RequestBody FloorNameVo floorNameVo) {
        return buildService.noHouseCharNull(floorNameVo);
    }


}
