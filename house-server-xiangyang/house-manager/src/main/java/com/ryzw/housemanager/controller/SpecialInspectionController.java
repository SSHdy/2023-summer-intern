package com.ryzw.housemanager.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryzw.housemanager.dto.SpecialDetailDto;
import com.ryzw.housemanager.dto.SpecialListDto;
import com.ryzw.housemanager.entity.SpecialInspection;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.ISpecialInspectionService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.ReformVo;
import com.ryzw.housemanager.vo.SpecialInspectionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * <p>
 * 专项巡检表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-10-08
 */
@Slf4j
@Api(value = "SpecialInspectionController",description = "专项巡检信息管理")
@RestController
@RequestMapping("/specialInspection")
public class SpecialInspectionController {
    @Autowired
    private ISpecialInspectionService specialInspectionService;

    /**
     * 新增或修改专项巡检信息
     * @param specialInspection
     * @return
     */
    @RequestMapping(value = "addOrUpdate",method = RequestMethod.POST)
    @ApiOperation(value = "新增或修改专项巡检信息",notes = "新增或修改专项巡检信息")
    public boolean addOrUpdate(@RequestBody SpecialInspection specialInspection){
        try{
            return specialInspectionService.saveOrUpdate(specialInspection);
        }catch (Exception ex){
            log.error("addOrUpdate error",ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 整改专项巡检信息
     * @param reformVo
     * @return
     */
    @RequestMapping(value = "reform",method = RequestMethod.POST)
    @ApiOperation(value = "整改专项巡检信息",notes = "整改专项巡检信息")
    public boolean reform(@RequestBody ReformVo reformVo){
        try{
            if (reformVo.getSpecialInspectionId() != null ){
                SpecialInspection specialInspection = new SpecialInspection();
                UpdateWrapper<SpecialInspection> updateWrapper = new UpdateWrapper<>();
                updateWrapper.eq("special_inspection_id",reformVo.getSpecialInspectionId());
                specialInspection.setSpecialInspectionRectifyState(reformVo.getSpecialInspectionRectifyState());
                specialInspection.setSpecialInspectionRectifyResult(reformVo.getSpecialInspectionRectifyResult());
                specialInspectionService.update(specialInspection,updateWrapper);
                return true;
            }
            throw new ServiceException("修改数据为空！");
        }catch (Exception ex){
            log.error("addOrUpdate error",ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 删除一条专项巡检信息
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除一条专项巡检信息",notes = "删除一条专项巡检信息")
    public boolean delete(@RequestBody BasicVo<Long> basicVo) {
        try {
            return specialInspectionService.removeById(basicVo.getId());
        } catch (Exception ex){
            log.error("deleteError",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }


    /**
     * 批量删除专项巡检信息
     * @param ids
     * @return
     */
    @RequestMapping(value = "/batchDelete",method = RequestMethod.POST)
    @ApiOperation(value = "批量删除专项巡检信息",notes = "批量删除专项巡检信息")
    public boolean deleteBatchPro(@RequestBody List<Long> ids) {
        try {
            return specialInspectionService.removeByIds(ids);
        } catch (Exception ex){
            log.error("batchDeleteError",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }

    /**
     * 专项巡检信息列表
     * @return
     */
    @RequestMapping(value = "/getList",method = RequestMethod.POST)
    @ApiOperation(value = "专项巡检信息列表",notes = "专项巡检信息列表")
    public IPage<SpecialListDto> getList(@RequestBody SpecialInspectionVo specialInspectionVo) {
        return specialInspectionService.specialList(specialInspectionVo);
    }

    /**
     * 专项检查详情
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/specialDetail",method = RequestMethod.POST)
    @ApiOperation(value = "专项检查详情",notes = "专项检查详情")
    public SpecialDetailDto specialDetail(@RequestBody BasicVo<Long> basicVo){
        return specialInspectionService.specialDetail(basicVo);
    }

}
