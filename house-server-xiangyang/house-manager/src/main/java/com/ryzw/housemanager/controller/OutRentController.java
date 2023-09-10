package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.OutRentDto;
import com.ryzw.housemanager.entity.OutRent;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IOutRentService;
import com.ryzw.housemanager.vo.BasicIdsVo;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.OutRentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 出租用房信息表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-05-05
 */
@RestController
@RequestMapping("/housemanager/out-rent")
@Api(value = "出租用房信息管理", description = "出租用房信息管理")
@Slf4j
public class OutRentController {

    @Autowired
    IOutRentService outRentService;


    /**
     * 添加出租用房信息
     *
     * @param outRent
     * @return
     */
    @RequestMapping(value = "/addOutRent", method = RequestMethod.POST)
    @ApiOperation(value = "添加出租用房信息", notes = "添加出租用房信息")
    public boolean addOutRent(@RequestBody @Valid OutRent outRent) {
        try {
            outRentService.save(outRent);
            return true;
        } catch (Exception ex) {
            log.error("addOutRentError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询出租用房信息列表
     *
     * @param outRentVo
     * @return
     */
    @RequestMapping(value = "/selectOutRentList", method = RequestMethod.POST)
    @ApiOperation(value = "查询出租用房信息列表", notes = "查询出租用房信息列表")
    public Page<OutRentDto> selectOutRentList(@RequestBody OutRentVo outRentVo) {
        return outRentService.selectOutRentList(outRentVo);
    }

    /**
     * 删除出租用房信息
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/deleteOutRent", method = RequestMethod.POST)
    @ApiOperation(value = "删除出租用房信息", notes = "删除出租用房信息")
    public boolean deleteOutRent(@RequestBody @Valid BasicVo<Long> basicVo) {
        try {
            return outRentService.removeById(basicVo.getId());
        } catch (Exception ex) {
            log.error("deleteOutRentError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 批量删除出租用房信息
     *
     * @param basicIdsVo
     * @return
     */
    @RequestMapping(value = "/batchDeleteOutRent", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除出租用房信息", notes = "批量删除出租用房信息")
    public boolean batchDeleteOutRent(@RequestBody @Valid BasicIdsVo<Long> basicIdsVo) {
        try {
            return outRentService.removeByIds(basicIdsVo.getIds());
        } catch (Exception ex) {
            log.error("batchDeleteOutRentError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改出租用房信息
     *
     * @param outRent
     * @return
     */
    @RequestMapping(value = "/updateOutRent", method = RequestMethod.POST)
    @ApiOperation(value = "修改出租用房信息", notes = "修改出租用房信息")
    public boolean updateOutRent(@RequestBody @Valid OutRent outRent) {
        try {
            return outRentService.updateById(outRent);
        } catch (Exception ex) {
            log.error("updateOutRentError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

}
