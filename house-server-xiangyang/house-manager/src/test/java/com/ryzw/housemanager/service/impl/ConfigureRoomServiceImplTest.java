package com.ryzw.housemanager.service.impl;

import com.ryzw.housemanager.dto.NewUnitDto;
import com.ryzw.housemanager.service.IConfigureRoomService;
import com.ryzw.housemanager.vo.AllocationUnitVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * <p>
 *
 * </p>
 *
 * @author mfl
 * @since 2019/6/24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigureRoomServiceImplTest {

    @Autowired
    IConfigureRoomService iConfigureRoomService;
    @Test
    public void configNewUnit() {
        AllocationUnitVo allocationUnitVo =new AllocationUnitVo();
        List<NewUnitDto> newUnit = iConfigureRoomService.configNewUnit(allocationUnitVo);

    }
}