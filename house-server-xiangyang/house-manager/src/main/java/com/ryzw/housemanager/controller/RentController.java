package com.ryzw.housemanager.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.dto.RentDetailDto;
import com.ryzw.housemanager.dto.RentListDto;
import com.ryzw.housemanager.dto.UpdateIsReadDto;
import com.ryzw.housemanager.entity.Contract;
import com.ryzw.housemanager.entity.Rent;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IContractService;
import com.ryzw.housemanager.service.IRentService;
import com.ryzw.housemanager.upload.FileOperateUtils;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.DownLoadEnclosureVo;
import com.ryzw.housemanager.vo.RentEnclosureVo;
import com.ryzw.housemanager.vo.RentPageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapper;
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
import java.util.Map;

/**
 * <p>
 * 租赁房间信息表 前端控制器
 * </p>
 *
 * @author mfl
 * @since 2019-03-13
 */
@RestController
@RequestMapping("/rent")
@Slf4j
@Api(value = "租赁管理", description = "租赁管理")
public class RentController {
    @Autowired
    private IRentService rentService;

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Autowired
    private IContractService contractService;


    /**
     * 房屋租赁信息列表
     *
     * @param rentPageVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "房屋租赁信息列表", notes = "房屋租赁信息列表")
    public Page<RentListDto> getList(@RequestBody @Valid RentPageVo rentPageVo) {

        return rentService.getlist(rentPageVo);
    }

    /**
     * 新增租赁信息,租赁附件信息,院落楼座关系,租赁关系
     *
     * @param rentEnclosureVo
     * @return
     */
    @ApiOperation(value = "新增租赁信息,租赁附件信息，院落楼座关系", notes = "新增租赁信息,租赁附件信息，院落楼座关系")
    @RequestMapping(value = "addRent", method = RequestMethod.POST)
    public boolean addRent(@RequestBody @Valid RentEnclosureVo rentEnclosureVo) {
        Rent rent = dozerBeanMapper.map(rentEnclosureVo, Rent.class);
        return rentService.addRent(rent, rentEnclosureVo.getAddressList(), rentEnclosureVo.getHousingUseList(), rentEnclosureVo.getState(), rentEnclosureVo.getSoilList());
    }

    /**
     * 修改租赁信息,租赁附件信息，院落楼座关系
     *
     * @param rentEnclosureVo
     * @return
     */
    @ApiOperation(value = "修改租赁信息,租赁附件信息，院落楼座关系", notes = "修改租赁信息,租赁附件信息，院落楼座关系")
    @RequestMapping(value = "updateRent", method = RequestMethod.POST)
    public boolean updateRent(@RequestBody @Valid RentEnclosureVo rentEnclosureVo) {
        return rentService.updateRent(rentEnclosureVo);
    }

    /**
     * 租赁详情
     *
     * @param basicVo
     * @return
     */
    @ApiOperation(value = "租赁详情", notes = "租赁详情")
    @RequestMapping(value = "rentDetail", method = RequestMethod.POST)
    public RentDetailDto detail(@RequestBody @Valid BasicVo<Long> basicVo) {
        RentDetailDto rentDetailDto = rentService.selectRentDetail(basicVo.getId());
        return rentDetailDto;
    }

    /**
     * 删除租赁信息
     *
     * @param basicVo
     * @return
     */
    @ApiOperation(value = "删除租赁信息", notes = "删除租赁信息")
    @RequestMapping(value = "deleteRent", method = RequestMethod.POST)
    public boolean deleteRent(@RequestBody @Valid BasicVo<Long> basicVo) {
        return rentService.deleteRent(basicVo.getId());
    }

    /**
     * 附件下载
     *
     * @param downLoadEnclosureVo
     * @param response
     */
    @RequestMapping(value = "/downLoad", method = RequestMethod.POST)
    @ApiOperation(value = "下载附件", notes = "下载附件")
    public Object download(@RequestBody DownLoadEnclosureVo downLoadEnclosureVo, HttpServletResponse response) {
        List<Long> enclosureIdList = downLoadEnclosureVo.getEnclosureIdList();
        if (CollectionUtils.isEmpty(enclosureIdList)) {
            throw new ServiceException("请选择有效下载文件");
        }
        List<DownLoadDto> enclosureUrlList = rentService.selectEnclosureUrl(enclosureIdList);
        //调用下载文件并打包的接口
        String fileName = "";
        if (enclosureUrlList.size() > 1) {
            //出租出借文件的下载名称
            if (downLoadEnclosureVo.getRentId() != null && downLoadEnclosureVo.getDownLoadType() != null) {
                Integer downLoadType = downLoadEnclosureVo.getDownLoadType();
                if (downLoadType.equals(1)) {
                    fileName = "出租合同资料.zip";
                }
                if (downLoadType.equals(2)) {
                    fileName = "出租证件.zip";
                }
                if (downLoadType.equals(3)) {
                    fileName = "出借合同资料.zip";
                }
                if (downLoadType.equals(4)) {
                    fileName = "出借证件.zip";
                }
            }
        } else {
            fileName = enclosureUrlList.get(0).getDownLoadName();
        }
        FileOperateUtils.fileToZip(response, enclosureUrlList, fileName);
        return enclosureUrlList;
    }

    /**
     * 退租
     *
     * @param basicVo
     * @return
     */
    @RequestMapping(value = "/rentWithdrawal", method = RequestMethod.POST)
    @ApiOperation(value = "退租", notes = "退租")
    public boolean rentWithdrawal(@RequestBody @Valid BasicVo<Long> basicVo) {
        return rentService.rentWithdrawal(basicVo.getId()) > 0;
    }


    /**
     * 租赁房屋联系人列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "rentContacts", method = RequestMethod.POST)
    @ApiOperation(value = "租赁房屋联系人列表", notes = "租赁房屋联系人列表")
    public List<Rent> rentContacts() {
        QueryWrapper<Rent> wrapper = new QueryWrapper<>();
        wrapper.select("rent_id", "contacts");
        return rentService.list(wrapper);
    }

    /**
     * 查询租赁房间将要付款的房间数量和维修提醒的数量
     *
     * @return
     */
    @RequestMapping(value = "/remindNum", method = RequestMethod.POST)
    @ApiOperation(value = "查询租赁房间将要付款的房间数量和维修提醒的数量", notes = "查询租赁房间将要付款的房间数量和维修提醒的数量")
    public Map<Object, Object> remindNum() {

        return rentService.remindNum();
    }


    /**
     * 出租租金提醒列表
     *
     * @return
     */
    @RequestMapping(value = "/rentRmindList", method = RequestMethod.POST)
    @ApiOperation(value = "出租租金提醒列表", notes = "出租租金提醒列表")
    public Page<RentListDto> rentRmindList(@RequestBody RentPageVo rentPageVo) {

        return rentService.rentRmindList(rentPageVo);
    }

    /**
     * 定时任务更新付款时间
     */
    @Async
    @Scheduled(cron = "${rent.scheduling}")
    public void updateRentTask() {
        try {
            QueryWrapper<Rent> queryWrapper = new QueryWrapper<>();
            queryWrapper.lt("payment_time", new Date());
            queryWrapper.eq("type", 1);
            List<Rent> rentList = rentService.list(queryWrapper);
            if (CollectionUtils.isNotEmpty(rentList)) {
                for (Rent rent : rentList) {
                    Date paymentTime = rent.getPaymentTime();
                    Calendar calendar = Calendar.getInstance();
                    if (paymentTime != null) {
                        calendar.setTime(paymentTime);
                        if (rent.getPaymentCycle() != null) {
                            calendar.add(Calendar.MONTH, rent.getPaymentCycle());
                            Date paymentTimeNow = calendar.getTime();
                            if (rent.getRentEnd() != null) {
                                if (paymentTimeNow.getTime() <= rent.getRentEnd().getTime()) {
                                    rent.setPaymentTime(paymentTimeNow);
                                    //改成未读
                                    rent.setIsRead(0);
                                    rentService.updateById(rent);
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            log.error("updateRentTaskError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 修改出租提醒状态
     *
     * @param updateIsReadDto
     * @return
     */
    @RequestMapping(value = "/updateIsRead", method = RequestMethod.POST)
    @ApiOperation(value = "修改出租提醒状态", notes = "修改出租提醒状态")
    public boolean updateIsRead(@RequestBody @Valid UpdateIsReadDto updateIsReadDto) {
        try {
            //修改租赁的Wrapper
            UpdateWrapper<Rent> rentWrapper = new UpdateWrapper();
            rentWrapper.in("rent_id", updateIsReadDto.getRentIdList());
            //修改社会租用的Wrapper
            UpdateWrapper<Contract> conractWrapper = new UpdateWrapper();
            conractWrapper.in("contract_id", updateIsReadDto.getContractIdList());
            Integer type = updateIsReadDto.getType();
            if (type.equals(1)) {
                //修改出租租金提醒状态
                rentService.update(new Rent().setIsRead(1), rentWrapper);
            } else if (type.equals(2)) {
                //修改出租到期提醒状态
                rentService.update(new Rent().setIsExpire(1), rentWrapper);
            } else if (type.equals(3)) {
                //修改租用社会单位租金付款提醒状态
                contractService.update(new Contract().setIsRead(1), conractWrapper);
            } else if (type.equals(4)) {
                //修改租用社会单位到期提醒状态
                contractService.update(new Contract().setIsExpire(1), conractWrapper);
            }
            return true;
        } catch (Exception ex) {
            log.error("updateIsReadError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 出租出借到期提醒列表
     *
     * @return
     */
    @RequestMapping(value = "/rentExpireList", method = RequestMethod.POST)
    @ApiOperation(value = "出租到期提醒列表", notes = "出租到期提醒列表")
    public Page<RentListDto> rentExpireList(@RequestBody RentPageVo rentPageVo) {

        return rentService.rentExpireList(rentPageVo);
    }


}
