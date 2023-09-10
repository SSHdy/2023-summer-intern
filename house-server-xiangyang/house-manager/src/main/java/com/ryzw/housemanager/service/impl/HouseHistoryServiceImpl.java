package com.ryzw.housemanager.service.impl;

import com.ryzw.housemanager.entity.HouseHistory;
import com.ryzw.housemanager.mapper.HouseHistoryMapper;
import com.ryzw.housemanager.service.IHouseHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 房屋信息历史表 服务实现类
 * </p>
 *
 * @author yz
 * @since 2019-08-23
 */
@Service
public class HouseHistoryServiceImpl extends ServiceImpl<HouseHistoryMapper, HouseHistory> implements IHouseHistoryService {

}
