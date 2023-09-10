package com.ryzw.housemanager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ryzw.housemanager.dto.ContractDetailDto;
import com.ryzw.housemanager.dto.DownLoadDto;
import com.ryzw.housemanager.entity.Contract;
import com.ryzw.housemanager.entity.ContractEnclosure;
import com.ryzw.housemanager.entity.ContractRelationship;
import com.ryzw.housemanager.entity.RepairDic;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.mapper.ContractEnclosureMapper;
import com.ryzw.housemanager.mapper.ContractMapper;
import com.ryzw.housemanager.mapper.ContractRelationshipMapper;
import com.ryzw.housemanager.mapper.RepairDicMapper;
import com.ryzw.housemanager.service.IContractService;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.ContractDelayVo;
import com.ryzw.housemanager.vo.ContractPageVo;
import com.ryzw.housemanager.vo.ContractVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 合同表 服务实现类
 * </p>
 *
 * @author yz
 * @since 2019-08-28
 */
@Service
@Slf4j
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements IContractService {

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ContractEnclosureMapper contractEnclosureMapper;

    @Autowired
    private ContractRelationshipMapper contractRelationshipMapper;

    @Autowired
    private RepairDicMapper repairDicMapper;

    @Value("${uploadUrl.url}")
    private String url;

    /**
     * 新增租用合同
     *
     * @param contractVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addContract(ContractVo contractVo) {
        try {
            Contract contract = dozerBeanMapper.map(contractVo, Contract.class);
            Date paymentTime = this.getPaymentTime(contract);
            contract.setPaymentTime(paymentTime);

            //新增合同信息
            contractMapper.insert(contract);
            //获取新增后的配置编号
            Long contractId = contract.getContractId();
            //新增合同附件
            if (CollectionUtils.isNotEmpty(contractVo.getContractEnclosures())) {
                for (ContractEnclosure contractEnclosure : contractVo.getContractEnclosures()) {
                    contractEnclosure.setContractId(contractId);
                    contractEnclosureMapper.insert(contractEnclosure);
                }
            }

            if (CollectionUtils.isNotEmpty(contractVo.getContractRelationships())) {
                for (ContractRelationship contractRelationship : contractVo.getContractRelationships()) {
                    contractRelationship.setContractId(contractId);
                    contractRelationshipMapper.insert(contractRelationship);
                }
            }

            return true;
        } catch (Exception ex) {
            log.error("addContractError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 修改租用合同
     *
     * @param contractVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateContract(ContractVo contractVo) {
        try {
            Contract contract = dozerBeanMapper.map(contractVo, Contract.class);
            Date paymentTime = this.getPaymentTime(contract);
            contract.setPaymentTime(paymentTime);

            //修改租用合同
            contractMapper.updateById(contract);

            //删除旧租赁照片服务器所在图片
            if (CollectionUtils.isNotEmpty(contractVo.getDeleteContractEnclosureIds())) {
                for (Long contractEnclosureId : contractVo.getDeleteContractEnclosureIds()) {
                    ContractEnclosure contractEnclosure = contractEnclosureMapper.selectById(contractEnclosureId);
                    if (contractEnclosure != null) {
                        this.deleteContractEnclosure(contractEnclosure);
                    }
                }
            }

            //新增合同附件
            if (CollectionUtils.isNotEmpty(contractVo.getContractEnclosures())) {
                for (ContractEnclosure contractEnclosure : contractVo.getContractEnclosures()) {
                    contractEnclosure.setContractId(contract.getContractId());
                    contractEnclosureMapper.insert(contractEnclosure);
                }
            }

            if (CollectionUtils.isNotEmpty(contractVo.getContractRelationships())) {
                for (ContractRelationship contractRelationship : contractVo.getContractRelationships()) {
                    contractRelationshipMapper.updateById(contractRelationship);
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("updateContractError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 废除合同
     *
     * @param basicVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean abolishContract(BasicVo<Long> basicVo) {
        try {
            Contract contract = new Contract();
            contract.setContractId(basicVo.getId()).setIdDelete(1);
            contractMapper.updateById(contract);
            return true;
        } catch (Exception ex) {
            log.error("abolishContractError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 延期合同
     *
     * @param contractDelayVo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delayContract(ContractDelayVo contractDelayVo) {
        try {
            Contract contract = contractMapper.selectById(contractDelayVo.getContractId());
            if (contract.getLeaseEndTime() != null) {
                long oldLeaseEndTime = contract.getLeaseEndTime().getTime();
                long newLeaseEndTime = contractDelayVo.getLeaseEndTime().getTime();
                if (newLeaseEndTime <= oldLeaseEndTime) {
                    throw new ServiceException("延期后的时间不能比原来合同结束时间早");
                }
            }
            contract.setLeaseEndTime(contractDelayVo.getLeaseEndTime());
            contractMapper.updateById(contract);

            //删除旧租赁照片服务器所在图片
            if (CollectionUtils.isNotEmpty(contractDelayVo.getDeleteContractEnclosureIds())) {
                for (Long contractEnclosureId : contractDelayVo.getDeleteContractEnclosureIds()) {
                    ContractEnclosure contractEnclosure = contractEnclosureMapper.selectById(contractEnclosureId);
                    if (contractEnclosure != null) {
                        this.deleteContractEnclosure(contractEnclosure);
                    }
                }
            }

            //新增合同附件
            if (CollectionUtils.isNotEmpty(contractDelayVo.getContractEnclosures())) {
                for (ContractEnclosure contractEnclosure : contractDelayVo.getContractEnclosures()) {
                    contractEnclosure.setContractId(contract.getContractId());
                    contractEnclosureMapper.insert(contractEnclosure);
                }
            }
            return true;
        } catch (Exception ex) {
            log.error("delayContractError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 租赁合同列表
     *
     * @param contractPageVo
     * @return
     */
    @Override
    public IPage<Contract> getContractList(ContractPageVo contractPageVo) {
        try {
            Page<Contract> page = new Page<>(contractPageVo.getCurrentPage(), contractPageVo.getPageSize());
            QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id_delete", 0);
            if (StringUtils.isNotBlank(contractPageVo.getRentalUnit())) {
                queryWrapper.like("rental_unit", contractPageVo.getRentalUnit());
            }
            queryWrapper.orderByDesc("contract_id");
            IPage<Contract> contractPage = contractMapper.selectPage(page, queryWrapper);
            return contractPage;
        } catch (Exception ex) {
            log.error("getContractListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 获取付款时间公共方法
     *
     * @param contract
     * @return
     */
    public Date getPaymentTime(Contract contract) {
        Date paymentTime = contract.getPaymentTime();
        Date caTime = paymentTime;
        Date lastTime = null;
        //如果付款时间小于等于当前时间就加上周期
        while (caTime.getTime() <= (new Date()).getTime()) {
            Calendar ca = Calendar.getInstance();
            ca.setTime(caTime);
            lastTime = ca.getTime();
            ca.add(Calendar.MONTH, contract.getPaymentCycle());
            long time = ca.getTime().getTime();
            long time1 = (contract.getLeaseEndTime()).getTime();
            if (time <= time1) {
                caTime = ca.getTime();
            } else {
                caTime = lastTime;
                break;
            }
        }
        return caTime;
    }

    /**
     * 删除公共服务器上的租赁信息图片方法
     *
     * @param contractEnclosure
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deleteContractEnclosure(ContractEnclosure contractEnclosure) throws Exception {
        try {
            contractEnclosureMapper.deleteById(contractEnclosure);
            String enclosureUrl = contractEnclosure.getEnclosurePhysicsUrl();
            // 图片的文件路径
            String imagePath = enclosureUrl;
            if (StringUtils.isNotEmpty(imagePath)) {
                synchronized (this) {
                    Files.deleteIfExists(Paths.get(imagePath));
                }
            }
        } catch (Exception ex) {
            log.error("deleteContractEnclosureError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    /**
     * 合同到期提醒列表
     *
     * @param contractPageVo
     * @return
     */
    @Override
    public Page<Contract> contractExpireList(ContractPageVo contractPageVo) {
        try {
            Page<Contract> page = new Page<>(contractPageVo.getCurrentPage(), contractPageVo.getPageSize());
            String contractExpireDate = this.getContractExpireDate();
            return contractMapper.contractExpireList(page, contractPageVo, contractExpireDate);
        } catch (Exception ex) {
            log.error("contractExpireListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 合同租金提醒列表
     *
     * @param contractPageVo
     * @return
     */
    @Override
    public Page<Contract> contractMoneyList(ContractPageVo contractPageVo) {
        try {
            Page<Contract> page = new Page<>(contractPageVo.getCurrentPage(), contractPageVo.getPageSize());
            String contractMoneyDay = this.getContractMoneyDate();
            return contractMapper.contractMoneyList(page, contractPageVo, contractMoneyDay);
        } catch (Exception ex) {
            log.error("contractMoneyListError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * 获取合同到期提醒天数
     */
    @Transactional(rollbackFor = Exception.class)
    public String getContractExpireDate() {
        RepairDic repairDic = repairDicMapper.selectOne(new QueryWrapper<RepairDic>().eq("repair_dic_key", "contract_expire_days"));
        String contractExpireDay = repairDic.getRepairDicValue();
        return contractExpireDay;
    }

    /**
     * 获取合同租金提醒天数
     */
    @Transactional(rollbackFor = Exception.class)
    public String getContractMoneyDate() {
        RepairDic repairDic = repairDicMapper.selectOne(new QueryWrapper<RepairDic>().eq("repair_dic_key", "contract_money_days"));
        String contractMoneyDay = repairDic.getRepairDicValue();
        return contractMoneyDay;
    }

    /**
     * 计算合同到期提醒数量
     *
     * @return
     */
    public Integer contractExpireAmount() {
        String contractExpireDay = this.getContractExpireDate();
        return contractMapper.contractExpireAmount(contractExpireDay);
    }

    /**
     * 计算合同租金提醒数量
     *
     * @return
     */
    public Integer contractMoneyAmount() {
        String contractMoneyDay = this.getContractMoneyDate();
        return contractMapper.contractMoneyAmount(contractMoneyDay);
    }

    /**
     * 合同详情查询
     *
     * @param basicVo
     * @return
     */
    @Override
    public ContractDetailDto contractDetail(BasicVo<Long> basicVo) {
        return contractMapper.contractDetail(basicVo);
    }


    /**
     * 查询合同附件Url
     *
     * @param enclosureIdList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<DownLoadDto> selectEnclosureUrl(List<Long> enclosureIdList) {
        try {
            //查询要下载的租赁附件
            List<ContractEnclosure> enclosures = contractEnclosureMapper.selectBatchIds(enclosureIdList);
            //找到对应的url，循环添加到数组
            List<DownLoadDto> enclosureUrlList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(enclosures)) {
                for (ContractEnclosure enclosure : enclosures) {
                    String enclosurePhysicsUrl = enclosure.getEnclosurePhysicsUrl();
                    if (enclosures.size() > 1) {
                        DownLoadDto downLoadDto = new DownLoadDto();
                        downLoadDto.setPath("file:///" + enclosurePhysicsUrl);
                        enclosureUrlList.add(downLoadDto);
                    } else {
                        DownLoadDto downLoadDto = new DownLoadDto();
                        String str = enclosure.getEnclosureName();
                        downLoadDto.setDownLoadName(str);
                        downLoadDto.setPath(enclosurePhysicsUrl);
                        enclosureUrlList.add(downLoadDto);

                    }
                }
            }
            return enclosureUrlList;
        } catch (Exception ex) {
            log.error("selectEnclosureUrlError", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

}
