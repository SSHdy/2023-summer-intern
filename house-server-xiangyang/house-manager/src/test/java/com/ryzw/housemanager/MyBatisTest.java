package com.ryzw.housemanager;

import com.ryzw.housemanager.mapper.UseUnitMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author yz
 * @since 2019/2/28
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisTest {

    @Autowired
    private UseUnitMapper useUnitMapper;

    @Test
    public void generator() {
        Map<String, Object> map = new HashMap<>();
        map.put("housingUse_id", 205);
        useUnitMapper.deleteByMap(map);
    }


}
