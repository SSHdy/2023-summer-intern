package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ryzw.housemanager.entity.PayMentState;
import com.ryzw.housemanager.entity.RepairPayment;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.RepairPaymentMapper;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IRepairPaymentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.vo.RepairPaymentVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 项目付款表 服务实现类
 * </p>
 *
 * @author mfl
 * @since 2019-04-02
 */
@Service
@Slf4j
public class RepairPaymentServiceImpl extends ServiceImpl<RepairPaymentMapper, RepairPayment> implements IRepairPaymentService {

    @Autowired
    private RepairPaymentMapper repairPaymentMapper;
    @Autowired
    IAuthorityUnitService authorityUnitService;
    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    /**
     * 付款状态修改:1未付款,2付款中，3已付款
     *
     * @param ids
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean completePayment(List<Long> ids) {
        try {
            List<RepairPayment> repairPayment = repairPaymentMapper.selectBatchIds(ids);
            if(CollectionUtils.isNotEmpty(repairPayment) && repairPayment.stream().map(RepairPayment::getRepairPaymentState).allMatch(PayMentState.PAY.getValue()::equals)){
                RepairPayment payment = new RepairPayment();
                UpdateWrapper<RepairPayment> updateWrapper = new UpdateWrapper<>();
                updateWrapper.in("repair_payment_id",ids);
//                updateWrapper.set("repair_payment_state",PayMentState.PAYEND.getValue());

                payment.setRepairPaymentState(PayMentState.PAYEND.getValue());
                repairPaymentMapper.update(payment,updateWrapper);
                return true;
            }
            throw new ServiceException("项目未发起付款或已付款!");
        } catch (Exception ex) {
            log.error("repairPayment/updateState error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 确认付款
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean payMent(RepairPaymentVo repairPaymentVo) {
        try {
            RepairPayment repairPayment = dozerBeanMapper.map(repairPaymentVo, RepairPayment.class);
            List<Long> repairPaymentId = repairPaymentVo.getRepairPaymentId();

            QueryWrapper<RepairPayment> selectwrapper = new QueryWrapper<>();
            selectwrapper.select("repair_payment_id", "repair_apply_id", "repair_payment_state");
            selectwrapper.in("repair_payment_id", repairPaymentId);
            List<RepairPayment> result = repairPaymentMapper.selectList(selectwrapper);

            if (CollectionUtils.isEmpty(result) ||
                    result.stream().anyMatch(p -> !(PayMentState.NOTPAY.getValue().equals(p.getRepairPaymentState())) && p.getRepairPaymentState() != null)) {
                throw new ServiceException("已确认付款，请勿重复操作！");
            }

            Map<String, Object> maps = authorityUnitService.getUserByToken();
            if (MapUtils.isNotEmpty(maps)) {
                UpdateWrapper<RepairPayment> wrapper = new UpdateWrapper<>();
                wrapper.in("repair_payment_id", repairPaymentId);
                wrapper.eq("repair_apply_id", repairPayment.getRepairApplyId());
                if (StringUtils.isNotEmpty(String.valueOf(maps.get("user_id")))) {
                    repairPayment.setRepairPaymentUserid(Long.valueOf(String.valueOf(maps.get("user_id"))));
                }
                repairPayment.setRepairPaymentUsername(String.valueOf(maps.get("username")));
                repairPayment.setRepairPaymentState(PayMentState.PAYEND.getValue());
                repairPayment.setRepairPaymentTime(new Date());
                repairPaymentMapper.update(repairPayment, wrapper);
            }
            return true;
        } catch (Exception ex) {
            log.error("payMentError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}
