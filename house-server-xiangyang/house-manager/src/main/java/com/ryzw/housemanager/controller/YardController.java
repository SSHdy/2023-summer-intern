package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.*;
import com.ryzw.housemanager.entity.Build;
import com.ryzw.housemanager.entity.HousingUse;
import com.ryzw.housemanager.entity.Yard;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IBuildService;
import com.ryzw.housemanager.service.IHousingUseService;
import com.ryzw.housemanager.service.IYardService;
import com.ryzw.housemanager.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 院落信息表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2018-11-15
 */
@RestController
@RequestMapping("/yard")
@Slf4j
@Api(value = "YardController", description = "院落信息列表")
public class YardController {
    @Autowired
    private IAuthorityUnitService authorityUnitService;
    @Autowired
    private IYardService yardService;
    @Autowired
    private IBuildService buildService;
    @Autowired
    private IHousingUseService housingUseService;

    /**
     * 院落信息列表
     *
     * @param yard
     * @return
     */
    @ApiOperation(value = "院落信息列表", notes = "院落下面绑定楼栋")
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public Page<Yard> list(@RequestBody YardVo yard) {
        return yardService.list(yard);
    }

    /**
     * 所有院落信息列表
     *
     * @return
     */
    @ApiOperation(value = "所有院落信息列表", notes = "院落下面绑定楼栋")
    @RequestMapping(value = "allList", method = RequestMethod.POST)
    public List<Yard> yardInfo(@RequestBody BasicVo<Long> basicVo) {
        QueryWrapper<Yard> yardQueryWrapper = new QueryWrapper<>();
        if (basicVo.getId() != null) {
            yardQueryWrapper.eq("type", basicVo.getId());
        }
        yardQueryWrapper.select("yard_id", "yard_name", "yard_position", "yard_longitude", "yard_latitude", "region_id", "yard_administrative_region");
        return yardService.list(yardQueryWrapper);
    }

    /**
     * 用户权限下的院落信息列表
     *
     * @return
     */
    @ApiOperation(value = "用户权限下的院落信息列表", notes = "用户权限下的院落信息列表")
    @RequestMapping(value = "authorityYard", method = RequestMethod.POST)
    public List<YardPositionDto> authorityYard() {
        List<YardPositionDto> yardPosition = new ArrayList<>();
        //获取用户可以查看的单位信息
        List<Long> unitIds = authorityUnitService.getUnitIdByToken();
        if (CollectionUtils.isNotEmpty(unitIds)) {
            yardPosition = yardService.yardPosition(unitIds);
        }
        return yardPosition;
    }

    /**
     * 添加院落信息
     *
     * @param yardAddVo
     * @return
     */
    @RequestMapping(value = "addYard", method = RequestMethod.POST)
    @ApiOperation(value = "添加院落信息", notes = "添加院落信息")
    public boolean addYard(@RequestBody @Valid YardAddVo yardAddVo) {
        return yardService.addYard(yardAddVo);
    }

    /**
     * 修改院落信息
     *
     * @param yardAddVo
     * @return
     */
    @RequestMapping(value = "updateYard", method = RequestMethod.POST)
    @ApiOperation(value = "修改院落信息", notes = "修改院落信息")
    public boolean updateYard(@RequestBody @Valid YardAddVo yardAddVo) {
        return yardService.updateYard(yardAddVo);
    }

    /**
     * 删除院落信息
     *
     * @param yard
     * @return
     */
    @RequestMapping(value = "deleteByid", method = RequestMethod.POST)
    @ApiOperation(value = "删除院落信息", notes = "如果院落下面绑定了楼栋，则不能删除")
    public boolean deleteYard(@RequestBody @Valid BasicVo<Long> yard) {
        return yardService.deleteYard(yard.getId());
    }

    /**
     * 查询单个院落信息
     *
     * @return
     */
    @ApiOperation(value = "查询单个院落信息", notes = "查询单个院落信息")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public YardDetailDto getOne(@RequestBody @Valid BasicVo<Long> basicVo) {
        return yardService.yardDetail(basicVo);
    }


    /**
     * 查询院落关联的楼座
     *
     * @param basicVo
     * @return
     */
    @ApiOperation(value = "院落下的楼座经纬度", notes = "院落下的楼座经纬度")
    @RequestMapping(value = "/yardBuilds", method = RequestMethod.POST)
    public YardBuildDto yardBuilds(@RequestBody @Valid BasicVo<Long> basicVo) {
        return yardService.yardBuilds(basicVo);
    }

    /**
     * 查询院落经纬度
     *
     * @param positionVo
     * @return
     */
    @ApiOperation(value = "查询院落经纬度", notes = "查询院落经纬度")
    @RequestMapping(value = "/position", method = RequestMethod.POST)
    public List<YardPositionDto> position(@RequestBody @Valid PositionVo positionVo) {
        List<YardPositionDto> yardPosition = new ArrayList<>();
        //获取用户可以查看的单位信息
        List<Long> pUnitIds = authorityUnitService.getUnitIdByToken();
        if (CollectionUtils.isNotEmpty(positionVo.getUnitIds())) {
            pUnitIds.retainAll(positionVo.getUnitIds());
        }
        if (CollectionUtils.isNotEmpty(pUnitIds)) {
            yardPosition = yardService.yardPosition(pUnitIds);
        }
        return yardPosition;
    }

    /**
     * 根据院落编号查询物业关系
     *
     * @param basicVo
     * @return
     */
    @ApiOperation(value = "根据院落编号查询物业关系", notes = "根据院落编号查询物业关系")
    @RequestMapping(value = "/selectEstate", method = RequestMethod.POST)
    public YardEstateDto selectEstate(@RequestBody @Valid BasicVo<Long> basicVo) {
        return yardService.selectEstate(basicVo.getId());
    }


    /**
     * 判断院落名称是否重复
     *
     * @param yardRepetitionVo
     * @return
     */
    @ApiOperation(value = "判断院落名称是否重复", notes = "判断院落名称是否重复")
    @RequestMapping(value = "/isRepetition", method = RequestMethod.POST)
    public boolean isRepetition(@RequestBody YardRepetitionVo yardRepetitionVo) {
        Long yardId = yardRepetitionVo.getYardId();
        QueryWrapper<Yard> wrapper = new QueryWrapper<>();
        wrapper = wrapper.eq("yard_name", yardRepetitionVo.getYardName());
        int count = (yardId == null) ? yardService.count(wrapper) : yardService.count(wrapper.ne("yard_id", yardId));
        if (count > 0) {
            throw new ServiceException("登录名重复");
        }
        return true;
    }

    /**
     * 判断院落下是否有楼栋
     *
     * @param yardRepetitionVo
     * @return
     */
    @ApiOperation(value = "判断院落下是否有楼栋", notes = "判断院落下是否有楼栋")
    @RequestMapping(value = "/isHaveBuild", method = RequestMethod.POST)
    public boolean isHaveBuild(@RequestBody YardRepetitionVo yardRepetitionVo) {
        Long yardId = yardRepetitionVo.getYardId();
        Integer buildCount = buildService.count(new QueryWrapper<Build>().eq("yard_id", yardId));
        if (buildCount > 0) {
            throw new ServiceException("该院落下挂有楼栋,无法更换");
        }
        return true;
    }

    /**
     * 判断院落/土地下是否有出租出借
     *
     * @param yardRepetitionVo
     * @return
     */
    @ApiOperation(value = "判断院落/土地下是否有出租出借", notes = "判断院落/土地下是否有出租出借")
    @RequestMapping(value = "/isHaveRent", method = RequestMethod.POST)
    public boolean isHaveRent(@RequestBody YardRepetitionVo yardRepetitionVo) {
        Long yardId = yardRepetitionVo.getYardId();
        Integer handleYardCount = yardService.isHaveRent(yardId);
        if (handleYardCount > 0) {
            throw new ServiceException("该院落/土地下有出租出借,无法修改");
        }
        return true;
    }


    /**
     * 查询一个院落下的楼栋信息列表
     *
     * @return
     */
    @ApiOperation(value = "查询一个院落下的楼栋信息列表", notes = "查询一个院落下的楼栋信息列表")
    @RequestMapping(value = "buildList", method = RequestMethod.POST)
    public List<Build> buildList(@RequestBody BasicVo<Long> basicVo) {
        QueryWrapper<Build> wrapper = new QueryWrapper<>();
        wrapper.select("build_id", "build_name");
        wrapper.eq("yard_id",basicVo.getId());
        return buildService.list(wrapper);
    }



    /**
     * 市（州、盟）办公用房报表信息查询
     * @return
     */
    @ApiOperation(value = "市（州、盟）办公用房报表信息查询", notes = "市（州、盟）办公用房报表信息查询")
    @RequestMapping(value = "/yardUnitReport", method = RequestMethod.POST)
    public List<YardUnitProfessionalDto> yardUnitReport() {
        return yardService.yardUnitReport();
    }

}
