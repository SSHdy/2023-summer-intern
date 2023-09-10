package com.ryzw.housemanager.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ryzw.housemanager.dto.ContractDetailDto;
import com.ryzw.housemanager.entity.Contract;
import com.ryzw.housemanager.vo.BasicVo;
import com.ryzw.housemanager.vo.ContractPageVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 合同表 Mapper 接口
 * </p>
 *
 * @author yz
 * @since 2019-08-28
 */
public interface ContractMapper extends BaseMapper<Contract> {

    /**
     * 合同到期提醒列表
     *
     * @param page
     * @param contractPageVo
     * @param contractExpireDate
     * @return
     */
    Page<Contract> contractExpireList(Page<Contract> page, @Param("contractPageVo") ContractPageVo contractPageVo, @Param("contractExpireDate") String contractExpireDate);

    /**
     * 合同租金提醒列表
     *
     * @param page
     * @param contractPageVo
     * @return
     */
    Page<Contract> contractMoneyList(Page<Contract> page, @Param("contractPageVo") ContractPageVo contractPageVo, @Param("contractMoneyDay") String contractMoneyDay);

    /**
     * 计算合同到期提醒数量
     *
     * @param contractExpireDay
     * @return
     */
    Integer contractExpireAmount(@Param("contractExpireDay") String contractExpireDay);

    /**
     * 计算合同租金提醒数量
     *
     * @param contractMoneyDay
     * @return
     */
    Integer contractMoneyAmount(@Param("contractMoneyDay")String contractMoneyDay);

    /**
     * 合同详情查询
     * @param basicVo
     * @return
     */
    ContractDetailDto contractDetail(@Param("id") BasicVo<Long> basicVo);

}
