package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryzw.housemanager.entity.RepairPayment;
import com.ryzw.housemanager.service.IRepairPaymentService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.RepairPaymentVo;
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
 * 项目付款表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-04-02
 */
@RestController
@Api(value = "RepairPaymentController", description = "项目付款管理")
@Slf4j
@RequestMapping("/repairPayment")
public class RepairPaymentController {
    @Autowired
    private IRepairPaymentService repairPaymentService;

    /**
     * 付款详情
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation(value = "付款详情", notes = "付款详情")
    public List<RepairPayment> detail(@RequestBody BasicVo<Long> basicVo) {
        QueryWrapper<RepairPayment> wrapper = new QueryWrapper<>();
        wrapper.eq("repair_apply_id", basicVo.getId());
        return repairPaymentService.list(wrapper);
    }

    /**
     * 完成付款(1未付款，2付款中，3已付款)
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/completePayment", method = RequestMethod.POST)
    @ApiOperation(value = "完成付款(1未付款，2付款中，3已付款)", notes = "完成付款(1未付款，2付款中，3已付款)")
    public boolean completePayment(@RequestBody List<Long> ids) {
        return repairPaymentService.completePayment(ids);
    }

    /**
     * 确认付款，付款状态(1未付款，2付款中，3已付款)
     *
     * @param repairPaymentVo
     * @return
     */
    @RequestMapping(value = "/payMent", method = RequestMethod.POST)
    @ApiOperation(value = "确认付款，付款状态(1未付款，2付款中，3已付款)", notes = "确认付款，付款状态(1未付款，2付款中，3已付款)")
    public boolean payMent(@RequestBody RepairPaymentVo repairPaymentVo) {
        return repairPaymentService.payMent(repairPaymentVo);
    }
}
