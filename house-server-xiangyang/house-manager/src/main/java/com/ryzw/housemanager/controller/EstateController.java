package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.entity.Estate;
import com.ryzw.housemanager.entity.Professional;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IEstateService;
import com.ryzw.housemanager.vo.BasicIdsVo;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.EstateVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 物业信息表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-05-05
 */
@RestController
@RequestMapping("/housemanager/estate")
@Api(value = "物业信息管理", description = "物业信息管理")
@Slf4j
public class EstateController {

    @Autowired
    IEstateService estateService;

    /**
     * 添加物业信息
     *
     * @param estate
     * @return
     */
    @RequestMapping(value = "/addEstate", method = RequestMethod.POST)
    @ApiOperation(value = "添加物业信息", notes = "添加物业信息")
    public boolean addEstate(@RequestBody @Valid Estate estate) {
        try {
            return estateService.save(estate);
        } catch (Exception ex) {
            log.error("addEstateError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询物业信息列表
     *
     * @param estateVo
     * @return
     */
    @RequestMapping(value = "/selectEstateList", method = RequestMethod.POST)
    @ApiOperation(value = "查询物业信息列表", notes = "查询物业信息列表")
    public Page<Estate> selectEstateList(@RequestBody EstateVo estateVo) {
        return estateService.selectEstateList(estateVo);

    }

    /**
     * 删除物业信息
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/deleteEstate", method = RequestMethod.POST)
    @ApiOperation(value = "删除物业信息", notes = "删除物业信息")
    public boolean deleteEstate(@RequestBody @Valid BasicVo<Long> basicVo) {
        try {
            return estateService.deleteEstate(basicVo.getId());
        } catch (Exception ex) {
            log.error("deleteEstateError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 批量删除物业信息
     *
     * @param basicIdsVo
     * @return
     */
    @RequestMapping(value = "/batchDeleteEstate", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除物业信息", notes = "批量删除物业信息")
    public boolean batchDeleteEstate(@RequestBody @Valid BasicIdsVo<Long> basicIdsVo) {
        try {
            return estateService.removeByIds(basicIdsVo.getIds());
        } catch (Exception ex) {
            log.error("batchDeleteEstateError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改物业信息
     *
     * @param estate
     * @return
     */
    @RequestMapping(value = "/updateEstate", method = RequestMethod.POST)
    @ApiOperation(value = "修改物业信息", notes = "修改物业信息")
    public boolean updateEstate(@RequestBody Estate estate) {
        try {
            return estateService.updateById(estate);
        } catch (Exception ex) {
            log.error("updateEstateError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 物业名称列表
     * @return
     */
    @RequestMapping(value = "/getList",method = RequestMethod.POST)
    @ApiOperation(value = "物业名称列表",notes = "物业名称列表")
    public List<Estate> getList() {
        QueryWrapper<Estate> wrapper = new QueryWrapper<>();
        wrapper.select("estate_id","estate_name");
        wrapper.orderByDesc("estate_id");
        return estateService.list(wrapper);
    }
}
