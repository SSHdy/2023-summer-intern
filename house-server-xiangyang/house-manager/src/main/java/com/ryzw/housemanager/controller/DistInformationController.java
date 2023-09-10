package com.ryzw.housemanager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ryzw.housemanager.entity.DistInformation;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IDistInformationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 *     行政区划前端控制器
 * </p>
 *
 * @author wj
 * @since 2019-09-04 0004
 */
@RestController
@Api(value = "行政区划管理", description = "行政区划管理")
@RequestMapping("/distInformation")
@Slf4j
public class DistInformationController {

    @Autowired
    IDistInformationService distInformationService;

    /**
     * 查询行政区划信息
     * @return
     */
    @RequestMapping(value = "/selectDistInfo",method = RequestMethod.POST)
    @ApiOperation(value = "查询行政区划信息", notes = "查询行政区划信息")
    public DistInformation selectDistInfo(){
        DistInformation distInformation = new DistInformation();
        try {
            DistInformation dist = distInformationService.getOne(new QueryWrapper<>());
            if(dist == null){
                return distInformation;
            }
            return dist;
        }catch (Exception ex){
            log.error("selectDistInfoError", ex);
            throw new ServiceException("行政区划信息有多条！");
        }
    }

}
