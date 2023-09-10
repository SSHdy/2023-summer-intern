package com.ryzw.housemanager.controller;


import com.ryzw.housemanager.entity.Record;
import com.ryzw.housemanager.exception.ServiceException;
import com.ryzw.housemanager.service.IRecordService;
import com.ryzw.housemanager.vo.RecordVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * 备案表 前端控制器
 * </p>
 *
 * @author wj
 * @since 2019-01-30
 */
@RestController
@Slf4j
@Api(value = "备案信息管理", description = "备案信息管理")
@RequestMapping("/record")
public class RecordController {

    @Autowired
    IRecordService recordService;


    /**
     * 提交备案记录
     */
    @RequestMapping(value = "updateRecord",method = RequestMethod.POST)
    @ApiOperation(value = "提交备案记录",notes = "提交备案记录")
    public boolean updateRecord(@RequestBody @Valid RecordVo recordVo){
        try {
            Record record = recordService.getById(recordVo.getRecordId());
            if(record!=null){
                record.setIsRecord(1);
                return recordService.updateById(record);
            }else{
                return false;
            }
        }catch (Exception ex){
            log.error("error",ex);
            throw new  ServiceException(ex.getMessage());
        }
    }

}
