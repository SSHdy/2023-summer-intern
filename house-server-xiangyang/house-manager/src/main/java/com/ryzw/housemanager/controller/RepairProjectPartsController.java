package com.ryzw.housemanager.controller;


import com.ryzw.housemanager.dto.RepairManageDto;
import com.ryzw.housemanager.entity.RepairItems;
import com.ryzw.housemanager.service.IRepairProjectPartsService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.RepairManageUpdateVo;
import com.ryzw.housemanager.vo.RepairMangeAddVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 项目管理施工部位表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-03-29
 */
@RestController
@RequestMapping("/housemanager/repair-project-parts")
@Api(value = "施工管理", description = "施工管理")
public class RepairProjectPartsController {


    @Autowired
    IRepairProjectPartsService repairProjectPartsService;


    /**
     * 查询施工日志信息
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "repairLog", method = RequestMethod.POST)
    @ApiOperation(value = "查询施工日志信息", notes = "查询施工日志信息")
    public RepairManageDto repairLog(@RequestBody @Valid BasicVo<Long> basicVo) {
        return repairProjectPartsService.selectRepairLogDetail(basicVo.getId());
    }

    /**
     * 查询增减项信息
     */
    @RequestMapping(value = "selectRepairItem", method = RequestMethod.POST)
    @ApiOperation(value = "查询增减项信息", notes = "查询增减项信息")
    public List<RepairItems> selectRepairItem(@RequestBody @Valid BasicVo<Long> basicVo) {
        return repairProjectPartsService.selectRepairItem(basicVo.getId());
    }

    /**
     * 新增施工日志
     *
     * @param repairMangeAddVo
     * @return
     */
    @RequestMapping(value = "addRepairManage", method = RequestMethod.POST)
    @ApiOperation(value = "新增施工日志", notes = "新增施工日志")
    public boolean addRepairManage(@RequestBody RepairMangeAddVo repairMangeAddVo) {
        return repairProjectPartsService.addRepairManage(repairMangeAddVo);
    }


    /**
     * 修改施工日志信息
     *
     * @param repairManageUpdateVo
     * @return
     */
    @RequestMapping(value = "repairManageUpdate", method = RequestMethod.POST)
    @ApiOperation(value = "修改施工日志信息", notes = "修改施工日志信息")
    public boolean repairManageUpdate(@RequestBody @Valid RepairManageUpdateVo repairManageUpdateVo) {
        return repairProjectPartsService.updateRepairManage(repairManageUpdateVo);
    }


}
