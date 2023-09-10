package com.ryzw.housemanager.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ryzw.housemanager.dto.ContractDetailDto;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.entity.Contract;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.ContractDelayVo;
import com.ryzw.housemanager.vo.ContractPageVo;
import com.ryzw.housemanager.vo.ContractVo;

import java.util.List;


/**
 * <p>
 * 合同表 服务类
 * </p>
 *
 * @author yz
 * @since 2019-08-28
 */
public interface IContractService extends IService<Contract> {

    /**
     * 新增租用合同
     * @param contractVo
     * @return
     */
    boolean addContract(ContractVo contractVo);

    /**
     * 合同到期提醒列表
     *
     * @param contractPageVo
     * @return
     */
    Page<Contract> contractExpireList(ContractPageVo contractPageVo);

    /**
     * 合同租金提醒列表
     *
     * @param contractPageVo
     * @return
     */
    Page<Contract> contractMoneyList(ContractPageVo contractPageVo);

    /**
     * 修改租用合同
     * @param contractVo
     * @return
     */
    boolean updateContract(ContractVo contractVo);

    /**
     * 废除合同
     * @param basicVo
     * @return
     */
    boolean abolishContract(BasicVo<Long> basicVo);

    /**
     * 延期合同
     * @param contractDelayVo
     * @return
     */
    boolean delayContract(ContractDelayVo contractDelayVo);

    /**
     * 租赁合同列表
     * @param contractPageVo
     * @return
     */
    IPage<Contract> getContractList(ContractPageVo contractPageVo);

    /**
     * 合同详情查询
     * @param basicVo
     * @return
     */
    ContractDetailDto contractDetail(BasicVo<Long> basicVo);

    /**
     * 查询合同附件Url
     * @param enclosureIdList
     * @return
     */
    List<DownLoadDto> selectEnclosureUrl(List<Long> enclosureIdList);
}
