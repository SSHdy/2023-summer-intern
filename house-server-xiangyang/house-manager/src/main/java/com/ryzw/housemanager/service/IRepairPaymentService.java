package com.ryzw.housemanager.service;

import com.ryzw.housemanager.entity.RepairPayment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.vo.RepairPaymentVo;

import java.util.List;

/**
 * <p>
 * 项目付款表 服务类
 * </p>
 *
 * @author mfl
 * @since 2019-04-02
 */
public interface IRepairPaymentService extends IService<RepairPayment> {

    /**
     * 付款状态修改:1未付款,2付款
     *
     * @param paymentId
     * @return
     */
    boolean completePayment(List<Long> paymentId);

    /**
     * 确认付款
     *
     * @return
     */
    boolean payMent(RepairPaymentVo repairPaymentVo);
}
