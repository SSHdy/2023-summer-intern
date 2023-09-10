package com.ryzw.houseauthority.service;

import com.ryzw.houseauthority.service.impl.UnitService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  unit
 * </p>
 *
 * @author yz
 * @since 2018/12/29
 */
@FeignClient(value = "house-service",fallback = UnitService.class)
public interface IUnitService {

    /**
     * 调用manager里面的方法查询unit
     * @param ids
     * @return
     */
    @RequestMapping(value = "/unit/findByRoleUnit",method = RequestMethod.POST)
    List<Map<Object,Object>> getRoleUnit(List<Long> ids);
}
