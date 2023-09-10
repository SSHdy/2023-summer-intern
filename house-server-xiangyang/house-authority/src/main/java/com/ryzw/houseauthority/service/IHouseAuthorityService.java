package com.ryzw.houseauthority.service;

import com.ryzw.houseauthority.config.TokenFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * <p>
 *远程调用住房管理接口
 * </p>
 *
 * @author mfl
 * @since 2019/4/15
 */
@FeignClient(serviceId = "house-service", name = "house-service",
        configuration = TokenFeignConfiguration.class
)
public interface IHouseAuthorityService {

    /**
     * 更新维修工作流
     * @param repairMap
     * @return
     */
    @RequestMapping(value = "/repairApply/updateRepairAuthority", method = RequestMethod.POST)
    boolean updateRepairAuthority(@RequestBody Map<String,Object> repairMap);

    /**
     * 更新处置工作流
     * @param handleMap
     * @return
     */
    @RequestMapping(value = "/handle/updateHandleAuthority", method = RequestMethod.POST)
    boolean updateHandleAuthority(@RequestBody Map<String,Object> handleMap);

    /**
     * 更新配置工作流
     * @param configMap
     * @return
     */
    @RequestMapping(value = "/configure/updateConfigureAuthority", method = RequestMethod.POST)
    boolean updateConfigureAuthority(@RequestBody Map<String,Object> configMap);


}
