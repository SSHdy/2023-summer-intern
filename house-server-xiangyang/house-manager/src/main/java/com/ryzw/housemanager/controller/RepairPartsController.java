package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.entity.RepairParts;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IRepairPartsService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.RepairPartVo;
import com.ryzw.housemanager.vo.RepairPartsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 建筑物保养部位表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-03-19
 */
@Api(value = "RepairPartsController",description = "维修部位管理")
@Slf4j
@RestController
@RequestMapping("/repairParts")
public class RepairPartsController {
    @Autowired
    private IRepairPartsService repairPartsService;
    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    /**
     * 新增或修改维修部位信息
     * @param repairPartsVo
     * @return
     */
    @RequestMapping(value = "/addOrUpdate",method = RequestMethod.POST)
    @ApiOperation(value = "新增或修改维修部位信息",notes = "新增或修改维修部位信息")
    public boolean addOrUpdate(@RequestBody RepairPartsVo repairPartsVo) {
        RepairParts repairParts = dozerBeanMapper.map(repairPartsVo,RepairParts.class);
        try {
            return repairPartsService.saveOrUpdate(repairParts);
        } catch (Exception ex){
            log.error("error",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }

    /**
     * 分页查询维修部位列表
     * @param repairPartVo
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    @ApiOperation(value = "分页查询维修部位列表",notes = "分页查询维修部位列表")
    public IPage<RepairParts> list(@RequestBody @Valid RepairPartVo repairPartVo) {
        QueryWrapper<RepairParts> wrapper = new QueryWrapper<>();
        if (repairPartVo.getRepairPartsId() != null) {
            wrapper.eq("repair_parts_id", repairPartVo.getRepairPartsId());
        }
        wrapper.eq("is_delete",0);
        wrapper.orderByDesc("repair_parts_id");
        IPage<RepairParts> page = new Page<>();
        page.setCurrent(repairPartVo.getCurrentPage());
        page.setSize(repairPartVo.getPageSize());
        return repairPartsService.page(page,wrapper);
    }

    /**
     * 删除维修部位信息
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除维修部位信息",notes = "删除维修部位信息")
    public boolean delete(@RequestBody BasicVo<Integer> basicVo) {
        return repairPartsService.delete(basicVo);
    }


    /**
     * 维修部位信息列表（无分页）
     * @return
     */
    @ApiOperation(value = "维修部位信息列表（无分页）",notes = "维修部位信息列表（无分页）")
    @RequestMapping(value = "/allList",method =RequestMethod.POST )
    public List<RepairParts> allList(){
        QueryWrapper<RepairParts> yardQueryWrapper= new QueryWrapper<>();
        yardQueryWrapper.eq("is_delete",0);
        yardQueryWrapper.select("repair_parts_id","repair_part_name","repair_part_quality");
        return repairPartsService.list(yardQueryWrapper);
    }

}
