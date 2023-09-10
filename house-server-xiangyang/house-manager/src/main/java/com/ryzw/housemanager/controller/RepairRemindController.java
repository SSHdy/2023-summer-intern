package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.RepairRecordDetailDto;
import com.ryzw.housemanager.dto.RepairRemindDto;
import com.ryzw.housemanager.dto.RepairRemindListDto;
import com.ryzw.housemanager.entity.RemindState;
import com.ryzw.housemanager.entity.RepairDic;
import com.ryzw.housemanager.entity.RepairRemind;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.RepairDicMapper;
import com.ryzw.housemanager.service.IRepairRemindService;
import com.ryzw.housemanager.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 维修提醒管理 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-02-27
 */
@RestController
@Slf4j
@RequestMapping("/repairRemind")
@Api(value = "维修提醒管理", description = "维修提醒管理")
public class RepairRemindController {
    @Autowired
    IRepairRemindService repairRemindService;

    @Autowired
    RepairDicMapper repairDicMapper;


    /**
     * 维修提醒列表
     *
     * @param repairRemindVo
     * @return
     */
    @RequestMapping(value = "/repairRemindList", method = RequestMethod.POST)
    @ApiOperation(value = "维修提醒列表", notes = "维修提醒列表")
    public Page<RepairRemindListDto> repairRemindList(@RequestBody RepairRemindVo repairRemindVo) {

        return repairRemindService.selectRepairRemindList(repairRemindVo);
    }

    /**
     * 新增维修提醒
     *
     * @param repairRemindAddVo
     * @return
     */
    @RequestMapping(value = "/addRepairRemind", method = RequestMethod.POST)
    @ApiOperation(value = "新增维修提醒", notes = "新增维修提醒")
    public boolean addRepairRemind(@RequestBody RepairRemindAddVo repairRemindAddVo) {
        return repairRemindService.addRepairRemind(repairRemindAddVo);
    }

    /**
     * 删除维修提醒
     */
    @RequestMapping(value = "/deleteRepairRemind", method = RequestMethod.POST)
    @ApiOperation(value = "删除维修提醒", notes = "删除维修提醒")
    public boolean deleteRepairRemind(@RequestBody @Valid BasicVo<Long> basicVo) {

        return repairRemindService.removeById(basicVo.getId());
    }

    /**
     * 批量删除维修提醒
     *
     * @param basicIdsVo
     * @return
     */
    @RequestMapping(value = "/batchDeleteRepairRemind", method = RequestMethod.POST)
    @ApiOperation(value = "批量删除维修提醒", notes = "批量删除维修提醒")
    public boolean batchDeleteRepairRemind(@RequestBody @Valid BasicIdsVo<Long> basicIdsVo) {
        return repairRemindService.batchDeleteRepairRemind(basicIdsVo.getIds());
    }

    /**
     * 查询相关维修记录
     *
     * @param repairRemindVo
     * @return
     */
    @RequestMapping(value = "/selectRepairRemind", method = RequestMethod.POST)
    @ApiOperation(value = "查询相关维修记录", notes = "查询相关维修记录")
    public List<RepairRecordDetailDto> selectRepairRemindRecord(@RequestBody RepairRemindVo repairRemindVo) {
        return repairRemindService.selectRepairRemindRecord(repairRemindVo);
    }

    /**
     * 查询维修提醒详情
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/selectRepairRemindDetail", method = RequestMethod.POST)
    @ApiOperation(value = "查询维修提醒详情", notes = "查询维修提醒详情")
    public RepairRemindDto selectRepairRemindDetail(@RequestBody @Valid BasicVo<Long> basicVo) {

        return repairRemindService.selectRepairRemindDetail(basicVo.getId());
    }

    /**
     * 修改维修提醒时间
     *
     * @param updateRemindTimeVo
     * @return
     */
    @RequestMapping(value = "/updateRepairRemind", method = RequestMethod.POST)
    @ApiOperation(value = "修改维修提醒时间", notes = "修改维修提醒时间")
    public boolean updateRepairRemind(@RequestBody @Valid UpdateRemindTimeVo updateRemindTimeVo) {
        try {
            RepairRemind repairRemind = new RepairRemind();
            repairRemind.setRemaindTime(updateRemindTimeVo.getRemaindTime());
            repairRemind.setRepairRemind(updateRemindTimeVo.getRepairRemind());
            repairRemindService.updateById(repairRemind);
            return true;
        } catch (Exception ex) {
            log.error("updateRepairRemindError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改提醒天数
     */
    @RequestMapping(value = "/updateRemindDay", method = RequestMethod.POST)
    @ApiOperation(value = "修改提醒天数", notes = "修改提醒天数")
    public boolean updateRemindDay(@RequestBody @Valid RepairDic repairDic) {

        return repairRemindService.updateRemindDay(repairDic);
    }

    /**
     * 暂不提醒
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/notRemind", method = RequestMethod.POST)
    @ApiOperation(value = "暂不提醒", notes = "暂不提醒")
    public boolean notRemind(@RequestBody @Valid BasicVo<Long> basicVo) {

        return repairRemindService.notRemind(basicVo.getId());
    }


    /**
     * 维修提醒状态修改
     *
     * @param repairRemind
     * @return
     */
    @RequestMapping(value = "/updateState", method = RequestMethod.POST)
    @ApiOperation(value = "维修提醒状态修改", notes = "维修提醒状态修改")
    public boolean updateState(@RequestBody RepairRemind repairRemind) {
        UpdateWrapper<RepairRemind> updateWrapper = new UpdateWrapper<>();
        try {
            if (RemindState.IGNORE.getValue().equals(repairRemind.getRemaindHandle())) {
                return repairRemindService.updateById(repairRemind);
            } else if (RemindState.HANDLE.getValue().equals(repairRemind.getRemaindHandle())) {
                updateWrapper.set("remaind_handle", repairRemind.getRemaindHandle());
                updateWrapper.eq("repair_remind", repairRemind.getRepairRemind());
                repairRemindService.update(repairRemind, updateWrapper);
            }
            return true;
        } catch (Exception ex) {
            log.error("error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 查询维修提醒数量
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/remindNum", method = RequestMethod.POST)
    @ApiOperation(value = "查询维修提醒数量", notes = "查询维修提醒数量")
    public Integer remindNum() {
        return repairRemindService.repairRemindNum();
    }

}
