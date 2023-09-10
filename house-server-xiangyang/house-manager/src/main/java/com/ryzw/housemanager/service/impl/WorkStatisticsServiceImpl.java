package com.ryzw.housemanager.service.impl;

import com.ryzw.housemanager.dto.ConfigureStatisticsDto;
import com.ryzw.housemanager.dto.DateRangeDto;
import com.ryzw.housemanager.entity.*;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.ConfigureMapper;
import com.ryzw.housemanager.mapper.HandleMapper;
import com.ryzw.housemanager.mapper.RepairApplyMapper;
import com.ryzw.housemanager.service.IAuthorityUnitService;
import com.ryzw.housemanager.service.IWorkStatisticsService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.WorkStatisticsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * </p>
 *
 * @author wj
 * @since 2019-07-24 0024
 */
@Service
@Slf4j
public class WorkStatisticsServiceImpl implements IWorkStatisticsService {

    @Autowired
    private RepairApplyMapper repairApplyMapper;

    @Autowired
    private ConfigureMapper configureMapper;

    @Autowired
    private HandleMapper handleMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private IAuthorityUnitService authorityUnitService;

    private static SimpleDateFormat formatMonth = new SimpleDateFormat("yyyy-MM");
    private static SimpleDateFormat formatYear = new SimpleDateFormat("yyyy");

    /**
     * 查询维修工作统计(按月)
     *
     * @param workStatisticsVo
     * @return
     */
    @Override
    public List<RepairWorkStatistics> selectRepairMothCount(WorkStatisticsVo workStatisticsVo) {
        try {
            List<RepairWorkStatistics> repairWorkStatisticsList = new ArrayList<>();

            //获取用户可以查看的单位信息
            List<Long> unitIds = authorityUnitService.getUnitIdByToken();
            workStatisticsVo.setUnitIdList(unitIds);

            Date repairApplyDate;
            if (StringUtils.isNotBlank(workStatisticsVo.getApplyDateMonth())) {
                repairApplyDate = this.strToDate(workStatisticsVo.getApplyDateMonth(), null);
            } else {
                throw new ServiceException("申请时间(月)不能为空！");
            }

            String repairApplyYearOld;
            String repairApplyMonthOld;
            Calendar calendar = Calendar.getInstance();
            if (repairApplyDate != null) {
                calendar.setTime(repairApplyDate);
                calendar.add(Calendar.YEAR, -1);
                Date repairApplyDateOld = calendar.getTime();
                repairApplyYearOld = this.dateToStr(repairApplyDateOld, null);
                workStatisticsVo.setApplyDateMonth(repairApplyYearOld);
            }
            //得到去年减去年份对应月的相关数据 零星维修
            workStatisticsVo.setStep(StepNode.POPROJECTS.getValue());
            RepairWorkStatistics repairWorkStatisticsYear1 = repairApplyMapper.selectRepairCount(workStatisticsVo);

            //得到去年减去年份对应月的相关数据 申请单位自费维修
            workStatisticsVo.setStep(StepNode.UDAMAINTENANCE.getValue());
            RepairWorkStatistics repairWorkStatisticsYear2 = repairApplyMapper.selectRepairCount(workStatisticsVo);

            //得到去年减去年份对应月的相关数据 机关管理局费用维修
            workStatisticsVo.setStep(StepNode.QWPERIOD.getValue());
            RepairWorkStatistics repairWorkStatisticsYear3 = repairApplyMapper.selectRepairCount(workStatisticsVo);


            if (repairApplyDate != null) {
                calendar.setTime(repairApplyDate);
                calendar.add(Calendar.MONTH, -1);
                Date repairApplyDateMonth = calendar.getTime();
                repairApplyMonthOld = this.dateToStr(repairApplyDateMonth, null);
                workStatisticsVo.setApplyDateMonth(repairApplyMonthOld);
            }
            //得到上个月的相关数据 零星维修
            workStatisticsVo.setStep(StepNode.POPROJECTS.getValue());
            RepairWorkStatistics repairWorkStatisticsMonth1 = repairApplyMapper.selectRepairCount(workStatisticsVo);
            //得到上个月的相关数据 申请单位自费维修
            workStatisticsVo.setStep(StepNode.UDAMAINTENANCE.getValue());
            RepairWorkStatistics repairWorkStatisticsMonth2 = repairApplyMapper.selectRepairCount(workStatisticsVo);
            //得到上个月的相关数据 机关管理局费用维修
            workStatisticsVo.setStep(StepNode.QWPERIOD.getValue());
            RepairWorkStatistics repairWorkStatisticsMonth3 = repairApplyMapper.selectRepairCount(workStatisticsVo);


            //零星维修统计
            workStatisticsVo.setStep(StepNode.POPROJECTS.getValue());
            if (repairApplyDate != null) {
                workStatisticsVo.setApplyDateMonth(this.dateToStr(repairApplyDate, null));
            }

            RepairWorkStatistics repairWorkStatistics1 = repairApplyMapper.selectRepairCount(workStatisticsVo);
            repairWorkStatistics1.setRepairType(1);

            //求零星维修统计同比
            if (repairWorkStatistics1 != null && repairWorkStatisticsYear1 != null) {
                //审批数量同比
                if (repairWorkStatistics1.getAuditCount() != null && repairWorkStatisticsYear1.getAuditCount() != null) {
                    repairWorkStatistics1.setAuditCountTb(repairWorkStatistics1.getAuditCount() - repairWorkStatisticsYear1.getAuditCount());
                }
                //维修面积同比
                if (repairWorkStatistics1.getRepairModuleArea() != null && repairWorkStatisticsYear1.getRepairModuleArea() != null) {
                    repairWorkStatistics1.setRepairModuleAreaTb(repairWorkStatistics1.getRepairModuleArea().subtract(repairWorkStatisticsYear1.getRepairModuleArea()));
                }
                //维修金额同比
                if (repairWorkStatistics1.getRepairPaymentMoney() != null && repairWorkStatisticsYear1.getRepairPaymentMoney() != null) {
                    repairWorkStatistics1.setRepairPaymentMoneyTb(repairWorkStatistics1.getRepairPaymentMoney().subtract(repairWorkStatisticsYear1.getRepairPaymentMoney()));
                }
            }

            //求零星维修统计环比
            if (repairWorkStatistics1 != null && repairWorkStatisticsMonth1 != null) {
                //审批数量环比
                if (repairWorkStatistics1.getAuditCount() != null && repairWorkStatisticsMonth1.getAuditCount() != null) {
                    repairWorkStatistics1.setAuditCountHb(repairWorkStatistics1.getAuditCount() - repairWorkStatisticsMonth1.getAuditCount());
                }
                //维修面积环比
                if (repairWorkStatistics1.getRepairModuleArea() != null && repairWorkStatisticsMonth1.getRepairModuleArea() != null) {
                    repairWorkStatistics1.setRepairModuleAreaHb(repairWorkStatistics1.getRepairModuleArea().subtract(repairWorkStatisticsMonth1.getRepairModuleArea()));
                }
                //维修金额环比
                if (repairWorkStatistics1.getRepairPaymentMoney() != null && repairWorkStatisticsMonth1.getRepairPaymentMoney() != null) {
                    repairWorkStatistics1.setRepairPaymentMoneyHb(repairWorkStatistics1.getRepairPaymentMoney().subtract(repairWorkStatisticsMonth1.getRepairPaymentMoney()));
                }
            }
            repairWorkStatisticsList.add(repairWorkStatistics1);


            //申请单位自费统计
            workStatisticsVo.setStep(StepNode.UDAMAINTENANCE.getValue());
            if (repairApplyDate != null) {
                workStatisticsVo.setApplyDateMonth(this.dateToStr(repairApplyDate, null));
            }
            RepairWorkStatistics repairWorkStatistics2 = repairApplyMapper.selectRepairCount(workStatisticsVo);
            repairWorkStatistics2.setRepairType(2);

            //求申请单位自费统计同比
            if (repairWorkStatistics2 != null && repairWorkStatisticsYear2 != null) {
                //审批数量同比
                if (repairWorkStatistics2.getAuditCount() != null && repairWorkStatisticsYear2.getAuditCount() != null) {
                    repairWorkStatistics2.setAuditCountTb(repairWorkStatistics2.getAuditCount() - repairWorkStatisticsYear2.getAuditCount());
                }
                //维修面积同比
                if (repairWorkStatistics2.getRepairModuleArea() != null && repairWorkStatisticsYear2.getRepairModuleArea() != null) {
                    repairWorkStatistics2.setRepairModuleAreaTb(repairWorkStatistics2.getRepairModuleArea().subtract(repairWorkStatisticsYear2.getRepairModuleArea()));
                }
                //维修金额同比
                if (repairWorkStatistics2.getRepairPaymentMoney() != null && repairWorkStatisticsYear2.getRepairPaymentMoney() != null) {
                    repairWorkStatistics2.setRepairPaymentMoneyTb(repairWorkStatistics2.getRepairPaymentMoney().subtract(repairWorkStatisticsYear2.getRepairPaymentMoney()));
                }
            }

            //求申请单位自费统计环比
            if (repairWorkStatistics2 != null && repairWorkStatisticsMonth2 != null) {
                //审批数量环比
                if (repairWorkStatistics2.getAuditCount() != null && repairWorkStatisticsMonth2.getAuditCount() != null) {
                    repairWorkStatistics2.setAuditCountHb(repairWorkStatistics2.getAuditCount() - repairWorkStatisticsMonth2.getAuditCount());
                }
                //维修面积环比
                if (repairWorkStatistics2.getRepairModuleArea() != null && repairWorkStatisticsMonth2.getRepairModuleArea() != null) {
                    repairWorkStatistics2.setRepairModuleAreaHb(repairWorkStatistics2.getRepairModuleArea().subtract(repairWorkStatisticsMonth2.getRepairModuleArea()));
                }
                //维修金额环比
                if (repairWorkStatistics2.getRepairPaymentMoney() != null && repairWorkStatisticsMonth2.getRepairPaymentMoney() != null) {
                    repairWorkStatistics2.setRepairPaymentMoneyHb(repairWorkStatistics2.getRepairPaymentMoney().subtract(repairWorkStatisticsMonth2.getRepairPaymentMoney()));
                }
            }
            repairWorkStatisticsList.add(repairWorkStatistics2);


            //机关管理局费用维修统计
            workStatisticsVo.setStep(StepNode.QWPERIOD.getValue());
            if (repairApplyDate != null) {
                workStatisticsVo.setApplyDateMonth(this.dateToStr(repairApplyDate, null));
            }
            RepairWorkStatistics repairWorkStatistics3 = repairApplyMapper.selectRepairCount(workStatisticsVo);
            repairWorkStatistics3.setRepairType(3);

            //求机关管理局费用维修同比
            if (repairWorkStatistics3 != null && repairWorkStatisticsYear3 != null) {
                //审批数量同比
                if (repairWorkStatistics3.getAuditCount() != null && repairWorkStatisticsYear3.getAuditCount() != null) {
                    repairWorkStatistics3.setAuditCountTb(repairWorkStatistics3.getAuditCount() - repairWorkStatisticsYear3.getAuditCount());
                }
                //维修面积同比
                if (repairWorkStatistics3.getRepairModuleArea() != null && repairWorkStatisticsYear3.getRepairModuleArea() != null) {
                    repairWorkStatistics3.setRepairModuleAreaTb(repairWorkStatistics3.getRepairModuleArea().subtract(repairWorkStatisticsYear3.getRepairModuleArea()));
                }
                //维修金额同比
                if (repairWorkStatistics3.getRepairPaymentMoney() != null && repairWorkStatisticsYear3.getRepairPaymentMoney() != null) {
                    repairWorkStatistics3.setRepairPaymentMoneyTb(repairWorkStatistics3.getRepairPaymentMoney().subtract(repairWorkStatisticsYear3.getRepairPaymentMoney()));
                }
            }

            //求机关管理局费用维修环比
            if (repairWorkStatistics3 != null && repairWorkStatisticsMonth3 != null) {
                //审批数量环比
                if (repairWorkStatistics3.getAuditCount() != null && repairWorkStatisticsMonth3.getAuditCount() != null) {
                    repairWorkStatistics3.setAuditCountHb(repairWorkStatistics3.getAuditCount() - repairWorkStatisticsMonth3.getAuditCount());
                }
                //维修金额环比
                if (repairWorkStatistics3.getRepairModuleArea() != null && repairWorkStatisticsMonth3.getRepairModuleArea() != null) {
                    repairWorkStatistics3.setRepairModuleAreaHb(repairWorkStatistics3.getRepairModuleArea().subtract(repairWorkStatisticsMonth3.getRepairModuleArea()));
                }
                //维修金额环比
                if (repairWorkStatistics3.getRepairPaymentMoney() != null && repairWorkStatisticsMonth3.getRepairPaymentMoney() != null) {
                    repairWorkStatistics3.setRepairPaymentMoneyHb(repairWorkStatistics3.getRepairPaymentMoney().subtract(repairWorkStatisticsMonth3.getRepairPaymentMoney()));
                }
            }
            repairWorkStatisticsList.add(repairWorkStatistics3);


            RepairWorkStatistics repairWorkStatistics4 = new RepairWorkStatistics();
            repairWorkStatistics4.setRepairType(4);
            //求审批数量总和
            repairWorkStatistics4.setAuditCount(repairWorkStatistics1.getAuditCount() + repairWorkStatistics2.getAuditCount()
                    + repairWorkStatistics3.getAuditCount());

            //求审批数量同比总和
            repairWorkStatistics4.setAuditCountTb(repairWorkStatistics1.getAuditCountTb() + repairWorkStatistics2.getAuditCountTb()
                    + repairWorkStatistics3.getAuditCountTb());

            //求审批数量环比总和
            repairWorkStatistics4.setAuditCountHb(repairWorkStatistics1.getAuditCountHb() + repairWorkStatistics2.getAuditCountHb()
                    + repairWorkStatistics3.getAuditCountHb());


            //求面积总和
            BigDecimal repairModuleAreaSum = new BigDecimal(0.00);
            if (repairWorkStatistics1.getRepairModuleArea() != null) {
                repairModuleAreaSum = repairModuleAreaSum.add(repairWorkStatistics1.getRepairModuleArea());
            }
            if (repairWorkStatistics2.getRepairModuleArea() != null) {
                repairModuleAreaSum = repairModuleAreaSum.add(repairWorkStatistics2.getRepairModuleArea());
            }
            if (repairWorkStatistics3.getRepairModuleArea() != null) {
                repairModuleAreaSum = repairModuleAreaSum.add(repairWorkStatistics3.getRepairModuleArea());
            }
            repairWorkStatistics4.setRepairModuleArea(repairModuleAreaSum);

            //求面积同比总和
            BigDecimal repairModuleAreaTbSum = new BigDecimal(0.00);
            if (repairWorkStatistics1.getRepairModuleAreaTb() != null) {
                repairModuleAreaTbSum = repairModuleAreaTbSum.add(repairWorkStatistics1.getRepairModuleAreaTb());
            }
            if (repairWorkStatistics2.getRepairModuleAreaTb() != null) {
                repairModuleAreaTbSum = repairModuleAreaTbSum.add(repairWorkStatistics2.getRepairModuleAreaTb());
            }
            if (repairWorkStatistics3.getRepairModuleAreaTb() != null) {
                repairModuleAreaTbSum = repairModuleAreaTbSum.add(repairWorkStatistics3.getRepairModuleAreaTb());
            }
            repairWorkStatistics4.setRepairModuleAreaTb(repairModuleAreaTbSum);

            //求面积环比总和
            BigDecimal repairModuleAreaHbSum = new BigDecimal(0.00);
            if (repairWorkStatistics1.getRepairModuleAreaHb() != null) {
                repairModuleAreaHbSum = repairModuleAreaHbSum.add(repairWorkStatistics1.getRepairModuleAreaHb());
            }
            if (repairWorkStatistics2.getRepairModuleAreaHb() != null) {
                repairModuleAreaHbSum = repairModuleAreaHbSum.add(repairWorkStatistics2.getRepairModuleAreaHb());
            }
            if (repairWorkStatistics3.getRepairModuleAreaHb() != null) {
                repairModuleAreaHbSum = repairModuleAreaHbSum.add(repairWorkStatistics3.getRepairModuleAreaHb());
            }
            repairWorkStatistics4.setRepairModuleAreaHb(repairModuleAreaHbSum);


            //求金额总和
            BigDecimal repairPaymentMoneySum = new BigDecimal(0.00);
            if (repairWorkStatistics1.getRepairPaymentMoney() != null) {
                repairPaymentMoneySum = repairPaymentMoneySum.add(repairWorkStatistics1.getRepairPaymentMoney());
            }
            if (repairWorkStatistics2.getRepairPaymentMoney() != null) {
                repairPaymentMoneySum = repairPaymentMoneySum.add(repairWorkStatistics2.getRepairPaymentMoney());
            }
            if (repairWorkStatistics3.getRepairPaymentMoney() != null) {
                repairPaymentMoneySum = repairPaymentMoneySum.add(repairWorkStatistics3.getRepairPaymentMoney());
            }
            repairWorkStatistics4.setRepairPaymentMoney(repairPaymentMoneySum);

            //求金额同比总和
            BigDecimal repairPaymentMoneyTbSum = new BigDecimal(0.00);
            if (repairWorkStatistics1.getRepairPaymentMoneyTb() != null) {
                repairPaymentMoneyTbSum = repairPaymentMoneyTbSum.add(repairWorkStatistics1.getRepairPaymentMoneyTb());
            }
            if (repairWorkStatistics2.getRepairPaymentMoneyTb() != null) {
                repairPaymentMoneyTbSum = repairPaymentMoneyTbSum.add(repairWorkStatistics2.getRepairPaymentMoneyTb());
            }
            if (repairWorkStatistics3.getRepairPaymentMoneyTb() != null) {
                repairPaymentMoneyTbSum = repairPaymentMoneyTbSum.add(repairWorkStatistics3.getRepairPaymentMoneyTb());
            }
            repairWorkStatistics4.setRepairPaymentMoneyTb(repairPaymentMoneyTbSum);

            //求金额环比总和
            BigDecimal repairPaymentMoneyHbSum = new BigDecimal(0.00);
            if (repairWorkStatistics1.getRepairPaymentMoneyHb() != null) {
                repairPaymentMoneyHbSum = repairPaymentMoneyHbSum.add(repairWorkStatistics1.getRepairPaymentMoneyHb());
            }
            if (repairWorkStatistics2.getRepairPaymentMoneyHb() != null) {
                repairPaymentMoneyHbSum = repairPaymentMoneyHbSum.add(repairWorkStatistics2.getRepairPaymentMoneyHb());
            }
            if (repairWorkStatistics3.getRepairPaymentMoneyHb() != null) {
                repairPaymentMoneyHbSum = repairPaymentMoneyHbSum.add(repairWorkStatistics3.getRepairPaymentMoneyHb());
            }
            repairWorkStatistics4.setRepairPaymentMoneyHb(repairPaymentMoneyHbSum);

            repairWorkStatisticsList.add(repairWorkStatistics4);

            return repairWorkStatisticsList;
        } catch (Exception ex) {
            log.error("selectRepairMothCountError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询维修工作统计(按年)
     *
     * @param workStatisticsVo
     * @return
     */
    @Override
    public List<RepairWorkStatistics> selectRepairYearCount(WorkStatisticsVo workStatisticsVo) {
        try {
            List<RepairWorkStatistics> repairWorkStatisticsList = new ArrayList<>();

            //获取用户可以查看的单位信息
            List<Long> unitIds = authorityUnitService.getUnitIdByToken();
            workStatisticsVo.setUnitIdList(unitIds);

            Date repairApplyDate;
            if (StringUtils.isNotBlank(workStatisticsVo.getApplyDateYear())) {
                repairApplyDate = this.strToDate(null, workStatisticsVo.getApplyDateYear());
            } else {
                throw new ServiceException("申请时间(年)不能为空！");
            }

            String repairApplyYearOld;
            Calendar calendar = Calendar.getInstance();
            if (repairApplyDate != null) {
                calendar.setTime(repairApplyDate);
                calendar.add(Calendar.YEAR, -1);
                Date repairApplyDateOld = calendar.getTime();
                repairApplyYearOld = this.dateToStr(null, repairApplyDateOld);
                workStatisticsVo.setApplyDateYear(repairApplyYearOld);
            }
            //得到去年减去年份对应月的相关数据 零星维修
            workStatisticsVo.setStep(StepNode.POPROJECTS.getValue());
            RepairWorkStatistics repairWorkStatisticsYear1 = repairApplyMapper.selectRepairCount(workStatisticsVo);

            //得到去年减去年份对应月的相关数据 申请单位自费维修
            workStatisticsVo.setStep(StepNode.UDAMAINTENANCE.getValue());
            RepairWorkStatistics repairWorkStatisticsYear2 = repairApplyMapper.selectRepairCount(workStatisticsVo);

            //得到去年减去年份对应月的相关数据 机关管理局费用维修
            workStatisticsVo.setStep(StepNode.QWPERIOD.getValue());
            RepairWorkStatistics repairWorkStatisticsYear3 = repairApplyMapper.selectRepairCount(workStatisticsVo);


            //零星维修统计
            workStatisticsVo.setStep(StepNode.POPROJECTS.getValue());
            if (repairApplyDate != null) {
                workStatisticsVo.setApplyDateYear(this.dateToStr(null, repairApplyDate));
            }

            RepairWorkStatistics repairWorkStatistics1 = repairApplyMapper.selectRepairCount(workStatisticsVo);
            repairWorkStatistics1.setRepairType(1);

            //求零星维修统计环比
            if (repairWorkStatistics1 != null && repairWorkStatisticsYear1 != null) {
                //审批数量环比
                if (repairWorkStatistics1.getAuditCount() != null && repairWorkStatisticsYear1.getAuditCount() != null) {
                    repairWorkStatistics1.setAuditCountHb(repairWorkStatistics1.getAuditCount() - repairWorkStatisticsYear1.getAuditCount());
                }
                //维修面积环比
                if (repairWorkStatistics1.getRepairModuleArea() != null && repairWorkStatisticsYear1.getRepairModuleArea() != null) {
                    repairWorkStatistics1.setRepairModuleAreaHb(repairWorkStatistics1.getRepairModuleArea().subtract(repairWorkStatisticsYear1.getRepairModuleArea()));
                }
                //维修金额环比
                if (repairWorkStatistics1.getRepairPaymentMoney() != null && repairWorkStatisticsYear1.getRepairPaymentMoney() != null) {
                    repairWorkStatistics1.setRepairPaymentMoneyHb(repairWorkStatistics1.getRepairPaymentMoney().subtract(repairWorkStatisticsYear1.getRepairPaymentMoney()));
                }
            }

            repairWorkStatisticsList.add(repairWorkStatistics1);


            //申请单位自费统计
            workStatisticsVo.setStep(StepNode.UDAMAINTENANCE.getValue());
            if (repairApplyDate != null) {
                workStatisticsVo.setApplyDateYear(this.dateToStr(null, repairApplyDate));
            }
            RepairWorkStatistics repairWorkStatistics2 = repairApplyMapper.selectRepairCount(workStatisticsVo);
            repairWorkStatistics2.setRepairType(2);

            //求申请单位自费维修统计环比
            if (repairWorkStatistics2 != null && repairWorkStatisticsYear2 != null) {
                //审批数量环比
                if (repairWorkStatistics2.getAuditCount() != null && repairWorkStatisticsYear2.getAuditCount() != null) {
                    repairWorkStatistics2.setAuditCountHb(repairWorkStatistics2.getAuditCount() - repairWorkStatisticsYear2.getAuditCount());
                }
                //维修面积环比
                if (repairWorkStatistics2.getRepairModuleArea() != null && repairWorkStatisticsYear2.getRepairModuleArea() != null) {
                    repairWorkStatistics2.setRepairModuleAreaHb(repairWorkStatistics2.getRepairModuleArea().subtract(repairWorkStatisticsYear2.getRepairModuleArea()));
                }
                //维修金额环比
                if (repairWorkStatistics2.getRepairPaymentMoney() != null && repairWorkStatisticsYear2.getRepairPaymentMoney() != null) {
                    repairWorkStatistics2.setRepairPaymentMoneyHb(repairWorkStatistics2.getRepairPaymentMoney().subtract(repairWorkStatisticsYear2.getRepairPaymentMoney()));
                }
            }
            repairWorkStatisticsList.add(repairWorkStatistics2);


            //机关管理局费用维修统计
            workStatisticsVo.setStep(StepNode.QWPERIOD.getValue());
            if (repairApplyDate != null) {
                workStatisticsVo.setApplyDateYear(this.dateToStr(null, repairApplyDate));
            }
            RepairWorkStatistics repairWorkStatistics3 = repairApplyMapper.selectRepairCount(workStatisticsVo);
            repairWorkStatistics3.setRepairType(3);

            //求机关管理局费用维修环比
            if (repairWorkStatistics3 != null && repairWorkStatisticsYear3 != null) {
                //审批数量环比
                if (repairWorkStatistics3.getAuditCount() != null && repairWorkStatisticsYear3.getAuditCount() != null) {
                    repairWorkStatistics3.setAuditCountHb(repairWorkStatistics3.getAuditCount() - repairWorkStatisticsYear3.getAuditCount());
                }
                //维修面积环比
                if (repairWorkStatistics3.getRepairModuleArea() != null && repairWorkStatisticsYear3.getRepairModuleArea() != null) {
                    repairWorkStatistics3.setRepairModuleAreaHb(repairWorkStatistics3.getRepairModuleArea().subtract(repairWorkStatisticsYear3.getRepairModuleArea()));
                }
                //维修金额环比
                if (repairWorkStatistics3.getRepairPaymentMoney() != null && repairWorkStatisticsYear3.getRepairPaymentMoney() != null) {
                    repairWorkStatistics3.setRepairPaymentMoneyHb(repairWorkStatistics3.getRepairPaymentMoney().subtract(repairWorkStatisticsYear3.getRepairPaymentMoney()));
                }
            }
            repairWorkStatisticsList.add(repairWorkStatistics3);


            RepairWorkStatistics repairWorkStatistics4 = new RepairWorkStatistics();
            repairWorkStatistics4.setRepairType(4);
            //求审批数量总和
            repairWorkStatistics4.setAuditCount(repairWorkStatistics1.getAuditCount() + repairWorkStatistics2.getAuditCount()
                    + repairWorkStatistics3.getAuditCount());


            //求审批数量环比总和
            repairWorkStatistics4.setAuditCountHb(repairWorkStatistics1.getAuditCountHb() + repairWorkStatistics2.getAuditCountHb()
                    + repairWorkStatistics3.getAuditCountHb());


            //求面积总和
            BigDecimal repairModuleAreaSum = new BigDecimal(0.00);
            if (repairWorkStatistics1.getRepairModuleArea() != null) {
                repairModuleAreaSum = repairModuleAreaSum.add(repairWorkStatistics1.getRepairModuleArea());
            }
            if (repairWorkStatistics2.getRepairModuleArea() != null) {
                repairModuleAreaSum = repairModuleAreaSum.add(repairWorkStatistics2.getRepairModuleArea());
            }
            if (repairWorkStatistics3.getRepairModuleArea() != null) {
                repairModuleAreaSum = repairModuleAreaSum.add(repairWorkStatistics3.getRepairModuleArea());
            }
            repairWorkStatistics4.setRepairModuleArea(repairModuleAreaSum);

            //求面积环比总和
            BigDecimal repairModuleAreaHbSum = new BigDecimal(0.00);
            if (repairWorkStatistics1.getRepairModuleAreaHb() != null) {
                repairModuleAreaHbSum = repairModuleAreaHbSum.add(repairWorkStatistics1.getRepairModuleAreaHb());
            }
            if (repairWorkStatistics2.getRepairModuleAreaHb() != null) {
                repairModuleAreaHbSum = repairModuleAreaHbSum.add(repairWorkStatistics2.getRepairModuleAreaHb());
            }
            if (repairWorkStatistics3.getRepairModuleAreaHb() != null) {
                repairModuleAreaHbSum = repairModuleAreaHbSum.add(repairWorkStatistics3.getRepairModuleAreaHb());
            }
            repairWorkStatistics4.setRepairModuleAreaHb(repairModuleAreaHbSum);


            //求金额总和
            BigDecimal repairPaymentMoneySum = new BigDecimal(0.00);
            if (repairWorkStatistics1.getRepairPaymentMoney() != null) {
                repairPaymentMoneySum = repairPaymentMoneySum.add(repairWorkStatistics1.getRepairPaymentMoney());
            }
            if (repairWorkStatistics2.getRepairPaymentMoney() != null) {
                repairPaymentMoneySum = repairPaymentMoneySum.add(repairWorkStatistics2.getRepairPaymentMoney());
            }
            if (repairWorkStatistics3.getRepairPaymentMoney() != null) {
                repairPaymentMoneySum = repairPaymentMoneySum.add(repairWorkStatistics3.getRepairPaymentMoney());
            }
            repairWorkStatistics4.setRepairPaymentMoney(repairPaymentMoneySum);

            //求金额环比总和
            BigDecimal repairPaymentMoneyHbSum = new BigDecimal(0.00);
            if (repairWorkStatistics1.getRepairPaymentMoneyHb() != null) {
                repairPaymentMoneyHbSum = repairPaymentMoneyHbSum.add(repairWorkStatistics1.getRepairPaymentMoneyHb());
            }
            if (repairWorkStatistics2.getRepairPaymentMoneyHb() != null) {
                repairPaymentMoneyHbSum = repairPaymentMoneyHbSum.add(repairWorkStatistics2.getRepairPaymentMoneyHb());
            }
            if (repairWorkStatistics3.getRepairPaymentMoneyHb() != null) {
                repairPaymentMoneyHbSum = repairPaymentMoneyHbSum.add(repairWorkStatistics3.getRepairPaymentMoneyHb());
            }
            repairWorkStatistics4.setRepairPaymentMoneyHb(repairPaymentMoneyHbSum);

            repairWorkStatisticsList.add(repairWorkStatistics4);

            return repairWorkStatisticsList;
        } catch (Exception ex) {
            log.error("selectRepairYearCountError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询配置工作统计(按月)
     *
     * @param workStatisticsVo
     * @return
     */
    @Override
    public List<ConfigureStatisticsDto> configureStatisticsByMonth(WorkStatisticsVo workStatisticsVo) {
        try {


            if (StringUtils.isEmpty(workStatisticsVo.getApplyDateMonth())) {
                throw new ServiceException("申请时间(月)不能为空！");
            }
            //获取用户可以查看的单位信息
            List<Long> unitIds = authorityUnitService.getUnitIdByToken();
            workStatisticsVo.setUnitIdList(unitIds);
            List<ConfigureStatisticsDto> list = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            //1.查询调剂本月的相关数据

            workStatisticsVo.setStep(1);
            ConfigureStatisticsDto tj1 = configureMapper.configureStatistics(workStatisticsVo);
            //获取本月调剂面积
            BigDecimal tj1Area = configureMapper.getTjArea(workStatisticsVo);
            tj1.setApprovalArea(tj1Area);
            tj1.setAllocationPlan(1);

            //查询置换本月的相关数据
            BigDecimal approvalMoneyzh1 = new BigDecimal(0.00);
            BigDecimal approvalAreazh1 = new BigDecimal(0.00);
            workStatisticsVo.setStep(2);
            ConfigureStatisticsDto zh1 = configureMapper.configureStatistics(workStatisticsVo);
            zh1.setAllocationPlan(2);
            //查询mongo中数据
            List<Map<String, String>> mapListzh1 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListzh1)) {
                for (Map<String, String> map : mapListzh1) {
                    if (map.containsKey("makeMoney")) {
                        String makeMoney = map.get("makeMoney");
                        if (StringUtils.isNotEmpty(makeMoney)) {
                            approvalMoneyzh1 = approvalMoneyzh1.add(new BigDecimal(makeMoney));
                        }
                    }
                    if (map.containsKey("profit")) {
                        String profit = map.get("profit");
                        if (StringUtils.isNotEmpty(profit)) {
                            approvalMoneyzh1 = approvalMoneyzh1.add(new BigDecimal(profit));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreazh1 = approvalAreazh1.add(new BigDecimal(area));
                        }
                    }
                }
            }
            zh1.setApprovalMoney(approvalMoneyzh1);
            zh1.setApprovalArea(approvalAreazh1);

            //查询租用本月的相关数据
            BigDecimal approvalMoneyzy1 = new BigDecimal(0.00);
            BigDecimal approvalAreazy1 = new BigDecimal(0.00);
            workStatisticsVo.setStep(3);
            ConfigureStatisticsDto zy1 = configureMapper.configureStatistics(workStatisticsVo);
            zy1.setAllocationPlan(3);
            //查询mongo中数据
            List<Map<String, String>> mapListzy1 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListzy1)) {
                for (Map<String, String> map : mapListzy1) {
                    if (map.containsKey("rent")) {
                        String rent = map.get("rent");
                        if (StringUtils.isNotEmpty(rent)) {
                            approvalMoneyzy1 = approvalMoneyzy1.add(new BigDecimal(rent));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreazy1 = approvalAreazy1.add(new BigDecimal(area));
                        }
                    }
                }
            }
            zy1.setApprovalMoney(approvalMoneyzy1);
            zy1.setApprovalArea(approvalAreazy1);

            //查询建设本月的相关数据
            BigDecimal approvalMoneyjs1 = new BigDecimal(0.00);
            BigDecimal approvalAreajs1 = new BigDecimal(0.00);
            workStatisticsVo.setStep(4);
            ConfigureStatisticsDto js1 = configureMapper.configureStatistics(workStatisticsVo);
            js1.setAllocationPlan(4);
            //查询mongo中数据
            List<Map<String, String>> mapListjs1 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListjs1)) {
                for (Map<String, String> map : mapListjs1) {
                    if (map.containsKey("expenditure")) {
                        String expenditure = map.get("expenditure");
                        if (StringUtils.isNotEmpty(expenditure)) {
                            approvalMoneyjs1 = approvalMoneyjs1.add(new BigDecimal(expenditure));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreajs1 = approvalAreajs1.add(new BigDecimal(area));
                        }
                    }
                }
            }
            js1.setApprovalMoney(approvalMoneyjs1);
            js1.setApprovalArea(approvalAreajs1);

            //2.查询调剂上月的相关数据
            Date date1 = this.strToDate(workStatisticsVo.getApplyDateMonth(), null);
            calendar.setTime(date1);
            calendar.add(Calendar.MONTH, -1);
            Date lastMonth = calendar.getTime();
            //获取上月
            String lastMonthStr = this.dateToStr(lastMonth, null);
            workStatisticsVo.setApplyDateMonth(lastMonthStr);
            workStatisticsVo.setStep(1);
            ConfigureStatisticsDto tj2 = configureMapper.configureStatistics(workStatisticsVo);
            //获取上月调剂面积
            BigDecimal tj2Area = configureMapper.getTjArea(workStatisticsVo);
            tj2.setApprovalArea(tj2Area);

            //查询置换上月的相关数据
            BigDecimal approvalMoneyzh2 = new BigDecimal(0.00);
            BigDecimal approvalAreazh2 = new BigDecimal(0.00);
            workStatisticsVo.setStep(2);
            ConfigureStatisticsDto zh2 = configureMapper.configureStatistics(workStatisticsVo);
            //查询mongo中数据
            List<Map<String, String>> mapListzh2 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListzh2)) {
                for (Map<String, String> map : mapListzh2) {
                    if (map.containsKey("makeMoney")) {
                        String makeMoney = map.get("makeMoney");
                        if (StringUtils.isNotEmpty(makeMoney)) {
                            approvalMoneyzh2 = approvalMoneyzh2.add(new BigDecimal(makeMoney));
                        }
                    }
                    if (map.containsKey("profit")) {
                        String profit = map.get("profit");
                        if (StringUtils.isNotEmpty(profit)) {
                            approvalMoneyzh2 = approvalMoneyzh2.add(new BigDecimal(profit));
                        }

                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreazh2 = approvalAreazh2.add(new BigDecimal(area));
                        }
                    }
                }
            }
            zh2.setApprovalMoney(approvalMoneyzh2);
            zh2.setApprovalArea(approvalAreazh2);


            //查询租用上月的相关数据
            BigDecimal approvalMoneyzy2 = new BigDecimal(0.00);
            BigDecimal approvalAreazy2 = new BigDecimal(0.00);
            workStatisticsVo.setStep(3);
            ConfigureStatisticsDto zy2 = configureMapper.configureStatistics(workStatisticsVo);
            //查询mongo中数据
            List<Map<String, String>> mapListzy2 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListzy2)) {
                for (Map<String, String> map : mapListzy2) {
                    if (map.containsKey("rent")) {
                        String rent = map.get("rent");
                        if (StringUtils.isNotEmpty(rent)) {
                            approvalMoneyzy2 = approvalMoneyzy2.add(new BigDecimal(rent));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreazy2 = approvalAreazy2.add(new BigDecimal(area));
                        }
                    }
                }
            }
            zy2.setApprovalMoney(approvalMoneyzy2);
            zy2.setApprovalArea(approvalAreazy2);

            //查询建设上月的相关数据
            BigDecimal approvalMoneyjs2 = new BigDecimal(0.00);
            BigDecimal approvalAreajs2 = new BigDecimal(0.00);
            workStatisticsVo.setStep(4);
            ConfigureStatisticsDto js2 = configureMapper.configureStatistics(workStatisticsVo);
            //查询mongo中数据
            List<Map<String, String>> mapListjs2 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListjs2)) {
                for (Map<String, String> map : mapListjs2) {
                    if (map.containsKey("expenditure")) {
                        String expenditure = map.get("expenditure");
                        if (StringUtils.isNotEmpty(expenditure)) {
                            approvalMoneyjs2 = approvalMoneyjs2.add(new BigDecimal(expenditure));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreajs2 = approvalAreajs2.add(new BigDecimal(area));
                        }
                    }
                }
            }
            js2.setApprovalMoney(approvalMoneyjs2);
            js2.setApprovalArea(approvalAreajs2);


            //3.查询调剂去年本月的相关数据

            Date date2 = this.strToDate(workStatisticsVo.getApplyDateMonth(), null);
            calendar.setTime(date2);
            calendar.add(Calendar.YEAR, -1);
            calendar.add(Calendar.MONTH, 1);
            Date lastYear = calendar.getTime();
            //获取去年
            String lastYearStr = this.dateToStr(lastYear, null);
            workStatisticsVo.setApplyDateMonth(lastYearStr);
            workStatisticsVo.setStep(1);
            ConfigureStatisticsDto tj3 = configureMapper.configureStatistics(workStatisticsVo);
            //获取去年本月调剂面积
            BigDecimal tj3Area = configureMapper.getTjArea(workStatisticsVo);
            tj3.setApprovalArea(tj3Area);

            //查询置换去年本月的相关数据
            BigDecimal approvalMoneyzh3 = new BigDecimal(0.00);
            BigDecimal approvalAreazh3 = new BigDecimal(0.00);
            workStatisticsVo.setStep(2);
            ConfigureStatisticsDto zh3 = configureMapper.configureStatistics(workStatisticsVo);
            //查询mongo中数据
            List<Map<String, String>> mapListzh3 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListzh3)) {
                for (Map<String, String> map : mapListzh3) {
                    if (map.containsKey("makeMoney")) {
                        String makeMoney = map.get("makeMoney");
                        if (StringUtils.isNotEmpty(makeMoney)) {
                            approvalMoneyzh3 = approvalMoneyzh3.add(new BigDecimal(makeMoney));
                        }
                    }
                    if (map.containsKey("profit")) {
                        String profit = map.get("profit");
                        if (StringUtils.isNotEmpty(profit)) {
                            approvalMoneyzh3 = approvalMoneyzh3.add(new BigDecimal(profit));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreazh3 = approvalAreazh3.add(new BigDecimal(area));
                        }
                    }
                }
            }
            zh3.setApprovalMoney(approvalMoneyzh3);
            zh3.setApprovalArea(approvalAreazh3);


            //查询租用去年本月的相关数据
            BigDecimal approvalMoneyzy3 = new BigDecimal(0.00);
            BigDecimal approvalAreazy3 = new BigDecimal(0.00);
            workStatisticsVo.setStep(3);
            ConfigureStatisticsDto zy3 = configureMapper.configureStatistics(workStatisticsVo);
            //查询mongo中数据
            List<Map<String, String>> mapListzy3 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListzy3)) {
                for (Map<String, String> map : mapListzy3) {
                    if (map.containsKey("rent")) {
                        String rent = map.get("rent");
                        if (StringUtils.isNotEmpty(rent)) {
                            approvalMoneyzy3 = approvalMoneyzy3.add(new BigDecimal(rent));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreazy3 = approvalAreazy3.add(new BigDecimal(area));
                        }
                    }
                }
            }
            zy3.setApprovalMoney(approvalMoneyzy3);
            zy3.setApprovalArea(approvalAreazy3);


            //查询建设去年本月的相关数据
            BigDecimal approvalMoneyjs3 = new BigDecimal(0.00);
            BigDecimal approvalAreajs3 = new BigDecimal(0.00);
            workStatisticsVo.setStep(4);
            ConfigureStatisticsDto js3 = configureMapper.configureStatistics(workStatisticsVo);
            //查询mongo中数据
            List<Map<String, String>> mapListjs3 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListjs3)) {
                for (Map<String, String> map : mapListjs3) {
                    if (map.containsKey("expenditure")) {
                        String expenditure = map.get("expenditure");
                        if (StringUtils.isNotEmpty(expenditure)) {
                            approvalMoneyjs3 = approvalMoneyjs3.add(new BigDecimal(expenditure));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreajs3 = approvalAreajs3.add(new BigDecimal(area));
                        }
                    }
                }
            }
            js3.setApprovalMoney(approvalMoneyjs3);
            js3.setApprovalArea(approvalAreajs3);

            //计算调剂审批数量环比
            if (tj1 != null && tj2 != null) {
                if (tj1.getApprovalNum() != null && tj2.getApprovalNum() != null) {
                    Integer tjhb = tj1.getApprovalNum() - tj2.getApprovalNum();
                    tj1.setApprovalNumHb(tjhb);
                }
            }

            //计算调剂审批面积环比
            if (tj1 != null && tj2 != null) {
                if (tj1.getApprovalArea() != null && tj2.getApprovalArea() != null) {
                    BigDecimal tjhb = tj1.getApprovalArea().subtract(tj2.getApprovalArea());
                    tj1.setApprovalAreaHb(tjhb);
                }
            }

            //计算置换审批数量环比
            if (zh1 != null && zh2 != null) {
                if (zh1.getApprovalNum() != null && zh2.getApprovalNum() != null) {
                    Integer zhhb = zh1.getApprovalNum() - zh2.getApprovalNum();
                    zh1.setApprovalNumHb(zhhb);
                }
            }

            //计算置换审批面积环比
            if (zh1 != null && zh2 != null) {
                if (zh1.getApprovalArea() != null && zh2.getApprovalArea() != null) {
                    BigDecimal zhhb = zh1.getApprovalArea().subtract(zh2.getApprovalArea());
                    zh1.setApprovalAreaHb(zhhb);
                }
            }

            //计算置换金额环比
            if (zh1 != null && zh2 != null) {
                if (zh1.getApprovalMoney() != null && zh1.getApprovalMoney() != null) {
                    BigDecimal tjhb = zh1.getApprovalMoney().subtract(zh2.getApprovalMoney());
                    zh1.setApprovalMoneyHb(tjhb);
                }
            }

            //计算租用审批数量环比
            if (zy1 != null && zy2 != null) {
                if (zy1.getApprovalNum() != null && zy2.getApprovalNum() != null) {
                    Integer zyhb = zy1.getApprovalNum() - zy2.getApprovalNum();
                    zy1.setApprovalNumHb(zyhb);
                }
            }

            //计算租用审批面积环比
            if (zy1 != null && zy2 != null) {
                if (zy1.getApprovalArea() != null && zy2.getApprovalArea() != null) {
                    BigDecimal zyhb = zy1.getApprovalArea().subtract(zy2.getApprovalArea());
                    zy1.setApprovalAreaHb(zyhb);
                }
            }

            //计算租用金额环比
            if (zy1 != null && zy2 != null) {
                if (zy1.getApprovalMoney() != null && zy2.getApprovalMoney() != null) {
                    BigDecimal zyhb = zy1.getApprovalMoney().subtract(zy2.getApprovalMoney());
                    zy1.setApprovalMoneyHb(zyhb);
                }
            }

            //计算建设审批数量环比
            if (js1 != null && js2 != null) {
                if (js1.getApprovalNum() != null && js2.getApprovalNum() != null) {
                    Integer jshb = js1.getApprovalNum() - js2.getApprovalNum();
                    js1.setApprovalNumHb(jshb);
                }
            }

            //计算建设审批面积环比
            if (js1 != null && js2 != null) {
                if (js1.getApprovalArea() != null && js2.getApprovalArea() != null) {
                    BigDecimal jshb = js1.getApprovalArea().subtract(js2.getApprovalArea());
                    js1.setApprovalAreaHb(jshb);
                }
            }

            //计算建设金额环比
            if (js1 != null && js2 != null) {
                if (js1.getApprovalMoney() != null && js2.getApprovalMoney() != null) {
                    BigDecimal jshb = js1.getApprovalMoney().subtract(js2.getApprovalMoney());
                    js1.setApprovalMoneyHb(jshb);
                }
            }

            //计算调剂审批数量同比
            if (tj1 != null && tj3 != null) {
                if (tj1.getApprovalNum() != null && tj3.getApprovalNum() != null) {
                    Integer tjtb = tj1.getApprovalNum() - tj3.getApprovalNum();
                    tj1.setApprovalNumTb(tjtb);
                }
            }

            //计算调剂审批面积同比
            if (tj1 != null && tj3 != null) {
                if (tj1.getApprovalArea() != null && tj3.getApprovalArea() != null) {
                    BigDecimal tjtb = tj1.getApprovalArea().subtract(tj3.getApprovalArea());
                    tj1.setApprovalAreaTb(tjtb);
                }
            }

            //计算置换审批数量同比
            if (zh1 != null && zh3 != null) {
                if (zh1.getApprovalNum() != null && zh3.getApprovalNum() != null) {
                    Integer zhtb = zh1.getApprovalNum() - zh3.getApprovalNum();
                    zh1.setApprovalNumTb(zhtb);
                }
            }

            //计算置换审批面积同比
            if (zh1 != null && zh3 != null) {
                if (zh1.getApprovalArea() != null && zh3.getApprovalArea() != null) {
                    BigDecimal zhtb = zh1.getApprovalArea().subtract(zh3.getApprovalArea());
                    zh1.setApprovalAreaTb(zhtb);
                }
            }

            //计算置换金额同比
            if (zh1 != null && zh3 != null) {
                if (zh1.getApprovalMoney() != null && zh3.getApprovalMoney() != null) {
                    BigDecimal tjtb = zh1.getApprovalMoney().subtract(zh3.getApprovalMoney());
                    zh1.setApprovalMoneyTb(tjtb);
                }
            }

            //计算租用审批数量同比
            if (zy1 != null && zy3 != null) {
                if (zy1.getApprovalNum() != null && zy3.getApprovalNum() != null) {
                    Integer zytb = zy1.getApprovalNum() - zy3.getApprovalNum();
                    zy1.setApprovalNumTb(zytb);
                }
            }

            //计算租用审批面积同比
            if (zy1 != null && zy3 != null) {
                if (zy1.getApprovalArea() != null && zy3.getApprovalArea() != null) {
                    BigDecimal zytb = zy1.getApprovalArea().subtract(zy3.getApprovalArea());
                    zy1.setApprovalAreaTb(zytb);
                }
            }

            //计算租用金额同比
            if (zy1 != null && zy3 != null) {
                if (zy1.getApprovalMoney() != null && zy3.getApprovalMoney() != null) {
                    BigDecimal zytb = zy1.getApprovalMoney().subtract(zy3.getApprovalMoney());
                    zy1.setApprovalMoneyTb(zytb);
                }
            }

            //计算建设审批数量同比
            if (js1 != null && js3 != null) {
                if (js1.getApprovalNum() != null && js3.getApprovalNum() != null) {
                    Integer jstb = js1.getApprovalNum() - js3.getApprovalNum();
                    js1.setApprovalNumTb(jstb);
                }
            }

            //计算建设审批面积同比
            if (js1 != null && js3 != null) {
                if (js1.getApprovalArea() != null && js3.getApprovalArea() != null) {
                    BigDecimal jstb = js1.getApprovalArea().subtract(js3.getApprovalArea());
                    js1.setApprovalAreaTb(jstb);
                }
            }

            //计算建设金额同比
            if (js1 != null && js3 != null) {
                if (js1.getApprovalMoney() != null && js3.getApprovalMoney() != null) {
                    BigDecimal jstb = js1.getApprovalMoney().subtract(js3.getApprovalMoney());
                    js1.setApprovalMoneyTb(jstb);
                }
            }

            //计算汇总数据
            ConfigureStatisticsDto amountData = new ConfigureStatisticsDto();
            amountData.setAllocationPlan(5);
            if (tj1.getApprovalNum() != null && zh1.getApprovalNum() != null &&
                    zy1.getApprovalNum() != null && js1.getApprovalNum() != null) {
                //审批数量汇总
                amountData.setApprovalNum(tj1.getApprovalNum() + zh1.getApprovalNum() + zy1.getApprovalNum() + js1.getApprovalNum());
            }
            if (tj1.getApprovalNumHb() != null && zh1.getApprovalNumHb() != null &&
                    zy1.getApprovalNumHb() != null && js1.getApprovalNumHb() != null) {
                //审批数量环比汇总
                amountData.setApprovalNumHb(tj1.getApprovalNumHb() + zh1.getApprovalNumHb() + zy1.getApprovalNumHb() + js1.getApprovalNumHb());
            }
            if (tj1.getApprovalNumTb() != null && zh1.getApprovalNumTb() != null &&
                    zy1.getApprovalNumTb() != null && js1.getApprovalNumTb() != null) {
                //审批数量同比汇总
                amountData.setApprovalNumTb(tj1.getApprovalNumTb() + zh1.getApprovalNumTb() + zy1.getApprovalNumTb() + js1.getApprovalNumTb());
            }

            if (tj1.getApprovalArea() != null && zh1.getApprovalArea() != null &&
                    zy1.getApprovalArea() != null && js1.getApprovalArea() != null) {
                //审批面积汇总
                amountData.setApprovalArea(tj1.getApprovalArea().add(zh1.getApprovalArea()).add(zy1.getApprovalArea()).add(js1.getApprovalArea()));
            }
            if (tj1.getApprovalAreaHb() != null && zh1.getApprovalAreaHb() != null &&
                    zy1.getApprovalAreaHb() != null && js1.getApprovalAreaHb() != null) {
                //审批面积环比汇总
                amountData.setApprovalAreaHb(tj1.getApprovalAreaHb().add(zh1.getApprovalAreaHb()).add(zy1.getApprovalAreaHb()).add(js1.getApprovalAreaHb()));
            }
            if (tj1.getApprovalAreaTb() != null && zh1.getApprovalAreaTb() != null &&
                    zy1.getApprovalAreaTb() != null && js1.getApprovalAreaTb() != null) {
                //审批面积同比汇总
                amountData.setApprovalAreaTb(tj1.getApprovalAreaTb().add(zh1.getApprovalAreaTb()).add(zy1.getApprovalAreaTb()).add(js1.getApprovalAreaTb()));
            }

            if (zh1.getApprovalMoney() != null && zy1.getApprovalMoney() != null && js1.getApprovalMoney() != null) {
                //审批金额汇总
                amountData.setApprovalMoney(zh1.getApprovalMoney().add(zy1.getApprovalMoney()).add(js1.getApprovalMoney()));
            }
            if (zh1.getApprovalMoneyHb() != null && zy1.getApprovalMoneyHb() != null && js1.getApprovalMoneyHb() != null) {
                //审批金额环比汇总
                amountData.setApprovalMoneyHb(zh1.getApprovalMoney().add(zy1.getApprovalMoney()).add(js1.getApprovalMoney()));
            }
            if (zh1.getApprovalMoneyTb() != null && zy1.getApprovalMoneyTb() != null && js1.getApprovalMoneyTb() != null) {
                //审批金额同比汇总
                amountData.setApprovalMoneyTb(zh1.getApprovalMoney().add(zy1.getApprovalMoney()).add(js1.getApprovalMoney()));
            }

            list.add(tj1);
            list.add(zh1);
            list.add(zy1);
            list.add(js1);
            list.add(amountData);
            return list;
        } catch (Exception ex) {
            log.error("configureStatisticsByMonthError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询配置工作统计(按年)
     *
     * @param workStatisticsVo
     * @return
     */
    @Override
    public List<ConfigureStatisticsDto> configureStatisticsByYear(WorkStatisticsVo workStatisticsVo) {
        try {
            if (StringUtils.isEmpty(workStatisticsVo.getApplyDateYear())) {
                throw new ServiceException("申请时间(年)不能为空！");
            }
            //获取用户可以查看的单位信息
            List<Long> unitIds = authorityUnitService.getUnitIdByToken();
            workStatisticsVo.setUnitIdList(unitIds);
            List<ConfigureStatisticsDto> list = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            //1.查询调剂本年的相关数据
            workStatisticsVo.setStep(1);
            ConfigureStatisticsDto tj1 = configureMapper.configureStatistics(workStatisticsVo);
            //获取本年调剂面积
            BigDecimal tj1Area = configureMapper.getTjArea(workStatisticsVo);
            tj1.setApprovalArea(tj1Area);
            tj1.setAllocationPlan(1);

            //查询置换本年的相关数据
            BigDecimal approvalMoneyzh1 = new BigDecimal(0.00);
            BigDecimal approvalAreazh1 = new BigDecimal(0.00);
            workStatisticsVo.setStep(2);
            ConfigureStatisticsDto zh1 = configureMapper.configureStatistics(workStatisticsVo);
            zh1.setAllocationPlan(2);
            //查询mongo中数据
            List<Map<String, String>> mapListzh1 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListzh1)) {
                for (Map<String, String> map : mapListzh1) {
                    if (map.containsKey("makeMoney")) {
                        String makeMoney = map.get("makeMoney");
                        if (StringUtils.isNotEmpty(makeMoney)) {
                            approvalMoneyzh1 = approvalMoneyzh1.add(new BigDecimal(makeMoney));
                        }
                    }
                    if (map.containsKey("profit")) {
                        String profit = map.get("profit");
                        if (StringUtils.isNotEmpty(profit)) {
                            approvalMoneyzh1 = approvalMoneyzh1.add(new BigDecimal(profit));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreazh1 = approvalAreazh1.add(new BigDecimal(area));
                        }
                    }
                }
            }
            zh1.setApprovalMoney(approvalMoneyzh1);
            zh1.setApprovalArea(approvalAreazh1);

            //查询租用本年的相关数据
            BigDecimal approvalMoneyzy1 = new BigDecimal(0.00);
            BigDecimal approvalAreazy1 = new BigDecimal(0.00);
            workStatisticsVo.setStep(3);
            ConfigureStatisticsDto zy1 = configureMapper.configureStatistics(workStatisticsVo);
            zy1.setAllocationPlan(3);
            //查询mongo中数据
            List<Map<String, String>> mapListzy1 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListzy1)) {
                for (Map<String, String> map : mapListzy1) {
                    if (map.containsKey("rent")) {
                        String rent = map.get("rent");
                        if (StringUtils.isNotEmpty(rent)) {
                            approvalMoneyzy1 = approvalMoneyzy1.add(new BigDecimal(rent));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreazy1 = approvalAreazy1.add(new BigDecimal(area));
                        }
                    }
                }
            }
            zy1.setApprovalMoney(approvalMoneyzy1);
            zy1.setApprovalArea(approvalAreazy1);

            //查询建设本年的相关数据
            BigDecimal approvalMoneyjs1 = new BigDecimal(0.00);
            BigDecimal approvalAreajs1 = new BigDecimal(0.00);
            workStatisticsVo.setStep(4);
            ConfigureStatisticsDto js1 = configureMapper.configureStatistics(workStatisticsVo);
            js1.setAllocationPlan(4);
            //查询mongo中数据
            List<Map<String, String>> mapListjs1 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListjs1)) {
                for (Map<String, String> map : mapListjs1) {
                    if (map.containsKey("expenditure")) {
                        String expenditure = map.get("expenditure");
                        if (StringUtils.isNotEmpty(expenditure)) {
                            approvalMoneyjs1 = approvalMoneyjs1.add(new BigDecimal(expenditure));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreajs1 = approvalAreajs1.add(new BigDecimal(area));
                        }
                    }
                }
            }
            js1.setApprovalMoney(approvalMoneyjs1);
            js1.setApprovalArea(approvalAreajs1);


            //3.查询调剂去年的相关数据
            Date date2 = this.strToDate(null, workStatisticsVo.getApplyDateYear());
            calendar.setTime(date2);
            calendar.add(Calendar.YEAR, -1);
            Date lastYear = calendar.getTime();
            //获取去年
            String lastYearStr = this.dateToStr(null, lastYear);
            workStatisticsVo.setApplyDateYear(lastYearStr);
            workStatisticsVo.setStep(1);
            ConfigureStatisticsDto tj3 = configureMapper.configureStatistics(workStatisticsVo);
            //获取去年调剂面积
            BigDecimal tj3Area = configureMapper.getTjArea(workStatisticsVo);
            tj3.setApprovalArea(tj3Area);


            //查询置换去年的相关数据
            BigDecimal approvalMoneyzh3 = new BigDecimal(0.00);
            BigDecimal approvalAreazh3 = new BigDecimal(0.00);
            workStatisticsVo.setStep(2);
            ConfigureStatisticsDto zh3 = configureMapper.configureStatistics(workStatisticsVo);
            //查询mongo中数据
            List<Map<String, String>> mapListzh3 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListzh3)) {
                for (Map<String, String> map : mapListzh3) {
                    if (map.containsKey("makeMoney")) {
                        String makeMoney = map.get("makeMoney");
                        if (StringUtils.isNotEmpty(makeMoney)) {
                            approvalMoneyzh3 = approvalMoneyzh1.add(new BigDecimal(makeMoney));
                        }
                    }
                    if (map.containsKey("profit")) {
                        String profit = map.get("profit");
                        if (StringUtils.isNotEmpty(profit)) {
                            approvalMoneyzh3 = approvalMoneyzh1.add(new BigDecimal(profit));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreazh3 = approvalAreazh3.add(new BigDecimal(area));
                        }
                    }
                }
            }
            zh3.setApprovalMoney(approvalMoneyzh3);
            zh3.setApprovalArea(approvalAreazh3);


            //查询租用去年的相关数据
            BigDecimal approvalMoneyzy3 = new BigDecimal(0.00);
            BigDecimal approvalAreazy3 = new BigDecimal(0.00);
            workStatisticsVo.setStep(3);
            ConfigureStatisticsDto zy3 = configureMapper.configureStatistics(workStatisticsVo);
            //查询mongo中数据
            List<Map<String, String>> mapListzy3 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListzy3)) {
                for (Map<String, String> map : mapListzy3) {
                    if (map.containsKey("rent")) {
                        String rent = map.get("rent");
                        if (StringUtils.isNotEmpty(rent)) {
                            approvalMoneyzy3 = approvalMoneyzy3.add(new BigDecimal(rent));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreazy3 = approvalAreazy3.add(new BigDecimal(area));
                        }
                    }
                }
            }
            zy3.setApprovalMoney(approvalMoneyzy3);
            zy3.setApprovalArea(approvalAreazy3);

            //查询建设去年的相关数据
            BigDecimal approvalMoneyjs3 = new BigDecimal(0.00);
            BigDecimal approvalAreajs3 = new BigDecimal(0.00);
            workStatisticsVo.setStep(4);
            ConfigureStatisticsDto js3 = configureMapper.configureStatistics(workStatisticsVo);
            //查询mongo中数据
            List<Map<String, String>> mapListjs3 = this.moneyMap(workStatisticsVo);
            if (CollectionUtils.isNotEmpty(mapListjs3)) {
                for (Map<String, String> map : mapListjs3) {
                    if (map.containsKey("expenditure")) {
                        String expenditure = map.get("expenditure");
                        if (StringUtils.isNotEmpty(expenditure)) {
                            approvalMoneyjs3 = approvalMoneyjs3.add(new BigDecimal(expenditure));
                        }
                    }
                    if (map.containsKey("area")) {
                        String area = map.get("area");
                        if (StringUtils.isNotEmpty(area)) {
                            approvalAreajs3 = approvalAreajs3.add(new BigDecimal(area));
                        }
                    }
                }
            }
            js3.setApprovalMoney(approvalMoneyjs3);
            js3.setApprovalArea(approvalAreajs3);

            //计算调剂审批数量环比
            if (tj1 != null && tj3 != null) {
                if (tj1.getApprovalNum() != null && tj3.getApprovalNum() != null) {
                    Integer tjhb = tj1.getApprovalNum() - tj3.getApprovalNum();
                    tj1.setApprovalNumHb(tjhb);
                }
            }

            //计算调剂审批面积环比
            if (tj1 != null && tj3 != null) {
                if (tj1.getApprovalArea() != null && tj3.getApprovalArea() != null) {
                    BigDecimal tjhb = tj1.getApprovalArea().subtract(tj3.getApprovalArea());
                    tj1.setApprovalAreaHb(tjhb);
                }
            }

            //计算置换审批数量环比
            if (zh1 != null && zh3 != null) {
                if (zh1.getApprovalNum() != null && zh3.getApprovalNum() != null) {
                    Integer zhhb = zh1.getApprovalNum() - zh3.getApprovalNum();
                    zh1.setApprovalNumHb(zhhb);
                }
            }

            //计算置换审批面积环比
            if (zh1 != null && zh3 != null) {
                if (zh1.getApprovalArea() != null && zh3.getApprovalArea() != null) {
                    BigDecimal zhhb = zh1.getApprovalArea().subtract(zh3.getApprovalArea());
                    zh1.setApprovalAreaHb(zhhb);
                }
            }

            //计算置换金额环比
            if (zh1 != null && zh3 != null) {
                if (zh1.getApprovalMoney() != null && zh3.getApprovalMoney() != null) {
                    BigDecimal zhhb = zh1.getApprovalMoney().subtract(zh3.getApprovalMoney());
                    zh1.setApprovalMoneyHb(zhhb);
                }
            }

            //计算租用审批数量环比
            if (zy1 != null && zy3 != null) {
                if (zy1.getApprovalNum() != null && zy3.getApprovalNum() != null) {
                    Integer zyhb = zy1.getApprovalNum() - zy3.getApprovalNum();
                    zy1.setApprovalNumHb(zyhb);
                }
            }

            //计算租用审批面积环比
            if (zy1 != null && zy3 != null) {
                if (zy1.getApprovalArea() != null && zy3.getApprovalArea() != null) {
                    BigDecimal zyhb = zy1.getApprovalArea().subtract(zy3.getApprovalArea());
                    zy1.setApprovalAreaHb(zyhb);
                }
            }

            //计算租用金额环比
            if (zy1 != null && zy3 != null) {
                if (zy1.getApprovalMoney() != null && zy3.getApprovalMoney() != null) {
                    BigDecimal zyhb = zy1.getApprovalMoney().subtract(zy3.getApprovalMoney());
                    zy1.setApprovalMoneyHb(zyhb);
                }
            }

            //计算建设审批数量环比
            if (js1 != null && js3 != null) {
                if (js1.getApprovalNum() != null && js3.getApprovalNum() != null) {
                    Integer jshb = js1.getApprovalNum() - js3.getApprovalNum();
                    js1.setApprovalNumHb(jshb);
                }
            }

            //计算建设审批面积环比
            if (js1 != null && js3 != null) {
                if (js1.getApprovalArea() != null && js3.getApprovalArea() != null) {
                    BigDecimal jshb = js1.getApprovalArea().subtract(js3.getApprovalArea());
                    js1.setApprovalAreaHb(jshb);
                }
            }

            //计算建设金额环比
            if (js1 != null && js3 != null) {
                if (js1.getApprovalMoney() != null && js3.getApprovalMoney() != null) {
                    BigDecimal jshb = js1.getApprovalMoney().subtract(js3.getApprovalMoney());
                    js1.setApprovalMoneyHb(jshb);
                }
            }


            //计算汇总数据
            ConfigureStatisticsDto amountData = new ConfigureStatisticsDto();
            amountData.setAllocationPlan(5);
            if (tj1.getApprovalNum() != null && zh1.getApprovalNum() != null &&
                    zy1.getApprovalNum() != null && js1.getApprovalNum() != null) {
                //审批数量汇总
                amountData.setApprovalNum(tj1.getApprovalNum() + zh1.getApprovalNum() + zy1.getApprovalNum() + js1.getApprovalNum());
            }
            if (tj1.getApprovalNumHb() != null && zh1.getApprovalNumHb() != null &&
                    zy1.getApprovalNumHb() != null && js1.getApprovalNumHb() != null) {
                //审批数量环比汇总
                amountData.setApprovalNumHb(tj1.getApprovalNumHb() + zh1.getApprovalNumHb() + zy1.getApprovalNumHb() + js1.getApprovalNumHb());
            }
            if (zh1.getApprovalMoney() != null && zy1.getApprovalMoney() != null && js1.getApprovalMoney() != null) {
                //审批金额汇总
                amountData.setApprovalMoney(zh1.getApprovalMoney().add(zy1.getApprovalMoney()).add(js1.getApprovalMoney()));
            }
            if (zh1.getApprovalMoneyHb() != null && zy1.getApprovalMoneyHb() != null && js1.getApprovalMoneyHb() != null) {
                //审批金额环比汇总
                amountData.setApprovalMoneyHb(zh1.getApprovalMoney().add(zy1.getApprovalMoney()).add(js1.getApprovalMoney()));
            }

            if (tj1.getApprovalArea() != null && zh1.getApprovalArea() != null &&
                    zy1.getApprovalArea() != null && js1.getApprovalArea() != null) {
                //审批面积汇总
                amountData.setApprovalArea(tj1.getApprovalArea().add(zh1.getApprovalArea()).add(zy1.getApprovalArea()).add(js1.getApprovalArea()));
            }
            if (tj1.getApprovalAreaHb() != null && zh1.getApprovalAreaHb() != null &&
                    zy1.getApprovalAreaHb() != null && js1.getApprovalAreaHb() != null) {
                //审批面积环比汇总
                amountData.setApprovalAreaHb(tj1.getApprovalAreaHb().add(zh1.getApprovalAreaHb()).add(zy1.getApprovalAreaHb()).add(js1.getApprovalAreaHb()));
            }

            list.add(tj1);
            list.add(zh1);
            list.add(zy1);
            list.add(js1);
            list.add(amountData);
            return list;
        } catch (Exception ex) {
            log.error("configureStatisticsByYearError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询工作统计日期范围
     *
     * @param basicVo
     * @return
     */
    @Override
    public DateRangeDto statisticsDateRange(BasicVo<Integer> basicVo) {
        try {
            //获取用户可以查看的单位信息
            List<Long> unitIds = authorityUnitService.getUnitIdByToken();
            DateRangeDto dateRangeDto = new DateRangeDto();
            //维修日期范围
            if (basicVo.getId().equals(1)) {
                dateRangeDto = repairApplyMapper.statisticsDateRange(unitIds);
            }
            //处置日期范围
            if (basicVo.getId().equals(2)) {
                dateRangeDto = handleMapper.statisticsDateRange(unitIds);
            }
            //配置日期范围
            if (basicVo.getId().equals(3)) {
                dateRangeDto = configureMapper.statisticsDateRange(unitIds);
            }
            return dateRangeDto;
        } catch (Exception ex) {
            log.error("configureStatisticsDateByMonthError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询mongo中符合条件的配置金额数据
     *
     * @param workStatisticsVo
     * @return
     */
    public List<Map<String, String>> moneyMap(WorkStatisticsVo workStatisticsVo) {
        List<Map<String, String>> resultList = new ArrayList<>();
        //查询配置Id集合
        List<Long> configureIdList = configureMapper.configureIdList(workStatisticsVo);
        if (CollectionUtils.isNotEmpty(configureIdList)) {
            for (Long configureId : configureIdList) {
                Document document = mongoTemplate.findById(configureId, Document.class, "configure");
                if (document != null) {
                    Map<String, Object> map = (Map<String, Object>) document.get("improvingDataDb");
                    if (map != null && map.containsKey("improvingDataRRC")) {
                        Map<String, String> resultMap = (Map<String, String>) map.get("improvingDataRRC");
                        resultList.add(resultMap);
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * 查询处置工作统计(按月)
     *
     * @param workStatisticsVo
     * @return
     */
    @Override
    public List<HandleWorkStatistics> selectHandleMothCount(WorkStatisticsVo workStatisticsVo) {
        try {
            List<HandleWorkStatistics> handleWorkStatisticsList = new ArrayList<>();

            //获取用户可以查看的单位信息
            List<Long> unitIds = authorityUnitService.getUnitIdByToken();
            workStatisticsVo.setUnitIdList(unitIds);

            Date handleDate;
            if (StringUtils.isNotBlank(workStatisticsVo.getApplyDateMonth())) {
                handleDate = this.strToDate(workStatisticsVo.getApplyDateMonth(), null);
            } else {
                throw new ServiceException("申请时间(月)不能为空！");
            }

            String handleYearOld;
            String handleMonthOld;
            Calendar calendar = Calendar.getInstance();
            if (handleDate != null) {
                calendar.setTime(handleDate);
                calendar.add(Calendar.YEAR, -1);
                Date repairApplyDateOld = calendar.getTime();
                handleYearOld = this.dateToStr(repairApplyDateOld, null);
                workStatisticsVo.setApplyDateMonth(handleYearOld);
            }
            //得到去年减去年份对应月的相关数据 转换用途
            workStatisticsVo.setHandleWay(HandleWayEnum.TRANSFROMATION.getValue());
            HandleWorkStatistics handleWorkStatisticsYear1 = handleMapper.selectHandleCount(workStatisticsVo);

            //得到去年减去年份对应月的相关数据 出租
            workStatisticsVo.setHandleWay(HandleWayEnum.RENT.getValue());
            HandleWorkStatistics handleWorkStatisticsYear2 = handleMapper.selectHandleCount(workStatisticsVo);

            //得到去年减去年份对应月的相关数据 拍卖
            workStatisticsVo.setHandleWay(HandleWayEnum.ACUTION.getValue());
            HandleWorkStatistics handleWorkStatisticsYear3 = handleMapper.selectHandleCount(workStatisticsVo);
            //得到去年减去年份对应月的相关数据 拍卖金额
            List<Handle> handleYearList1 = handleMapper.selectHandleIds(workStatisticsVo);
            BigDecimal handleMoneyYear1 = new BigDecimal(0.00);
            if (CollectionUtils.isNotEmpty(handleYearList1)) {
                for (Handle handle : handleYearList1) {
                    Document document = mongoTemplate.findById(handle.getHandleId(), Document.class, "handle");
                    if (document != null) {
                        if (document.get("auctionDb") != null) {
                            Map<String, Object> map = (Map<String, Object>) document.get("auctionDb");
                            if (map.containsKey("winningAmount")) {
                                String winningAmount = String.valueOf(map.get("winningAmount"));
                                BigDecimal winningAmounts = new BigDecimal(winningAmount);
                                handleMoneyYear1 = handleMoneyYear1.add(winningAmounts);
                            }
                        }
                    }
                }
            }
            handleWorkStatisticsYear3.setHandleMoney(handleMoneyYear1);


            //得到去年减去年份对应月的相关数据 拆除
            workStatisticsVo.setHandleWay(HandleWayEnum.DISMANTLE.getValue());
            HandleWorkStatistics handleWorkStatisticsYear4 = handleMapper.selectHandleCount(workStatisticsVo);

            //得到去年减去年份对应月的相关数据 出借
            workStatisticsVo.setHandleWay(HandleWayEnum.LEND.getValue());
            HandleWorkStatistics handleWorkStatisticsYear5 = handleMapper.selectHandleCount(workStatisticsVo);

            //得到去年减去年份对应月的相关数据 征收
            workStatisticsVo.setHandleWay(HandleWayEnum.LEVY.getValue());
            HandleWorkStatistics handleWorkStatisticsYear6 = handleMapper.selectHandleCount(workStatisticsVo);
            //得到去年减去年份对应月的相关数据 征收金额
            List<Handle> handleYearList2 = handleMapper.selectHandleIds(workStatisticsVo);
            BigDecimal handleMoneyYear2 = new BigDecimal(0.00);
            if (CollectionUtils.isNotEmpty(handleYearList2)) {
                for (Handle handle : handleYearList2) {
                    Document document = mongoTemplate.findById(handle.getHandleId(), Document.class, "handle");
                    if (document != null) {
                        if (document.get("levyDb") != null) {
                            Map<String, Object> map = (Map<String, Object>) document.get("levyDb");
                            if (map.containsKey("levyCompensation")) {
                                String levyCompensation = String.valueOf(map.get("levyCompensation"));
                                BigDecimal levyCompensations = new BigDecimal(levyCompensation);
                                handleMoneyYear2 = handleMoneyYear2.add(levyCompensations);
                            }
                        }
                    }
                }
            }
            handleWorkStatisticsYear6.setHandleMoney(handleMoneyYear2);


            if (handleDate != null) {
                calendar.setTime(handleDate);
                calendar.add(Calendar.MONTH, -1);
                Date repairApplyDateMonth = calendar.getTime();
                handleMonthOld = this.dateToStr(repairApplyDateMonth, null);
                workStatisticsVo.setApplyDateMonth(handleMonthOld);
            }
            //得到上个月的相关数据 转换用途
            workStatisticsVo.setHandleWay(HandleWayEnum.TRANSFROMATION.getValue());
            HandleWorkStatistics handleWorkStatisticsMonth1 = handleMapper.selectHandleCount(workStatisticsVo);

            //得到上个月的相关数据 出租
            workStatisticsVo.setHandleWay(HandleWayEnum.RENT.getValue());
            HandleWorkStatistics handleWorkStatisticsMonth2 = handleMapper.selectHandleCount(workStatisticsVo);

            //得到上个月的相关数据 拍卖
            workStatisticsVo.setHandleWay(HandleWayEnum.ACUTION.getValue());
            HandleWorkStatistics handleWorkStatisticsMonth3 = handleMapper.selectHandleCount(workStatisticsVo);
            //得到上个月的相关数据 拍卖金额
            List<Handle> handleMonthList1 = handleMapper.selectHandleIds(workStatisticsVo);
            BigDecimal handleMoneyMonth1 = new BigDecimal(0.00);
            if (CollectionUtils.isNotEmpty(handleMonthList1)) {
                for (Handle handle : handleMonthList1) {
                    Document document = mongoTemplate.findById(handle.getHandleId(), Document.class, "handle");
                    if (document != null) {
                        if (document.get("auctionDb") != null) {
                            Map<String, Object> map = (Map<String, Object>) document.get("auctionDb");
                            if (map.containsKey("winningAmount")) {
                                String winningAmount = String.valueOf(map.get("winningAmount"));
                                BigDecimal winningAmounts = new BigDecimal(winningAmount);
                                handleMoneyMonth1 = handleMoneyMonth1.add(winningAmounts);
                            }
                        }
                    }
                }
            }
            handleWorkStatisticsMonth3.setHandleMoney(handleMoneyMonth1);

            //得到上个月的相关数据 拆除
            workStatisticsVo.setHandleWay(HandleWayEnum.DISMANTLE.getValue());
            HandleWorkStatistics handleWorkStatisticsMonth4 = handleMapper.selectHandleCount(workStatisticsVo);

            //得到上个月的相关数据 出借
            workStatisticsVo.setHandleWay(HandleWayEnum.LEND.getValue());
            HandleWorkStatistics handleWorkStatisticsMonth5 = handleMapper.selectHandleCount(workStatisticsVo);

            //得到上个月的相关数据 征收
            workStatisticsVo.setHandleWay(HandleWayEnum.LEVY.getValue());
            HandleWorkStatistics handleWorkStatisticsMonth6 = handleMapper.selectHandleCount(workStatisticsVo);
            //得到上个月的相关数据 征收金额
            List<Handle> handleMonthList2 = handleMapper.selectHandleIds(workStatisticsVo);
            BigDecimal handleMoneyMonth2 = new BigDecimal(0.00);
            if (CollectionUtils.isNotEmpty(handleMonthList2)) {
                for (Handle handle : handleMonthList2) {
                    Document document = mongoTemplate.findById(handle.getHandleId(), Document.class, "handle");
                    if (document != null) {
                        if (document.get("levyDb") != null) {
                            Map<String, Object> map = (Map<String, Object>) document.get("levyDb");
                            if (map.containsKey("levyCompensation")) {
                                String levyCompensation = String.valueOf(map.get("levyCompensation"));
                                BigDecimal levyCompensations = new BigDecimal(levyCompensation);
                                handleMoneyMonth2 = handleMoneyMonth2.add(levyCompensations);
                            }
                        }
                    }
                }
            }
            handleWorkStatisticsMonth6.setHandleMoney(handleMoneyMonth2);


            //转换用途统计
            workStatisticsVo.setHandleWay(HandleWayEnum.TRANSFROMATION.getValue());
            if (handleDate != null) {
                workStatisticsVo.setApplyDateMonth(this.dateToStr(handleDate, null));
            }
            HandleWorkStatistics handleWorkStatistics1 = handleMapper.selectHandleCount(workStatisticsVo);
            handleWorkStatistics1.setHandleType(1);

            //求转换用途统计同比
            if (handleWorkStatistics1 != null && handleWorkStatisticsYear1 != null) {
                //审批数量同比
                if (handleWorkStatistics1.getAuditCount() != null && handleWorkStatisticsYear1.getAuditCount() != null) {
                    handleWorkStatistics1.setAuditCountTb(handleWorkStatistics1.getAuditCount() - handleWorkStatisticsYear1.getAuditCount());
                }
                //处置面积同比
                if (handleWorkStatistics1.getHandleArea() != null && handleWorkStatisticsYear1.getHandleArea() != null) {
                    handleWorkStatistics1.setHandleAreaTb(handleWorkStatistics1.getHandleArea() - handleWorkStatisticsYear1.getHandleArea());
                }
            }

            //求转换用途统计环比
            if (handleWorkStatistics1 != null && handleWorkStatisticsMonth1 != null) {
                //审批数量环比
                if (handleWorkStatistics1.getAuditCount() != null && handleWorkStatisticsMonth1.getAuditCount() != null) {
                    handleWorkStatistics1.setAuditCountHb(handleWorkStatistics1.getAuditCount() - handleWorkStatisticsMonth1.getAuditCount());
                }
                //处置面积环比
                if (handleWorkStatistics1.getHandleArea() != null && handleWorkStatisticsMonth1.getHandleArea() != null) {
                    handleWorkStatistics1.setHandleAreaHb(handleWorkStatistics1.getHandleArea() - handleWorkStatisticsMonth1.getHandleArea());
                }
            }
            handleWorkStatisticsList.add(handleWorkStatistics1);


            //出租统计
            workStatisticsVo.setHandleWay(HandleWayEnum.RENT.getValue());
            if (handleDate != null) {
                workStatisticsVo.setApplyDateMonth(this.dateToStr(handleDate, null));
            }
            HandleWorkStatistics handleWorkStatistics2 = handleMapper.selectHandleCount(workStatisticsVo);
            handleWorkStatistics2.setHandleType(2);

            //求出租统计同比
            if (handleWorkStatistics2 != null && handleWorkStatisticsYear2 != null) {
                //审批数量同比
                if (handleWorkStatistics2.getAuditCount() != null && handleWorkStatisticsYear2.getAuditCount() != null) {
                    handleWorkStatistics2.setAuditCountTb(handleWorkStatistics2.getAuditCount() - handleWorkStatisticsYear2.getAuditCount());
                }
                //处置面积同比
                if (handleWorkStatistics2.getHandleArea() != null && handleWorkStatisticsYear2.getHandleArea() != null) {
                    handleWorkStatistics2.setHandleAreaTb(handleWorkStatistics2.getHandleArea() - handleWorkStatisticsYear2.getHandleArea());
                }
            }

            //求出租统计环比
            if (handleWorkStatistics2 != null && handleWorkStatisticsMonth2 != null) {
                //审批数量环比
                if (handleWorkStatistics2.getAuditCount() != null && handleWorkStatisticsMonth2.getAuditCount() != null) {
                    handleWorkStatistics2.setAuditCountHb(handleWorkStatistics2.getAuditCount() - handleWorkStatisticsMonth2.getAuditCount());
                }
                //处置面积环比
                if (handleWorkStatistics2.getHandleArea() != null && handleWorkStatisticsMonth2.getHandleArea() != null) {
                    handleWorkStatistics2.setHandleAreaHb(handleWorkStatistics2.getHandleArea() - handleWorkStatisticsMonth2.getHandleArea());
                }
            }
            handleWorkStatisticsList.add(handleWorkStatistics2);


            //拍卖统计
            workStatisticsVo.setHandleWay(HandleWayEnum.ACUTION.getValue());
            if (handleDate != null) {
                workStatisticsVo.setApplyDateMonth(this.dateToStr(handleDate, null));
            }
            HandleWorkStatistics handleWorkStatistics3 = handleMapper.selectHandleCount(workStatisticsVo);
            handleWorkStatistics3.setHandleType(3);

            //拍卖金额
            List<Handle> handleList1 = handleMapper.selectHandleIds(workStatisticsVo);
            BigDecimal handleMoney1 = new BigDecimal(0.00);
            if (CollectionUtils.isNotEmpty(handleList1)) {
                for (Handle handle : handleList1) {
                    Document document = mongoTemplate.findById(handle.getHandleId(), Document.class, "handle");
                    if (document != null) {
                        if (document.get("auctionDb") != null) {
                            Map<String, Object> map = (Map<String, Object>) document.get("auctionDb");
                            if (map.containsKey("winningAmount")) {
                                String winningAmount = String.valueOf(map.get("winningAmount"));
                                BigDecimal winningAmounts = new BigDecimal(winningAmount);
                                handleMoney1 = handleMoney1.add(winningAmounts);
                            }
                        }
                    }
                }
            }
            handleWorkStatistics3.setHandleMoney(handleMoney1);

            //求拍卖统计同比
            if (handleWorkStatistics3 != null && handleWorkStatisticsYear3 != null) {
                //审批数量同比
                if (handleWorkStatistics3.getAuditCount() != null && handleWorkStatisticsYear3.getAuditCount() != null) {
                    handleWorkStatistics3.setAuditCountTb(handleWorkStatistics3.getAuditCount() - handleWorkStatisticsYear3.getAuditCount());
                }
                //处置面积同比
                if (handleWorkStatistics3.getHandleArea() != null && handleWorkStatisticsYear3.getHandleArea() != null) {
                    handleWorkStatistics3.setHandleAreaTb(handleWorkStatistics3.getHandleArea() - handleWorkStatisticsYear3.getHandleArea());
                }
                //处置金额同比
                if (handleWorkStatistics3.getHandleMoney() != null && handleWorkStatisticsYear3.getHandleMoney() != null) {
                    handleWorkStatistics3.setHandleMoneyTb(handleWorkStatistics3.getHandleMoney().subtract(handleWorkStatisticsYear3.getHandleMoney()));
                }
            }

            //求拍卖统计环比
            if (handleWorkStatistics3 != null && handleWorkStatisticsMonth3 != null) {
                //审批数量环比
                if (handleWorkStatistics3.getAuditCount() != null && handleWorkStatisticsMonth3.getAuditCount() != null) {
                    handleWorkStatistics3.setAuditCountHb(handleWorkStatistics3.getAuditCount() - handleWorkStatisticsMonth3.getAuditCount());
                }
                //处置面积环比
                if (handleWorkStatistics3.getHandleArea() != null && handleWorkStatisticsMonth3.getHandleArea() != null) {
                    handleWorkStatistics3.setHandleAreaHb(handleWorkStatistics3.getHandleArea() - handleWorkStatisticsMonth3.getHandleArea());
                }
                //处置金额环比
                if (handleWorkStatistics3.getHandleMoney() != null && handleWorkStatisticsMonth3.getHandleMoney() != null) {
                    handleWorkStatistics3.setHandleMoneyHb(handleWorkStatistics3.getHandleMoney().subtract(handleWorkStatisticsMonth3.getHandleMoney()));
                }
            }
            handleWorkStatisticsList.add(handleWorkStatistics3);


            //拆除统计
            workStatisticsVo.setHandleWay(HandleWayEnum.DISMANTLE.getValue());
            if (handleDate != null) {
                workStatisticsVo.setApplyDateMonth(this.dateToStr(handleDate, null));
            }
            HandleWorkStatistics handleWorkStatistics4 = handleMapper.selectHandleCount(workStatisticsVo);
            handleWorkStatistics4.setHandleType(4);

            //求拆除统计同比
            if (handleWorkStatistics4 != null && handleWorkStatisticsYear4 != null) {
                //审批数量同比
                if (handleWorkStatistics4.getAuditCount() != null && handleWorkStatisticsYear4.getAuditCount() != null) {
                    handleWorkStatistics4.setAuditCountTb(handleWorkStatistics4.getAuditCount() - handleWorkStatisticsYear4.getAuditCount());
                }
                //处置面积同比
                if (handleWorkStatistics4.getHandleArea() != null && handleWorkStatisticsYear4.getHandleArea() != null) {
                    handleWorkStatistics4.setHandleAreaTb(handleWorkStatistics4.getHandleArea() - handleWorkStatisticsYear4.getHandleArea());
                }
            }

            //求拆除统计环比
            if (handleWorkStatistics4 != null && handleWorkStatisticsMonth4 != null) {
                //审批数量环比
                if (handleWorkStatistics4.getAuditCount() != null && handleWorkStatisticsMonth4.getAuditCount() != null) {
                    handleWorkStatistics4.setAuditCountHb(handleWorkStatistics4.getAuditCount() - handleWorkStatisticsMonth4.getAuditCount());
                }
                //处置面积环比
                if (handleWorkStatistics4.getHandleArea() != null && handleWorkStatisticsMonth4.getHandleArea() != null) {
                    handleWorkStatistics4.setHandleAreaHb(handleWorkStatistics4.getHandleArea() - handleWorkStatisticsMonth4.getHandleArea());
                }
            }
            handleWorkStatisticsList.add(handleWorkStatistics4);


            //出借统计
            workStatisticsVo.setHandleWay(HandleWayEnum.LEND.getValue());
            if (handleDate != null) {
                workStatisticsVo.setApplyDateMonth(this.dateToStr(handleDate, null));
            }
            HandleWorkStatistics handleWorkStatistics5 = handleMapper.selectHandleCount(workStatisticsVo);
            handleWorkStatistics5.setHandleType(5);

            //求出借统计同比
            if (handleWorkStatistics5 != null && handleWorkStatisticsYear5 != null) {
                //审批数量同比
                if (handleWorkStatistics5.getAuditCount() != null && handleWorkStatisticsYear5.getAuditCount() != null) {
                    handleWorkStatistics5.setAuditCountTb(handleWorkStatistics5.getAuditCount() - handleWorkStatisticsYear5.getAuditCount());
                }
                //处置面积同比
                if (handleWorkStatistics5.getHandleArea() != null && handleWorkStatisticsYear5.getHandleArea() != null) {
                    handleWorkStatistics5.setHandleAreaTb(handleWorkStatistics5.getHandleArea() - handleWorkStatisticsYear5.getHandleArea());
                }
            }

            //求出借统计环比
            if (handleWorkStatistics5 != null && handleWorkStatisticsMonth5 != null) {
                //审批数量环比
                if (handleWorkStatistics5.getAuditCount() != null && handleWorkStatisticsMonth5.getAuditCount() != null) {
                    handleWorkStatistics5.setAuditCountHb(handleWorkStatistics5.getAuditCount() - handleWorkStatisticsMonth5.getAuditCount());
                }
                //处置面积环比
                if (handleWorkStatistics5.getHandleArea() != null && handleWorkStatisticsMonth5.getHandleArea() != null) {
                    handleWorkStatistics5.setHandleAreaHb(handleWorkStatistics5.getHandleArea() - handleWorkStatisticsMonth5.getHandleArea());
                }
            }
            handleWorkStatisticsList.add(handleWorkStatistics5);


            //征收统计
            workStatisticsVo.setHandleWay(HandleWayEnum.LEVY.getValue());
            if (handleDate != null) {
                workStatisticsVo.setApplyDateMonth(this.dateToStr(handleDate, null));
            }
            HandleWorkStatistics handleWorkStatistics6 = handleMapper.selectHandleCount(workStatisticsVo);
            handleWorkStatistics6.setHandleType(6);

            //征收金额
            List<Handle> handleList2 = handleMapper.selectHandleIds(workStatisticsVo);
            BigDecimal handleMoney2 = new BigDecimal(0.00);
            if (CollectionUtils.isNotEmpty(handleList2)) {
                for (Handle handle : handleList2) {
                    Document document = mongoTemplate.findById(handle.getHandleId(), Document.class, "handle");
                    if (document != null) {
                        if (document.get("levyDb") != null) {
                            Map<String, Object> map = (Map<String, Object>) document.get("levyDb");
                            if (map.containsKey("levyCompensation")) {
                                String levyCompensation = String.valueOf(map.get("levyCompensation"));
                                BigDecimal levyCompensations = new BigDecimal(levyCompensation);
                                handleMoney2 = handleMoney2.add(levyCompensations);
                            }
                        }
                    }
                }
            }
            handleWorkStatistics6.setHandleMoney(handleMoney2);

            //求征收统计同比
            if (handleWorkStatistics6 != null && handleWorkStatisticsYear6 != null) {
                //审批数量同比
                if (handleWorkStatistics6.getAuditCount() != null && handleWorkStatisticsYear6.getAuditCount() != null) {
                    handleWorkStatistics6.setAuditCountTb(handleWorkStatistics6.getAuditCount() - handleWorkStatisticsYear6.getAuditCount());
                }
                //处置面积同比
                if (handleWorkStatistics6.getHandleArea() != null && handleWorkStatisticsYear6.getHandleArea() != null) {
                    handleWorkStatistics6.setHandleAreaTb(handleWorkStatistics6.getHandleArea() - handleWorkStatisticsYear6.getHandleArea());
                }
                //处置金额同比
                if (handleWorkStatistics6.getHandleMoney() != null && handleWorkStatisticsYear6.getHandleMoney() != null) {
                    handleWorkStatistics6.setHandleMoneyTb(handleWorkStatistics6.getHandleMoney().subtract(handleWorkStatisticsYear6.getHandleMoney()));
                }
            }

            //求征收统计环比
            if (handleWorkStatistics6 != null && handleWorkStatisticsMonth6 != null) {
                //审批数量环比
                if (handleWorkStatistics6.getAuditCount() != null && handleWorkStatisticsMonth6.getAuditCount() != null) {
                    handleWorkStatistics6.setAuditCountHb(handleWorkStatistics6.getAuditCount() - handleWorkStatisticsMonth6.getAuditCount());
                }
                //处置面积环比
                if (handleWorkStatistics6.getHandleArea() != null && handleWorkStatisticsMonth6.getHandleArea() != null) {
                    handleWorkStatistics6.setHandleAreaHb(handleWorkStatistics6.getHandleArea() - handleWorkStatisticsMonth6.getHandleArea());
                }
                //处置金额环比
                if (handleWorkStatistics6.getHandleMoney() != null && handleWorkStatisticsMonth6.getHandleMoney() != null) {
                    handleWorkStatistics6.setHandleMoneyHb(handleWorkStatistics6.getHandleMoney().subtract(handleWorkStatisticsMonth6.getHandleMoney()));
                }
            }
            handleWorkStatisticsList.add(handleWorkStatistics6);

            //求合计
            HandleWorkStatistics handleWorkStatistics7 = new HandleWorkStatistics();
            handleWorkStatistics7.setHandleType(7);
            //求审批数量总和
            handleWorkStatistics7.setAuditCount(handleWorkStatistics1.getAuditCount() + handleWorkStatistics2.getAuditCount() +
                    handleWorkStatistics3.getAuditCount() + handleWorkStatistics4.getAuditCount() + handleWorkStatistics5.getAuditCount() +
                    handleWorkStatistics6.getAuditCount());

            //求审批数量同比总和
            handleWorkStatistics7.setAuditCountTb(handleWorkStatistics1.getAuditCountTb() + handleWorkStatistics2.getAuditCountTb() +
                    handleWorkStatistics3.getAuditCountTb() + handleWorkStatistics4.getAuditCountTb() + handleWorkStatistics5.getAuditCountTb() +
                    handleWorkStatistics6.getAuditCountTb());

            //求审批数量环比总和
            handleWorkStatistics7.setAuditCountHb(handleWorkStatistics1.getAuditCountHb() + handleWorkStatistics2.getAuditCountHb() +
                    handleWorkStatistics3.getAuditCountHb() + handleWorkStatistics4.getAuditCountHb() + handleWorkStatistics5.getAuditCountHb() +
                    handleWorkStatistics6.getAuditCountHb());


            //求处置面积总和
            Float handleAreaSum = 0.00F;
            if (handleWorkStatistics1.getHandleArea() != null) {
                handleAreaSum = handleWorkStatistics1.getHandleArea();
            }
            if (handleWorkStatistics2.getHandleArea() != null) {
                handleAreaSum += handleWorkStatistics2.getHandleArea();
            }
            if (handleWorkStatistics3.getHandleArea() != null) {
                handleAreaSum += handleWorkStatistics3.getHandleArea();
            }
            if (handleWorkStatistics4.getHandleArea() != null) {
                handleAreaSum += handleWorkStatistics4.getHandleArea();
            }
            if (handleWorkStatistics5.getHandleArea() != null) {
                handleAreaSum += handleWorkStatistics5.getHandleArea();
            }
            if (handleWorkStatistics6.getHandleArea() != null) {
                handleAreaSum += handleWorkStatistics6.getHandleArea();
            }
            handleWorkStatistics7.setHandleArea(handleAreaSum);

            //求处置面积同比总和
            Float handleAreaTbSum = 0.00F;
            if (handleWorkStatistics1.getHandleAreaTb() != null) {
                handleAreaTbSum = handleWorkStatistics1.getHandleAreaTb();
            }
            if (handleWorkStatistics2.getHandleAreaTb() != null) {
                handleAreaTbSum += handleWorkStatistics2.getHandleAreaTb();
            }
            if (handleWorkStatistics3.getHandleAreaTb() != null) {
                handleAreaTbSum += handleWorkStatistics3.getHandleAreaTb();
            }
            if (handleWorkStatistics4.getHandleAreaTb() != null) {
                handleAreaTbSum += handleWorkStatistics4.getHandleAreaTb();
            }
            if (handleWorkStatistics5.getHandleAreaTb() != null) {
                handleAreaTbSum += handleWorkStatistics5.getHandleAreaTb();
            }
            if (handleWorkStatistics6.getHandleAreaTb() != null) {
                handleAreaTbSum += handleWorkStatistics6.getHandleAreaTb();
            }
            handleWorkStatistics7.setHandleAreaTb(handleAreaTbSum);

            //求处置面积环比总和
            Float handleAreaHbSum = 0.00F;
            if (handleWorkStatistics1.getHandleAreaHb() != null) {
                handleAreaHbSum = handleWorkStatistics1.getHandleAreaHb();
            }
            if (handleWorkStatistics2.getHandleAreaHb() != null) {
                handleAreaHbSum += handleWorkStatistics2.getHandleAreaHb();
            }
            if (handleWorkStatistics3.getHandleAreaHb() != null) {
                handleAreaHbSum += handleWorkStatistics3.getHandleAreaHb();
            }
            if (handleWorkStatistics4.getHandleAreaHb() != null) {
                handleAreaHbSum += handleWorkStatistics4.getHandleAreaHb();
            }
            if (handleWorkStatistics5.getHandleAreaHb() != null) {
                handleAreaHbSum += handleWorkStatistics5.getHandleAreaHb();
            }
            if (handleWorkStatistics6.getHandleAreaHb() != null) {
                handleAreaHbSum += handleWorkStatistics6.getHandleAreaHb();
            }
            handleWorkStatistics7.setHandleAreaHb(handleAreaHbSum);

            //求处置金额总和
            BigDecimal handleMoneySum = new BigDecimal(0.00);
            if (handleWorkStatistics3.getHandleMoney() != null) {
                handleMoneySum = handleMoneySum.add(handleWorkStatistics3.getHandleMoney());
            }
            if (handleWorkStatistics6.getHandleMoney() != null) {
                handleMoneySum = handleMoneySum.add(handleWorkStatistics6.getHandleMoney());
            }
            handleWorkStatistics7.setHandleMoney(handleMoneySum);

            //求处置金额同比总和
            BigDecimal handleMoneyTbSum = new BigDecimal(0.00);
            if (handleWorkStatistics3.getHandleMoneyTb() != null) {
                handleMoneyTbSum = handleMoneyTbSum.add(handleWorkStatistics3.getHandleMoneyTb());
            }
            if (handleWorkStatistics6.getHandleMoneyTb() != null) {
                handleMoneyTbSum = handleMoneyTbSum.add(handleWorkStatistics6.getHandleMoneyTb());
            }
            handleWorkStatistics7.setHandleMoneyTb(handleMoneyTbSum);

            //求处置金额环比总和
            BigDecimal handleMoneyHbSum = new BigDecimal(0.00);
            if (handleWorkStatistics3.getHandleMoneyHb() != null) {
                handleMoneyHbSum = handleMoneyHbSum.add(handleWorkStatistics3.getHandleMoneyHb());
            }
            if (handleWorkStatistics6.getHandleMoneyHb() != null) {
                handleMoneyHbSum = handleMoneyHbSum.add(handleWorkStatistics6.getHandleMoneyHb());
            }
            handleWorkStatistics7.setHandleMoneyHb(handleMoneyHbSum);

            handleWorkStatisticsList.add(handleWorkStatistics7);

            return handleWorkStatisticsList;
        } catch (Exception ex) {
            log.error("selectHandleMothCountError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 查询处置工作统计(按年)
     *
     * @param workStatisticsVo
     * @return
     */
    @Override
    public List<HandleWorkStatistics> selectHandleYearCount(WorkStatisticsVo workStatisticsVo) {
        try {
            List<HandleWorkStatistics> handleWorkStatisticsList = new ArrayList<>();

            //获取用户可以查看的单位信息
            List<Long> unitIds = authorityUnitService.getUnitIdByToken();
            workStatisticsVo.setUnitIdList(unitIds);

            Date handleDate;
            if (StringUtils.isNotBlank(workStatisticsVo.getApplyDateYear())) {
                handleDate = this.strToDate(null, workStatisticsVo.getApplyDateYear());
            } else {
                throw new ServiceException("申请时间(年)不能为空！");
            }

            String handleYearOld;
            Calendar calendar = Calendar.getInstance();
            if (handleDate != null) {
                calendar.setTime(handleDate);
                calendar.add(Calendar.YEAR, -1);
                Date repairApplyDateOld = calendar.getTime();
                handleYearOld = this.dateToStr(null, repairApplyDateOld);
                workStatisticsVo.setApplyDateYear(handleYearOld);
            }
            //得到去年减去年份对应月的相关数据 转换用途
            workStatisticsVo.setHandleWay(HandleWayEnum.TRANSFROMATION.getValue());
            HandleWorkStatistics handleWorkStatisticsYear1 = handleMapper.selectHandleCount(workStatisticsVo);

            //得到去年减去年份对应月的相关数据 出租
            workStatisticsVo.setHandleWay(HandleWayEnum.RENT.getValue());
            HandleWorkStatistics handleWorkStatisticsYear2 = handleMapper.selectHandleCount(workStatisticsVo);

            //得到去年减去年份对应月的相关数据 拍卖
            workStatisticsVo.setHandleWay(HandleWayEnum.ACUTION.getValue());
            HandleWorkStatistics handleWorkStatisticsYear3 = handleMapper.selectHandleCount(workStatisticsVo);
            //得到去年减去年份对应月的相关数据 拍卖金额
            List<Handle> handleYearList1 = handleMapper.selectHandleIds(workStatisticsVo);
            BigDecimal handleMoneyYear1 = new BigDecimal(0.00);
            if (CollectionUtils.isNotEmpty(handleYearList1)) {
                for (Handle handle : handleYearList1) {
                    Document document = mongoTemplate.findById(handle.getHandleId(), Document.class, "handle");
                    if (document != null) {
                        if (document.get("auctionDb") != null) {
                            Map<String, Object> map = (Map<String, Object>) document.get("auctionDb");
                            if (map.containsKey("winningAmount")) {
                                String winningAmount = String.valueOf(map.get("winningAmount"));
                                BigDecimal winningAmounts = new BigDecimal(winningAmount);
                                handleMoneyYear1 = handleMoneyYear1.add(winningAmounts);
                            }
                        }
                    }
                }
            }
            handleWorkStatisticsYear3.setHandleMoney(handleMoneyYear1);

            //得到去年减去年份对应月的相关数据 拆除
            workStatisticsVo.setHandleWay(HandleWayEnum.DISMANTLE.getValue());
            HandleWorkStatistics handleWorkStatisticsYear4 = handleMapper.selectHandleCount(workStatisticsVo);

            //得到去年减去年份对应月的相关数据 出借
            workStatisticsVo.setHandleWay(HandleWayEnum.LEND.getValue());
            HandleWorkStatistics handleWorkStatisticsYear5 = handleMapper.selectHandleCount(workStatisticsVo);

            //得到去年减去年份对应月的相关数据 征收
            workStatisticsVo.setHandleWay(HandleWayEnum.LEVY.getValue());
            HandleWorkStatistics handleWorkStatisticsYear6 = handleMapper.selectHandleCount(workStatisticsVo);
            //得到去年减去年份对应月的相关数据 征收金额
            List<Handle> handleYearList2 = handleMapper.selectHandleIds(workStatisticsVo);
            BigDecimal handleMoneyYear2 = new BigDecimal(0.00);
            if (CollectionUtils.isNotEmpty(handleYearList2)) {
                for (Handle handle : handleYearList2) {
                    Document document = mongoTemplate.findById(handle.getHandleId(), Document.class, "handle");
                    if (document != null) {
                        if (document.get("levyDb") != null) {
                            Map<String, Object> map = (Map<String, Object>) document.get("levyDb");
                            if (map.containsKey("levyCompensation")) {
                                String levyCompensation = String.valueOf(map.get("levyCompensation"));
                                BigDecimal levyCompensations = new BigDecimal(levyCompensation);
                                handleMoneyYear2 = handleMoneyYear2.add(levyCompensations);
                            }
                        }
                    }
                }
            }
            handleWorkStatisticsYear6.setHandleMoney(handleMoneyYear2);

            //转换用途统计
            workStatisticsVo.setHandleWay(HandleWayEnum.TRANSFROMATION.getValue());
            if (handleDate != null) {
                workStatisticsVo.setApplyDateYear(this.dateToStr(null, handleDate));
            }
            HandleWorkStatistics handleWorkStatistics1 = handleMapper.selectHandleCount(workStatisticsVo);
            handleWorkStatistics1.setHandleType(1);

            //求转换用途统计环比
            if (handleWorkStatistics1 != null && handleWorkStatisticsYear1 != null) {
                //审批数量环比
                if (handleWorkStatistics1.getAuditCount() != null && handleWorkStatisticsYear1.getAuditCount() != null) {
                    handleWorkStatistics1.setAuditCountHb(handleWorkStatistics1.getAuditCount() - handleWorkStatisticsYear1.getAuditCount());
                }
                //处置面积环比
                if (handleWorkStatistics1.getHandleArea() != null && handleWorkStatisticsYear1.getHandleArea() != null) {
                    handleWorkStatistics1.setHandleAreaHb(handleWorkStatistics1.getHandleArea() - handleWorkStatisticsYear1.getHandleArea());
                }
            }
            handleWorkStatisticsList.add(handleWorkStatistics1);

            //出租统计
            workStatisticsVo.setHandleWay(HandleWayEnum.RENT.getValue());
            if (handleDate != null) {
                workStatisticsVo.setApplyDateYear(this.dateToStr(null, handleDate));
            }
            HandleWorkStatistics handleWorkStatistics2 = handleMapper.selectHandleCount(workStatisticsVo);
            handleWorkStatistics2.setHandleType(2);

            //求出租统计环比
            if (handleWorkStatistics2 != null && handleWorkStatisticsYear2 != null) {
                //审批数量环比
                if (handleWorkStatistics2.getAuditCount() != null && handleWorkStatisticsYear2.getAuditCount() != null) {
                    handleWorkStatistics2.setAuditCountHb(handleWorkStatistics2.getAuditCount() - handleWorkStatisticsYear2.getAuditCount());
                }
                //处置面积环比
                if (handleWorkStatistics2.getHandleArea() != null && handleWorkStatisticsYear2.getHandleArea() != null) {
                    handleWorkStatistics2.setHandleAreaHb(handleWorkStatistics2.getHandleArea() - handleWorkStatisticsYear2.getHandleArea());
                }
            }
            handleWorkStatisticsList.add(handleWorkStatistics2);

            //拍卖统计
            workStatisticsVo.setHandleWay(HandleWayEnum.ACUTION.getValue());
            if (handleDate != null) {
                workStatisticsVo.setApplyDateYear(this.dateToStr(null, handleDate));
            }
            HandleWorkStatistics handleWorkStatistics3 = handleMapper.selectHandleCount(workStatisticsVo);
            handleWorkStatistics3.setHandleType(3);

            //拍卖金额
            List<Handle> handleList1 = handleMapper.selectHandleIds(workStatisticsVo);
            BigDecimal handleMoney1 = new BigDecimal(0.00);
            if (CollectionUtils.isNotEmpty(handleList1)) {
                for (Handle handle : handleList1) {
                    Document document = mongoTemplate.findById(handle.getHandleId(), Document.class, "handle");
                    if (document != null) {
                        if (document.get("auctionDb") != null) {
                            Map<String, Object> map = (Map<String, Object>) document.get("auctionDb");
                            if (map.containsKey("winningAmount")) {
                                String winningAmount = String.valueOf(map.get("winningAmount"));
                                BigDecimal winningAmounts = new BigDecimal(winningAmount);
                                handleMoney1 = handleMoney1.add(winningAmounts);
                            }
                        }
                    }
                }
            }
            handleWorkStatistics3.setHandleMoney(handleMoney1);

            //求拍卖统计环比
            if (handleWorkStatistics3 != null && handleWorkStatisticsYear3 != null) {
                //审批数量环比
                if (handleWorkStatistics3.getAuditCount() != null && handleWorkStatisticsYear3.getAuditCount() != null) {
                    handleWorkStatistics3.setAuditCountHb(handleWorkStatistics3.getAuditCount() - handleWorkStatisticsYear3.getAuditCount());
                }
                //处置面积环比
                if (handleWorkStatistics3.getHandleArea() != null && handleWorkStatisticsYear3.getHandleArea() != null) {
                    handleWorkStatistics3.setHandleAreaHb(handleWorkStatistics3.getHandleArea() - handleWorkStatisticsYear3.getHandleArea());
                }
                //处置金额环比
                if (handleWorkStatistics3.getHandleMoney() != null && handleWorkStatisticsYear3.getHandleMoney() != null) {
                    handleWorkStatistics3.setHandleMoneyHb(handleWorkStatistics3.getHandleMoney().subtract(handleWorkStatisticsYear3.getHandleMoney()));
                }
            }
            handleWorkStatisticsList.add(handleWorkStatistics3);


            //拆除统计
            workStatisticsVo.setHandleWay(HandleWayEnum.DISMANTLE.getValue());
            if (handleDate != null) {
                workStatisticsVo.setApplyDateYear(this.dateToStr(null, handleDate));
            }
            HandleWorkStatistics handleWorkStatistics4 = handleMapper.selectHandleCount(workStatisticsVo);
            handleWorkStatistics4.setHandleType(4);

            //求拆除统计环比
            if (handleWorkStatistics4 != null && handleWorkStatisticsYear4 != null) {
                //审批数量环比
                if (handleWorkStatistics4.getAuditCount() != null && handleWorkStatisticsYear4.getAuditCount() != null) {
                    handleWorkStatistics4.setAuditCountHb(handleWorkStatistics4.getAuditCount() - handleWorkStatisticsYear4.getAuditCount());
                }
                //处置面积环比
                if (handleWorkStatistics4.getHandleArea() != null && handleWorkStatisticsYear4.getHandleArea() != null) {
                    handleWorkStatistics4.setHandleAreaHb(handleWorkStatistics4.getHandleArea() - handleWorkStatisticsYear4.getHandleArea());
                }
            }
            handleWorkStatisticsList.add(handleWorkStatistics4);

            //出借统计
            workStatisticsVo.setHandleWay(HandleWayEnum.LEND.getValue());
            if (handleDate != null) {
                workStatisticsVo.setApplyDateYear(this.dateToStr(null, handleDate));
            }
            HandleWorkStatistics handleWorkStatistics5 = handleMapper.selectHandleCount(workStatisticsVo);
            handleWorkStatistics5.setHandleType(5);

            //求出借统计环比
            if (handleWorkStatistics5 != null && handleWorkStatisticsYear5 != null) {
                //审批数量环比
                if (handleWorkStatistics5.getAuditCount() != null && handleWorkStatisticsYear5.getAuditCount() != null) {
                    handleWorkStatistics5.setAuditCountHb(handleWorkStatistics5.getAuditCount() - handleWorkStatisticsYear5.getAuditCount());
                }
                //处置面积环比
                if (handleWorkStatistics5.getHandleArea() != null && handleWorkStatisticsYear5.getHandleArea() != null) {
                    handleWorkStatistics5.setHandleAreaHb(handleWorkStatistics5.getHandleArea() - handleWorkStatisticsYear5.getHandleArea());
                }
            }
            handleWorkStatisticsList.add(handleWorkStatistics5);


            //征收统计
            workStatisticsVo.setHandleWay(HandleWayEnum.LEVY.getValue());
            if (handleDate != null) {
                workStatisticsVo.setApplyDateYear(this.dateToStr(null, handleDate));
            }
            HandleWorkStatistics handleWorkStatistics6 = handleMapper.selectHandleCount(workStatisticsVo);
            handleWorkStatistics6.setHandleType(6);

            //征收金额
            List<Handle> handleList2 = handleMapper.selectHandleIds(workStatisticsVo);
            BigDecimal handleMoney2 = new BigDecimal(0.00);
            if (CollectionUtils.isNotEmpty(handleList2)) {
                for (Handle handle : handleList2) {
                    Document document = mongoTemplate.findById(handle.getHandleId(), Document.class, "handle");
                    if (document != null) {
                        if (document.get("levyDb") != null) {
                            Map<String, Object> map = (Map<String, Object>) document.get("levyDb");
                            if (map.containsKey("levyCompensation")) {
                                String levyCompensation = String.valueOf(map.get("levyCompensation"));
                                BigDecimal levyCompensations = new BigDecimal(levyCompensation);
                                handleMoney2 = handleMoney2.add(levyCompensations);
                            }
                        }
                    }
                }
            }
            handleWorkStatistics6.setHandleMoney(handleMoney2);

            //求征收统计环比
            if (handleWorkStatistics6 != null && handleWorkStatisticsYear6 != null) {
                //审批数量环比
                if (handleWorkStatistics6.getAuditCount() != null && handleWorkStatisticsYear6.getAuditCount() != null) {
                    handleWorkStatistics6.setAuditCountHb(handleWorkStatistics6.getAuditCount() - handleWorkStatisticsYear6.getAuditCount());
                }
                //处置面积环比
                if (handleWorkStatistics6.getHandleArea() != null && handleWorkStatisticsYear6.getHandleArea() != null) {
                    handleWorkStatistics6.setHandleAreaHb(handleWorkStatistics6.getHandleArea() - handleWorkStatisticsYear6.getHandleArea());
                }
                //处置金额环比
                if (handleWorkStatistics6.getHandleMoney() != null && handleWorkStatisticsYear6.getHandleMoney() != null) {
                    handleWorkStatistics6.setHandleMoneyHb(handleWorkStatistics6.getHandleMoney().subtract(handleWorkStatisticsYear6.getHandleMoney()));
                }
            }
            handleWorkStatisticsList.add(handleWorkStatistics6);

            //求合计
            HandleWorkStatistics handleWorkStatistics7 = new HandleWorkStatistics();
            handleWorkStatistics7.setHandleType(7);
            //求审批数量总和
            handleWorkStatistics7.setAuditCount(handleWorkStatistics1.getAuditCount() + handleWorkStatistics2.getAuditCount() +
                    handleWorkStatistics3.getAuditCount() + handleWorkStatistics4.getAuditCount() + handleWorkStatistics5.getAuditCount() +
                    handleWorkStatistics6.getAuditCount());
            //求审批数量环比总和
            handleWorkStatistics7.setAuditCountHb(handleWorkStatistics1.getAuditCountHb() + handleWorkStatistics2.getAuditCountHb() +
                    handleWorkStatistics3.getAuditCountHb() + handleWorkStatistics4.getAuditCountHb() + handleWorkStatistics5.getAuditCountHb() +
                    handleWorkStatistics6.getAuditCountHb());


            //求处置面积总和
            Float handleAreaSum = 0.00F;
            if (handleWorkStatistics1.getHandleArea() != null) {
                handleAreaSum = handleWorkStatistics1.getHandleArea();
            }
            if (handleWorkStatistics2.getHandleArea() != null) {
                handleAreaSum += handleWorkStatistics2.getHandleArea();
            }
            if (handleWorkStatistics3.getHandleArea() != null) {
                handleAreaSum += handleWorkStatistics3.getHandleArea();
            }
            if (handleWorkStatistics4.getHandleArea() != null) {
                handleAreaSum += handleWorkStatistics4.getHandleArea();
            }
            if (handleWorkStatistics5.getHandleArea() != null) {
                handleAreaSum += handleWorkStatistics5.getHandleArea();
            }
            if (handleWorkStatistics6.getHandleArea() != null) {
                handleAreaSum += handleWorkStatistics6.getHandleArea();
            }
            handleWorkStatistics7.setHandleArea(handleAreaSum);

            //求处置面积环比总和
            Float handleAreaHbSum = 0.00F;
            if (handleWorkStatistics1.getHandleAreaHb() != null) {
                handleAreaHbSum = handleWorkStatistics1.getHandleAreaHb();
            }
            if (handleWorkStatistics2.getHandleAreaHb() != null) {
                handleAreaHbSum += handleWorkStatistics2.getHandleAreaHb();
            }
            if (handleWorkStatistics3.getHandleAreaHb() != null) {
                handleAreaHbSum += handleWorkStatistics3.getHandleAreaHb();
            }
            if (handleWorkStatistics4.getHandleAreaHb() != null) {
                handleAreaHbSum += handleWorkStatistics4.getHandleAreaHb();
            }
            if (handleWorkStatistics5.getHandleAreaHb() != null) {
                handleAreaHbSum += handleWorkStatistics5.getHandleAreaHb();
            }
            if (handleWorkStatistics6.getHandleAreaHb() != null) {
                handleAreaHbSum += handleWorkStatistics6.getHandleAreaHb();
            }
            handleWorkStatistics7.setHandleAreaHb(handleAreaHbSum);

            //求处置金额总和
            BigDecimal handleMoneySum = new BigDecimal(0.00);
            if (handleWorkStatistics3.getHandleMoney() != null) {
                handleMoneySum = handleMoneySum.add(handleWorkStatistics3.getHandleMoney());
            }
            if (handleWorkStatistics6.getHandleMoney() != null) {
                handleMoneySum = handleMoneySum.add(handleWorkStatistics6.getHandleMoney());
            }
            handleWorkStatistics7.setHandleMoney(handleMoneySum);

            //求处置金额环比总和
            BigDecimal handleMoneyHbSum = new BigDecimal(0.00);
            if (handleWorkStatistics3.getHandleMoneyHb() != null) {
                handleMoneyHbSum = handleMoneyHbSum.add(handleWorkStatistics3.getHandleMoneyHb());
            }
            if (handleWorkStatistics6.getHandleMoneyHb() != null) {
                handleMoneyHbSum = handleMoneyHbSum.add(handleWorkStatistics6.getHandleMoneyHb());
            }
            handleWorkStatistics7.setHandleMoneyHb(handleMoneyHbSum);

            handleWorkStatisticsList.add(handleWorkStatistics7);
            return handleWorkStatisticsList;
        } catch (Exception ex) {
            log.error("selectHandleYearCountError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 日期转换成字符串
     *
     * @param dateMonth
     * @param dateYear
     * @return time
     */
    public static String dateToStr(Date dateMonth, Date dateYear) {
        String time = null;
        if (dateMonth != null) {
            time = formatMonth.format(dateMonth);
        }
        if (dateYear != null) {
            time = formatYear.format(dateYear);
        }
        return time;
    }

    /**
     * 字符串转换成日期
     *
     * @param timeMonth
     * @param timeYear
     * @return date
     */
    public static Date strToDate(String timeMonth, String timeYear) {
        Date date = null;
        try {
            if (StringUtils.isNotBlank(timeMonth)) {
                date = formatMonth.parse(timeMonth);
            }
            if (StringUtils.isNotBlank(timeYear)) {
                date = formatYear.parse(timeYear);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
