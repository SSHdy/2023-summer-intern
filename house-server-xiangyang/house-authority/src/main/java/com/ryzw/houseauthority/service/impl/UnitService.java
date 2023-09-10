package com.ryzw.houseauthority.service.impl;

import com.ryzw.houseauthority.service.IUnitService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author yz
 * @since 2018/12/29
 */
@Component
public class UnitService implements IUnitService {

    /**
     * 调用manager里面的方法查询unit
     * @param ids
     * @return
     */
    @Override
    public List<Map<Object, Object>> getRoleUnit(List<Long> ids) {
        return null;
    }
}
