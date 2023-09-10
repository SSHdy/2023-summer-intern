package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ryzw.housemanager.dto.ContractDetailDto;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.service.IContractService;
import com.ryzw.housemanager.upload.FileOperateUtils;
import com.ryzw.housemanager.vo.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.entity.Contract;
import com.ryzw.housemanager.exception.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 租赁合同 前端控制器
 * </p>
 *
 * @author wj
 * @since 2019-08-28
 */
@Slf4j
@RestController
@RequestMapping("/contract")
@Api(value = "租赁合同管理", description = "租赁合同管理")
public class ContractController {

    @Autowired
    IContractService contractService;


    /**
     * 新增合同
     * @param contractVo
     * @return
     */
    @RequestMapping(value = "addContract", method = RequestMethod.POST)
    @ApiOperation(value = "新增合同", notes = "新增合同")
    public boolean addContract(@RequestBody @Valid ContractVo contractVo){
        return contractService.addContract(contractVo);
    }

    /**
     * 修改合同
     * @param contractVo
     * @return
     */
    @RequestMapping(value = "updateContract", method = RequestMethod.POST)
    @ApiOperation(value = "修改合同", notes = "修改合同")
    public boolean updateContract(@RequestBody @Valid ContractVo contractVo){
        return contractService.updateContract(contractVo);
    }

    /**
     * 废除合同
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "abolishContract", method = RequestMethod.POST)
    @ApiOperation(value = "废除合同", notes = "废除合同")
    public boolean abolishContract(@RequestBody @Valid BasicVo<Long> basicVo){
        return contractService.abolishContract(basicVo);
    }

    /**
     * 延期合同
     * @param contractDelayVo
     * @return
     */
    @RequestMapping(value = "delayContract", method = RequestMethod.POST)
    @ApiOperation(value = "延期合同", notes = "延期合同")
    public boolean delayContract(@RequestBody @Valid ContractDelayVo contractDelayVo){
        return contractService.delayContract(contractDelayVo);
    }

    /**
     * 租赁合同列表
     *
     * @param contractPageVo
     * @return
     */
    @RequestMapping(value = "/getContractList", method = RequestMethod.POST)
    @ApiOperation(value = "租赁合同列表", notes = "租赁合同列表")
    public IPage<Contract> getContractList(@RequestBody @Valid ContractPageVo contractPageVo) {
        return contractService.getContractList(contractPageVo);
    }


    /**
     * 合同到期提醒列表
     *
     * @param contractPageVo
     * @return
     */
    @RequestMapping(value = "/contractExpireList", method = RequestMethod.POST)
    @ApiOperation(value = "合同到期提醒列表", notes = "出租到期提醒列表")
    public Page<Contract> contractExpireList(@RequestBody @Valid ContractPageVo contractPageVo) {

        return contractService.contractExpireList(contractPageVo);
    }

    /**
     * 合同租金提醒列表
     *
     * @param contractPageVo
     * @return
     */
    @RequestMapping(value = "/contractMoneyList", method = RequestMethod.POST)
    @ApiOperation(value = "合同租金提醒列表", notes = "合同租金提醒列表")
    public Page<Contract> contractMoneyList(@RequestBody @Valid ContractPageVo contractPageVo) {

        return contractService.contractMoneyList(contractPageVo);
    }

    /**
     * 定时任务更新付款时间
     */
    @Async
    @Scheduled(cron = "${contract.scheduling}")
    public void updateContractTask() {
        try {
            QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
            queryWrapper.lt("payment_time", new Date());
            List<Contract> contractList = contractService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(contractList)) {
                for (Contract contract : contractList) {
                    Date paymentTime = contract.getPaymentTime();
                    // 使用默认时区和语言环境获得一个日历
                    Calendar calendar = Calendar.getInstance();
                    if (paymentTime != null) {
                        calendar.setTime(paymentTime);
                        if (contract.getPaymentCycle() != null) {
                            calendar.add(Calendar.MONTH, contract.getPaymentCycle());
                            Date paymentTimeNow = calendar.getTime();
                            if (contract.getLeaseEndTime() != null) {
                                if (paymentTimeNow.getTime() <= contract.getLeaseEndTime().getTime()) {
                                    contract.setPaymentTime(paymentTimeNow);
                                    //改成未读
                                    contract.setIsRead(0);
                                    contractService.updateById(contract);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("contract/updateContractTask error", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 合同详情查询
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/contractDetail", method = RequestMethod.POST)
    @ApiOperation(value = "合同详情查询", notes = "合同详情查询")
    public ContractDetailDto contractDetail(@RequestBody @Valid BasicVo<Long> basicVo){
        return contractService.contractDetail(basicVo);
    }


    /**
     * 合同附件下载
     *
     * @param contractEnclosureVo
     * @param response
     */
    @RequestMapping(value = "/downLoad", method = RequestMethod.POST)
    @ApiOperation(value = "合同附件下载", notes = "合同附件下载")
    public Object download(@RequestBody ContractEnclosureVo contractEnclosureVo, HttpServletResponse response) {
        List<Long> enclosureIdList = contractEnclosureVo.getContractEnclosureId();
        if (CollectionUtils.isEmpty(enclosureIdList)) {
            throw new ServiceException("请选择有效下载文件");
        }
        List<DownLoadDto> enclosureUrlList = contractService.selectEnclosureUrl(enclosureIdList);
        //调用下载文件并打包的接口
        String fileName = "";
        if (enclosureUrlList.size() > 1) {
            //合同文件的下载名称
            if (contractEnclosureVo.getContractId() != null && contractEnclosureVo.getEnclosureType() != null) {
                Integer downLoadType = contractEnclosureVo.getEnclosureType();
                if (downLoadType.equals(1)) {
                    fileName = "合同照片.zip";
                }
                if (downLoadType.equals(2)) {
                    fileName = "合同证件.zip";
                }
            }
        } else {
            fileName = enclosureUrlList.get(0).getDownLoadName();
        }
        FileOperateUtils.fileToZip(response, enclosureUrlList, fileName);
        return enclosureUrlList;
    }


}
