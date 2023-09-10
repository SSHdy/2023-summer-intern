package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.entity.RepairDic;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IRepairDicService;
import com.ryzw.housemanager.vo.RepairDicVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 维修字典表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-08-08
 */
@Api(value = "ProfessionalController",description = "维修字典关系管理")
@Slf4j
@RestController
@RequestMapping("/repairDic")
public class RepairDicController {
    @Autowired
    private IRepairDicService repairDicService;

    /**
     * 新增或修改维修字典关系
     * @param repairDic
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate",method = RequestMethod.POST)
    @ApiOperation(value = "新增或修改维修字典关系",notes = "新增或修改维修字典关系")
    public boolean addPro(@RequestBody RepairDic repairDic) {
        try {
            return repairDicService.saveOrUpdate(repairDic);
        } catch (Exception ex){
            log.error("addProError",ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 维修字典列表
     * @param repairDicVo
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "维修字典列表",notes = "维修字典列表")
    public IPage<RepairDic> getList(@RequestBody @Valid RepairDicVo repairDicVo) {
        try {
            QueryWrapper<RepairDic> wrapper = new QueryWrapper<>();
            if (StringUtils.isNotBlank(repairDicVo.getRepairDicKey())) {
                wrapper.like("repair_dic_key", repairDicVo.getRepairDicKey());
            }
            wrapper.notIn("repair_dic_id",3,6);
            wrapper.orderByDesc("repair_dic_id");
            IPage<RepairDic> page = new Page<>();
            page.setCurrent(repairDicVo.getCurrentPage());
            page.setSize(repairDicVo.getPageSize());
            return repairDicService.page(page,wrapper);
        }catch (Exception ex){
            log.error("repairDic/List Error",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }

}
